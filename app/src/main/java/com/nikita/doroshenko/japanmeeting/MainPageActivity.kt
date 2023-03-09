package com.nikita.doroshenko.japanmeeting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.nikita.doroshenko.japanmeeting.utils.Constants
import java.util.*


//import com.robotemi.sdk.Robot
//import com.robotemi.sdk.TtsRequest
//import com.robotemi.sdk.listeners.OnRobotReadyListener

class MainPageActivity : BaseActivity() {

    private lateinit var buttonTemiInstructions: Button
    private lateinit var buttonForwardToMenu: Button
    private lateinit var buttonBackToLanguage: Button
//    private lateinit var robot: Robot

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainPageActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        instance = this

//        robot = Robot.getInstance()

        buttonForwardToMenu = findViewById(R.id.btn_forward_to_menu)
        buttonForwardToMenu.setOnClickListener {
            val destinationActivity = MenuActivity::class.java
            val menuActivityIntent = Intent(this@MainPageActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }

        buttonTemiInstructions = findViewById(R.id.btn_instructions)
        buttonTemiInstructions.setOnClickListener {
            Toast.makeText(this, Constants.TEMI_SPEECH, Toast.LENGTH_SHORT).show()
//            robotSpeak(Constants.TEMI_SPEECH, true)
        }

        buttonBackToLanguage = findViewById(R.id.btn_back_to_language)
        buttonBackToLanguage.setOnClickListener {
            val destinationActivity = LanguageActivity::class.java
            val languageActivityIntent = Intent(this@MainPageActivity, destinationActivity)
            startActivity(languageActivityIntent)
        }

    }



//    override fun onStart() {
//        super.onStart()
//        robot.addOnRobotReadyListener(this)
//    }

//    override fun onStop() {
//        super.onStop()
//        robot.removeOnRobotReadyListener(this)
//    }

//    override fun onRobotReady(isReady: Boolean) {
//        if (isReady) {
//            try {
//                robot.requestToBeKioskApp()
//                Log.i("SelectedKiosk", "Is selected kiosk: ${robot.isSelectedKioskApp()}")
//                robot.tiltBy(55, 0.5f)
//            } catch (e: PackageManager.NameNotFoundException) {
//                throw RuntimeException(e)
//            }
//        }
//    }

//    fun robotSpeak(text: String, showConversationLayer: Boolean) {
//        robot.speak(
//            TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
//            TtsRequest.Language.EN_US))
//    }

//     fun call(name: String, userId: String) {
//        Log.i("CallHelpCalling", "Received name: $name, Received user id: $userId")
//        robot.finishConversation()
//        robotSpeak("Calling help", true)
//        robot.startTelepresence(name, userId)
//    }

}