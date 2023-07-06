package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnGreetModeStateChangedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import java.util.*


class LanguageActivity : BaseActivity(), OnRobotReadyListener, OnGreetModeStateChangedListener {

    private lateinit var buttonRussian: ImageView
    private lateinit var buttonHebrew: ImageView
    private lateinit var buttonEnglish: ImageView
    private lateinit var buttonHide: Button
    private lateinit var robot: Robot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        robot = Robot.getInstance()

        buttonRussian = findViewById(R.id.btn_russian)
        buttonRussian.setOnClickListener {
            setLocale(Locale("ru"))
            openMenuPage()
        }

        buttonHebrew = findViewById(R.id.btn_hebrew)
        buttonHebrew.setOnClickListener {
            setLocale(Locale("he"))
            openMenuPage()
        }

        buttonEnglish = findViewById(R.id.btn_english)
        buttonEnglish.setOnClickListener {
            setLocale(Locale("en"))
            openMenuPage()
        }

        buttonHide = findViewById(R.id.btn_hide)
        buttonHide.setOnLongClickListener {
            finishAffinity()
            System.exit(0);
            true
        }

    }

    private fun openMenuPage() {
        val destinationActivity = MenuActivity::class.java
        val menuPageActivityIntent = Intent(this@LanguageActivity, destinationActivity)
        finish()
        startActivity(menuPageActivityIntent)
    }

    private fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(
            config,
            baseContext.resources.displayMetrics
        )
        // Save the selected language to shared preferences
        val editor = getSharedPreferences("LanguagePreference", MODE_PRIVATE).edit()
        editor.putString("language", locale.language)
        editor.apply()
        // Restart the activity to reflect the language changes
        finishAffinity()
        val destinationActivity = MenuActivity::class.java
        val menuActivityIntent = Intent(this@LanguageActivity, destinationActivity)
        startActivity(menuActivityIntent)
    }

    override fun onGreetModeStateChanged(state: Int) {
        Log.i("onGreetModeStateChangedLanguageActivity", "Received state $state in language activity")
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

    override fun onStart() {
        super.onStart()
        robot.addOnRobotReadyListener(this)
        robot.addOnGreetModeStateChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        robot.removeOnRobotReadyListener(this)
        robot.removeOnGreetModeStateChangedListener(this)
    }

}