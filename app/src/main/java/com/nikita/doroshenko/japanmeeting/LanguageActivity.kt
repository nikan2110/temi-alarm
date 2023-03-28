package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.system.exitProcess


class LanguageActivity : BaseActivity() {

    private lateinit var buttonRussian: ImageView
    private lateinit var buttonHebrew: ImageView
    private lateinit var buttonEnglish: ImageView
    private lateinit var buttonHide: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        buttonRussian = findViewById(R.id.btn_russian)
        buttonRussian.setOnClickListener {
            setLocale(Locale("ru"))
            openMainPage()
        }

        buttonHebrew = findViewById(R.id.btn_hebrew)
        buttonHebrew.setOnClickListener {
            setLocale(Locale("he"))
            openMainPage()
        }

        buttonEnglish = findViewById(R.id.btn_english)
        buttonEnglish.setOnClickListener {
            openMainPage()
        }

        buttonHide = findViewById(R.id.btn_hide)
        buttonHide.setOnLongClickListener {
            finishAffinity()
            System.exit(0);
            true
        }



    }

    private fun openMainPage() {
        val destinationActivity = MainPageActivity::class.java
        val mainPageActivityIntent = Intent(this@LanguageActivity, destinationActivity)
        finish()
        startActivity(mainPageActivityIntent)
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




}