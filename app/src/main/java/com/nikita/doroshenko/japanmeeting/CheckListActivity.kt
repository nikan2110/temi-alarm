package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.nikita.doroshenko.japanmeeting.layouts.CheckBoxLayout
import com.nikita.doroshenko.japanmeeting.models.ChatGPTAnswerModel
import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel
import com.nikita.doroshenko.japanmeeting.services.ChatGptService
import com.nikita.doroshenko.japanmeeting.services.CheckBoxListService
import com.nikita.doroshenko.japanmeeting.retrofit.RetrofitClientChatGPTServer
import com.nikita.doroshenko.japanmeeting.retrofit.RetrofitClientTemiServer
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.constants.SdkConstants
import com.robotemi.sdk.listeners.OnConversationStatusChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

class CheckListActivity : BaseActivity(), OnRobotReadyListener, Robot.AsrListener{

    private lateinit var buttonBackMenu: Button
    private lateinit var buttonTemiInteraction: Button
    private lateinit var checkBoxContent: LinearLayout
    private lateinit var progressBarCheckList: ProgressBar

    private lateinit var robot: Robot

    private lateinit var language: String

    private var isCheckingStart = false

    private var checkBoxLayoutsUnchecked: List<CheckBoxLayout> = ArrayList<CheckBoxLayout>()

    private var retrofitTemiServer = RetrofitClientTemiServer.getClient()
    private var retrofitChatGPTServer = RetrofitClientChatGPTServer.getClient()

