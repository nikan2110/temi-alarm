package com.nikita.doroshenko.japanmeeting

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*


open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Load the selected language from shared preferences
        val prefs = newBase.getSharedPreferences("LanguagePreference", MODE_PRIVATE)
        var language = prefs.getString("language", "")
        // Set the default language to English if no language is set
        if (language!!.isEmpty()) {
            Log.i("BaseActivity", "language is empty")
            language = "en"
        }
        if (language.equals("he") || language.equals("iw")) {
            language = "he";
        }
        // Set the language for the application
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        newBase.resources.updateConfiguration(config, newBase.resources.displayMetrics)
        super.attachBaseContext(newBase)
        Log.i("BaseActivity", "set $language")
    }
}