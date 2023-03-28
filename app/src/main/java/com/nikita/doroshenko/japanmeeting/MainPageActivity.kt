package com.nikita.doroshenko.japanmeeting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.nikita.doroshenko.japanmeeting.utils.Constants
import java.util.*


import com.robotemi.sdk.Robot
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.listeners.OnGreetModeStateChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener

class MainPageActivity : BaseActivity(), OnRobotReadyListener, OnGreetModeStateChangedListener {

    private lateinit var buttonTemiInstructions: Button
    private lateinit var buttonForwardToMenu: Button
    private lateinit var buttonBackToLanguage: Button
    private lateinit var robot: Robot

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: MainPageActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        val language = Locale.getDefault().language
        Log.i("MainPageActivity", "received language for temi speech $language")

        instance = this

        robot = Robot.getInstance()

        buttonForwardToMenu = findViewById(R.id.btn_forward_to_menu)
        buttonForwardToMenu.setOnClickListener {
            val destinationActivity = MenuActivity::class.java
            val menuActivityIntent = Intent(this@MainPageActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }

        buttonTemiInstructions = findViewById(R.id.btn_instructions)
        buttonTemiInstructions.setOnClickListener {
            val instructionText = resources.getString(R.string.temi_instructions)
//            Toast.makeText(this, instructionText, Toast.LENGTH_SHORT).show()
            robotSpeak(instructionText, true, language)
        }

        buttonBackToLanguage = findViewById(R.id.btn_back_to_language)
        buttonBackToLanguage.setOnClickListener {
            val destinationActivity = LanguageActivity::class.java
            val languageActivityIntent = Intent(this@MainPageActivity, destinationActivity)
            removeLanguageFromSharedPreferences()
            startActivity(languageActivityIntent)
        }

    }


    override fun onStart() {
        super.onStart()
        robot.addOnRobotReadyListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnRobotReadyListener(this)
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            try {
                // interaction
            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    fun robotSpeak(text: String, showConversationLayer: Boolean, language: String) {
        when(language) {
            "iw" -> {
                robot.speak(TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
                    TtsRequest.Language.HE_IL))
            }
            "ru" -> {
                robot.speak(TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
                    TtsRequest.Language.RU_RU))
            }
            "en" -> {
                robot.speak(TtsRequest.create(text, isShowOnConversationLayer = showConversationLayer,
                    TtsRequest.Language.EN_US))
            }
        }
    }

     fun call(name: String, userId: String) {
        Log.i("CallHelpCalling", "Received name: $name, Received user id: $userId")
        robot.startTelepresence(name, userId)
    }

    override fun onGreetModeStateChanged(state: Int) {
        Log.i("onGreetModeStateChanged", "Received state $state")
        if (state == 3) {
            robotSpeak("Hello, how can I help you?", false, "en")
        }
    }

    private fun removeLanguageFromSharedPreferences() {
        val prefs = getSharedPreferences("LanguagePreference", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("language")
        editor.apply()
    }

}