    private var checkBoxListService = retrofitTemiServer.create(CheckBoxListService::class.java)
    private var chatGPTService = retrofitChatGPTServer.create(ChatGptService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        language = Locale.getDefault().language
        Log.i("CheckListActivity", "received language for checklist $language")

        robot = Robot.getInstance()

        checkBoxContent = findViewById(R.id.ll_check_box_content)

        progressBarCheckList = findViewById(R.id.pb_check_list)
        progressBarCheckList.visibility = View.VISIBLE

        buttonBackMenu = findViewById(R.id.btn_back_to_menu)
        buttonBackMenu.setOnClickListener {
            val destinationActivity = MenuActivity::class.java
            val menuActivityIntent = Intent(this@CheckListActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }

        buttonTemiInteraction = findViewById(R.id.btn_temi_interaction)
        buttonTemiInteraction.setOnClickListener {
            if (checkBoxLayoutsUnchecked.isNotEmpty()) {
                val instructionTextFirstPart = resources.getString(R.string.temi_ask_about_tasks_first_part)
                val instructionTextSecondPart = resources.getString(R.string.temi_ask_about_tasks_second_part)
                robot.askQuestion(
                    " $instructionTextFirstPart ${checkBoxLayoutsUnchecked.size} $instructionTextSecondPart"
                )
            } else {
                robotSpeak("כל המשימות הושלמו למעבר משגרה לחירום - המרפאה מוכנה לפעילות בשעת חירום", true, language)
            }
        }

        getAllCheckBoxesByLanguage(language)
    }

    private fun getAllCheckBoxesByLanguage(language: String) {
        checkBoxListService.getAllCheckBoxesByLanguage(language).enqueue(object : Callback<List<CheckBoxModel>> {
                override fun onResponse(call: Call<List<CheckBoxModel>>, response: Response<List<CheckBoxModel>>) {
                    progressBarCheckList.visibility = View.GONE
                    val checkBoxModels: List<CheckBoxModel>? = response.body()
                    if (checkBoxModels != null) {
                        var checkBoxModelsSorted: List<CheckBoxModel>? = null
                        if (language == "iw") {
                             checkBoxModelsSorted = checkBoxModels.sortedByDescending {
                                it.isDone
                            }
                        } else {
                            checkBoxModelsSorted = checkBoxModels.sortedBy {
                                it.isDone
                            }

                        }
                        Log.i("getCheckLists", "received ${checkBoxModels.size} checkLists models")
                        val checkBoxLayouts: ArrayList<CheckBoxLayout> =
                            createCheckBoxes(checkBoxModelsSorted)
                        for (checkBoxLayout in checkBoxLayouts) {
                            checkBoxLayout.button.setOnClickListener {
                                val body: HashMap<String, Boolean> = HashMap()
                                if (checkBoxLayout.checkIsDone) {
                                    checkBoxLayout.button.background = AppCompatResources.getDrawable(this@CheckListActivity, R.drawable.unchecked_box_background)
                                    body["checkBoxStatusUpdate"] = false
                                    changeStatusRequest(checkBoxLayout, body)
                                } else {
                                    checkBoxLayout.button.background = AppCompatResources.getDrawable(this@CheckListActivity, R.drawable.checked_box_background)
                                    body["checkBoxStatusUpdate"] = true
                                    changeStatusRequest(checkBoxLayout, body)
                                }
                            }
                        }
                        checkBoxLayoutsUnchecked = checkBoxLayouts.filter { checkBoxLayout ->
                            !checkBoxLayout.checkIsDone
                        }
                        if (checkBoxLayoutsUnchecked.isEmpty()) {
                            robotSpeak("כל המשימות הושלמו למעבר משגרה לחירום - המרפאה מוכנה לפעילות בשעת חירום", true, language)
                        }
                        if (isCheckingStart) {
                            if (checkBoxLayoutsUnchecked.isNotEmpty()) {
                                val text = checkBoxLayoutsUnchecked[0].checkBoxText
                                robot.askQuestion(" האם בוצע - ${text}?")
                            } else {
                                robotSpeak("You checked all tasks", true, language)
                                isCheckingStart = false
                            }
                        }

                    }
                }

                override fun onFailure(call: Call<List<CheckBoxModel>>, t: Throwable) {
                    progressBarCheckList.visibility = View.VISIBLE
                    val errorMessage = t.message
                    Log.e("getCheckListsFailureByLanguage", "Error retrieving data from server: $errorMessage")
                }
            })
    }

    private fun changeStatusRequest(checkBoxLayout: CheckBoxLayout, body: HashMap<String, Boolean>) {
        checkBoxListService.updateCheckBoxStatus(checkBoxLayout.checkBoxId, body)
            .enqueue(object : Callback<CheckBoxModel> {
                override fun onResponse(call: Call<CheckBoxModel>, response: Response<CheckBoxModel>) {
                    val checkBoxModel: CheckBoxModel? = response.body()
                    if (checkBoxModel != null) {
                        Log.i("changedStatus", "changed status to ${checkBoxModel.done()} ")
                        checkBoxLayout.checkIsDone = checkBoxModel.done()
                        checkBoxContent.removeAllViews()
                        getAllCheckBoxesByLanguage(language)
                    }
                }
                override fun onFailure(call: Call<CheckBoxModel>, t: Throwable) {
                    val errorMessage = t.message
                    Log.e("changedStatusFailure", "Error retrieving data from server: $errorMessage")
                }
            })
    }

    private fun sendQuestion(body: HashMap<String, String>) {
        chatGPTService.askQuestion(body).enqueue(object: Callback<ChatGPTAnswerModel> {
            override fun onResponse(call: Call<ChatGPTAnswerModel>, response: Response<ChatGPTAnswerModel>) {
                progressBarCheckList.visibility = View.GONE
                checkBoxContent.visibility = View.VISIBLE
                val answerChatGPT: ChatGPTAnswerModel? = response.body()
                if (answerChatGPT != null && answerChatGPT.answer.isNotEmpty()) {
                    robot.askQuestion(answerChatGPT.answer + "." + "אני עדיין יכולה לעזור לך ?")
                }
            }

            override fun onFailure(call: Call<ChatGPTAnswerModel>, t: Throwable) {
                Toast.makeText(this@CheckListActivity, "Something wrong with server", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun createCheckBoxes(checkBoxModels: List<CheckBoxModel>): ArrayList<CheckBoxLayout> {
        val checkBoxesLayout = ArrayList<CheckBoxLayout>()
        for (checkListModel in checkBoxModels) {
            val checkBoxLayout = CheckBoxLayout(
                this, checkListModel.shortDescription, checkListModel.id, checkListModel.done(), checkListModel.tag)
            checkBoxContent.addView(checkBoxLayout)
            checkBoxesLayout.add(checkBoxLayout)
        }
        return checkBoxesLayout
    }


    private fun robotSpeak(text: String, showConversationLayer: Boolean, language: String) {
        when (language) {
            "iw" -> {
                robot.speak(TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer, TtsRequest.Language.HE_IL))
            }
            "ru" -> {
                robot.speak(TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer, TtsRequest.Language.RU_RU))
            }
            "en" -> {
                robot.speak(TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer, TtsRequest.Language.EN_US))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        robot.addOnRobotReadyListener(this)
        robot.addAsrListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnRobotReadyListener(this)
        robot.removeAsrListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            try {
                robot.requestToBeKioskApp()
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
            asrResult.equals("סדר פעולות", ignoreCase = true) -> {
                isCheckingStart = true
                val text = checkBoxLayoutsUnchecked[0].checkBoxText
                robot.askQuestion(" האם בוצע - ${text}?")
            }
            asrResult.equals("כן", ignoreCase = true) -> {
                val body: HashMap<String, Boolean> = HashMap()
                body["checkBoxStatusUpdate"] = true
                changeStatusRequest(checkBoxLayoutsUnchecked[0], body)
            }
            asrResult.equals("לא", ignoreCase = true) -> {
                robotSpeak("אנא השלם את המשימה וחזור אלי", true, language)
                isCheckingStart = false
            }
            asrResult.startsWith("תגידי לי", ignoreCase = true) -> {
                val question = asrResult.drop(8)
                if (question.isNotEmpty()) {
                    val body: HashMap<String, String> = HashMap()
                    body["question"] = question
                    robotSpeak("אני חושבת ...", true, language)
                    sendQuestion(body)
                    progressBarCheckList.visibility = View.VISIBLE
                    checkBoxContent.visibility = View.GONE
                }
            }
        }
    }
}


