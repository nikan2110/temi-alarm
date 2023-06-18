package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import com.nikita.doroshenko.japanmeeting.layouts.CheckBoxLayout
import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel
import com.nikita.doroshenko.japanmeeting.services.CheckBoxListService
import com.nikita.doroshenko.japanmeeting.utils.RetrofitClient
import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.constants.SdkConstants
import com.robotemi.sdk.listeners.OnRobotReadyListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CheckListActivity : BaseActivity(), OnRobotReadyListener, Robot.AsrListener {

    private lateinit var buttonBackMenu: Button
    private lateinit var buttonTemiInteraction: Button
    private lateinit var checkBoxContent: LinearLayout
    private lateinit var progressBarCheckList: ProgressBar

    private lateinit var robot: Robot

    private lateinit var language: String

    private var isCheckingStart = false

    private var checkBoxLayoutsUnchecked: List<CheckBoxLayout> = ArrayList<CheckBoxLayout>()

    private var retrofit = RetrofitClient.getClient()
    private var checkBoxListService = retrofit.create(CheckBoxListService::class.java)

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
                robot.askQuestion(
                    "You have ${checkBoxLayoutsUnchecked.size} not solve tasks. " +
                            "If you wanna check it with Temi, say 'START CHECK'"
                )
            } else {
                robotSpeak("You checked all tasks", true, language)
            }
        }

        getAllCheckBoxesByLanguage(language)
    }

    private fun getAllCheckBoxesByLanguage(language: String) {
        checkBoxListService.getAllCheckBoxesByLanguage(language)
            .enqueue(object : Callback<List<CheckBoxModel>> {
                override fun onResponse(
                    call: Call<List<CheckBoxModel>>,
                    response: Response<List<CheckBoxModel>>
                ) {
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
                                    checkBoxLayout.button.background =
                                        AppCompatResources.getDrawable(
                                            this@CheckListActivity,
                                            R.drawable.unchecked_box_background
                                        )
                                    body["checkBoxStatusUpdate"] = false
                                    changeStatusRequest(checkBoxLayout, body)
                                } else {
                                    checkBoxLayout.button.background =
                                        AppCompatResources.getDrawable(
                                            this@CheckListActivity,
                                            R.drawable.checked_box_background
                                        )
                                    body["checkBoxStatusUpdate"] = true
                                    changeStatusRequest(checkBoxLayout, body)
                                }
                            }
                        }
                        checkBoxLayoutsUnchecked = checkBoxLayouts.filter { checkBoxLayout ->
                            !checkBoxLayout.checkIsDone
                        }
                        if (isCheckingStart) {
                            if (checkBoxLayoutsUnchecked.isNotEmpty()) {
                                val text = checkBoxLayoutsUnchecked[0].checkBoxText
                                robot.askQuestion(" אתֿה עשה ${text}?")
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
                    Log.e(
                        "getCheckListsFailureByLanguage",
                        "Error retrieving data from server: $errorMessage"
                    )
                }
            })
    }

    private fun changeStatusRequest(
        checkBoxLayout: CheckBoxLayout,
        body: HashMap<String, Boolean>
    ) {
        checkBoxListService.updateCheckBoxStatus(checkBoxLayout.checkBoxId, body)
            .enqueue(object : Callback<CheckBoxModel> {
                override fun onResponse(
                    call: Call<CheckBoxModel>,
                    response: Response<CheckBoxModel>
                ) {
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
                    Log.e(
                        "changedStatusFailure",
                        "Error retrieving data from server: $errorMessage"
                    )
                }

            })
    }

    private fun createCheckBoxes(checkBoxModels: List<CheckBoxModel>): ArrayList<CheckBoxLayout> {
        val checkBoxesLayout = ArrayList<CheckBoxLayout>()
        for (checkListModel in checkBoxModels) {
            val checkBoxLayout = CheckBoxLayout(
                this,
                checkListModel.shortDescription,
                checkListModel.id,
                checkListModel.done(),
                checkListModel.tag
            )
//            val layoutParams = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.marginEnd= 16.dpToPx()
            checkBoxContent.addView(checkBoxLayout)
            checkBoxesLayout.add(checkBoxLayout)
        }
        return checkBoxesLayout
    }

    private fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

    private fun robotSpeak(text: String, showConversationLayer: Boolean, language: String) {
        when (language) {
            "iw" -> {
                robot.speak(
                    TtsRequest.create(
                        text, isShowOnConversationLayer = showConversationLayer,
                        TtsRequest.Language.HE_IL
                    )
                )
            }
            "ru" -> {
                robot.speak(
                    TtsRequest.create(
                        text, isShowOnConversationLayer = showConversationLayer,
                        TtsRequest.Language.RU_RU
                    )
                )
            }
            "en" -> {
                robot.speak(
                    TtsRequest.create(
                        text, isShowOnConversationLayer = showConversationLayer,
                        TtsRequest.Language.EN_US
                    )
                )
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
            val metadata = packageManager
                .getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData
                ?: return
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
            asrResult.equals("שלום", ignoreCase = true) -> {
                isCheckingStart = true
                val text = checkBoxLayoutsUnchecked[0].checkBoxText
                robot.askQuestion(" אתֿה עשה ${text}?")
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
        }
    }

}


