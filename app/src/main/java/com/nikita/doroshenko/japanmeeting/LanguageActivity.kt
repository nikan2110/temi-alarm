package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class LanguageActivity : AppCompatActivity() {

    private lateinit var buttonRussian: Button
    private lateinit var buttonHebrew: Button
    private lateinit var buttonForwardToMainPage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        buttonRussian = findViewById(R.id.btn_russian)
        buttonRussian.setOnClickListener {
            setLocale(Locale("ru"));
        }

        buttonHebrew = findViewById(R.id.btn_hebrew)
        buttonHebrew.setOnClickListener {
            setLocale(Locale("he"))
        }

        buttonForwardToMainPage = findViewById(R.id.btn_forward_to_main_page)
        buttonForwardToMainPage.setOnClickListener {
            val destinationActivity = MainPageActivity::class.java
            val mainPageActivityIntent = Intent(this@LanguageActivity, destinationActivity)
            startActivity(mainPageActivityIntent)
        }

        removeLanguageFromSharedPreferences()

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
        val destinationActivity = MainPageActivity::class.java
        val mainPageActivityIntent = Intent(this@LanguageActivity, destinationActivity)
        startActivity(mainPageActivityIntent)
    }

    private fun removeLanguageFromSharedPreferences() {
        val prefs = getSharedPreferences("LanguagePreference", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("language")
        editor.apply()
    }


}