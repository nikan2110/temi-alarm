package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.nikita.doroshenko.japanmeeting.models.ChatGPTAnswerModel
import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel
import com.nikita.doroshenko.japanmeeting.retrofit.RetrofitClientChatGPTServer
import com.nikita.doroshenko.japanmeeting.retrofit.RetrofitClientTemiServer
import com.nikita.doroshenko.japanmeeting.services.*
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.constants.SdkConstants
import com.robotemi.sdk.listeners.OnRobotReadyListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MenuActivity : BaseActivity(), OnRobotReadyListener, Robot.AsrListener, Robot.TtsListener  {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var buttonCheckList: Button
    private lateinit var buttonTemiInstructions: Button
    private lateinit var buttonBackLanguagePage: Button
    private lateinit var buttonTemiInteraction: Button

    private lateinit var parentLayoutMenuActivity: LinearLayout
    private lateinit var mainLayoutMenuActivity: LinearLayout

    private lateinit var robot: Robot

    private lateinit var language: String

    private lateinit var progressBarMenu: ProgressBar

    private var retrofit = RetrofitClientTemiServer.getClient()
    private var checkBoxListService = retrofit.create(CheckBoxListService::class.java)

    private var chatGPTAnswerText = ""

    private var retrofitChatGPTServer = RetrofitClientChatGPTServer.getClient()
    private var chatGPTService = retrofitChatGPTServer.create(ChatGptService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        language = Locale.getDefault().language
        Log.i("MenuActivity", "received language for temi speech $language")

        robot = Robot.getInstance()

        mediaPlayer = MediaPlayer.create(this, R.raw.siren)

        progressBarMenu = findViewById(R.id.pb_main_menu)

        parentLayoutMenuActivity = findViewById(R.id.parent_layout_menu_activity)
        mainLayoutMenuActivity = findViewById(R.id.main_layout_menu)

        buttonCheckList = findViewById(R.id.btn_check_list)
        buttonTemiInstructions = findViewById(R.id.btn_instructions)
        buttonBackLanguagePage = findViewById(R.id.btn_back_to_language_page)

        buttonCheckList.setOnClickListener {
            val destinationActivity = CheckListActivity::class.java
            val checkListActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(checkListActivityIntent)
        }

        buttonTemiInstructions.setOnClickListener {
            val instructionText = resources.getString(R.string.temi_instructions)
            robotSpeak(instructionText, true, language)
        }


        buttonBackLanguagePage.setOnClickListener {
            val destinationActivity = LanguageActivity::class.java
            val languageActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(languageActivityIntent)
        }

        buttonTemiInteraction = findViewById(R.id.btn_temi_interaction)
        buttonTemiInteraction.setOnClickListener {
            robot.askQuestion("שאל אותי הכל על הכנת המרפאה למעבר משגרה לחירום. בבקשה תגיד ״ תגידי לי ״ ושאלה שלך")
        }

        checkBoxListService.getAllCheckBoxesByLanguageAndStatus(language, false).enqueue(object: Callback<List<CheckBoxModel>>{
            override fun onResponse(call: Call<List<CheckBoxModel>>, response: Response<List<CheckBoxModel>>) {
                showElementsAndHideProgressBar()
                val checkBoxModels: List<CheckBoxModel>? = response.body()
                if (checkBoxModels != null) {
                    if (checkBoxModels.isEmpty()) {
                        buttonCheckList.background = AppCompatResources.getDrawable(this@MenuActivity,R.drawable.check_list_button_green)
                    }
                }
            }

            override fun onFailure(call: Call<List<CheckBoxModel>>, t: Throwable) {
                val errorMessage = t.message
                Log.e("getCheckListsFailureByLanguageAndStatus", "Error retrieving data from server: $errorMessage")
            }

        })


        val mailChecker = Thread {
            while (true) {
                if (MailService.check()) {
                    Log.i("MailServiceCheck", "Started trigger")
                    mediaPlayer.start()
//                    MainPageActivity.instance.robotSpeak("Please access the protected space near the elevator", true)
                    Toast.makeText(this, "Please access the protected space near the elevator", Toast.LENGTH_SHORT).show()
                    Thread.sleep(5000)
//                    MainPageActivity.instance.robotSpeak("Please access the protected space near the elevator", true)
                    Toast.makeText(this, "Please access the protected space near the elevator", Toast.LENGTH_SHORT).show()
                    Thread.sleep(5000)
                }
                Thread.sleep(5000)
            }
        }
        mailChecker.start()


    }

    private fun showElementsAndHideProgressBar() {
        buttonCheckList.visibility = View.VISIBLE
        buttonTemiInstructions.visibility = View.VISIBLE
        progressBarMenu.visibility = View.GONE
    }

    private fun showElementsAndHidePicture() {
        parentLayoutMenuActivity.background =  ResourcesCompat.getDrawable(resources, R.drawable.main_background, null)
        mainLayoutMenuActivity.visibility = View.VISIBLE
    }



    override fun onStart() {
        super.onStart()
        robot.addOnRobotReadyListener(this)
        robot.addAsrListener(this)
        robot.addTtsListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnRobotReadyListener(this)
        robot.removeAsrListener(this)
        robot.removeTtsListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            try {
            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    override fun onAsrResult(asrResult: String) {
        Log.i("AsrResult", "Received asrResult: $asrResult")
        try {
            val metadata = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData ?: return
            if (!robot.isSelectedKioskApp()) {
                return
            }
            if (!metadata.getBoolean(SdkConstants.METADATA_OVERRIDE_NLU)) {
                return
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return
        }
        when {
            asrResult.startsWith("תגידי לי", ignoreCase = true) -> {
                val question = asrResult.drop(8)
                if (question.isNotEmpty()) {
                    val body: HashMap<String, String> = HashMap()
                    body["question"] = question
                    robotSpeak("אני חושבת ...", false, language)
                    sendQuestion(body)
                    mainLayoutMenuActivity.visibility = View.GONE
                    parentLayoutMenuActivity.background =  ResourcesCompat.getDrawable(resources, R.drawable.think_picture, null)


                }
            } else -> {
                robot.askQuestion("Sorry, I don't understand your question. Try again, say:   ״ תגידי לי ״ and your question")
            }
        }
    }

    private fun sendQuestion(body: HashMap<String, String>) {
        chatGPTService.askQuestion(body).enqueue(object: Callback<ChatGPTAnswerModel> {
            override fun onResponse(call: Call<ChatGPTAnswerModel>, response: Response<ChatGPTAnswerModel>) {
                showElementsAndHidePicture()
                val answerChatGPT: ChatGPTAnswerModel? = response.body()
                if (answerChatGPT != null && answerChatGPT.answer.isNotEmpty()) {
                    chatGPTAnswerText = answerChatGPT.answer
                    robotSpeak(chatGPTAnswerText, true, language)
                }
            }

            override fun onFailure(call: Call<ChatGPTAnswerModel>, t: Throwable) {
                Toast.makeText(this@MenuActivity, "Something wrong with server", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun robotSpeak(text: String, showConversationLayer: Boolean, language: String) {
        when(language) {
            "iw" -> {
                robot.speak(
                    TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
                    TtsRequest.Language.HE_IL))
            }
            "ru" -> {
                robot.speak(
                    TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
                    TtsRequest.Language.RU_RU))
            }
            "en" -> {
                robot.speak(
                    TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
                    TtsRequest.Language.EN_US))
            }
        }
    }

    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
        Log.i("Status", ttsRequest.status.toString())
        if (chatGPTAnswerText.isNotEmpty() && ttsRequest.status == TtsRequest.Status.COMPLETED) {
            chatGPTAnswerText = ""
            robot.askQuestion( "אני עדיין יכולה לעזור לך ?")
        }
    }
}
