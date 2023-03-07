package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FinishActivity : BaseActivity() {

    private lateinit var buttonBackToMainPage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finish)

        buttonBackToMainPage = findViewById(R.id.btn_back_to_main_page_finish)
        buttonBackToMainPage.setOnClickListener {
            val destinationActivity = MainPageActivity::class.java
            val mainPageActivityIntent = Intent(this@FinishActivity, destinationActivity)
            startActivity(mainPageActivityIntent)
        }
    }
}