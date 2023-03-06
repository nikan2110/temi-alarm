package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.nikita.doroshenko.japanmeeting.services.MailService

class MenuActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var buttonCheckList: Button
    private lateinit var buttonPatients: Button
    private lateinit var buttonBackMainPage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        mediaPlayer = MediaPlayer.create(this, R.raw.siren)

        buttonCheckList = findViewById(R.id.btn_check_list)
        buttonPatients = findViewById(R.id.btn_patients)
        buttonBackMainPage = findViewById(R.id.btn_back_to_main_page)

        buttonCheckList.setOnClickListener {
            val destinationActivity = CheckListActivity::class.java
            val menuActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }

        buttonPatients.setOnClickListener {
            val destinationActivity = PatientsActivity::class.java
            val menuActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }

        buttonBackMainPage.setOnClickListener {
            val destinationActivity = MainPageActivity::class.java
            val mainPageActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(mainPageActivityIntent)
        }

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
}
