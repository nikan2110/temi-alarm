package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel
import com.nikita.doroshenko.japanmeeting.models.PatientModel
import com.nikita.doroshenko.japanmeeting.services.CheckBoxListService
import com.nikita.doroshenko.japanmeeting.services.MailService
import com.nikita.doroshenko.japanmeeting.services.PatientListService
import com.nikita.doroshenko.japanmeeting.services.RetrofitClientTemiServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MenuActivity : BaseActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var buttonCheckList: Button
    private lateinit var buttonPatients: Button
    private lateinit var buttonBackMainPage: Button

    private lateinit var progressBarMenu: ProgressBar

    private var retrofit = RetrofitClientTemiServer.getClient()
    private var checkBoxListService = retrofit.create(CheckBoxListService::class.java)
    private var patientListService = retrofit.create(PatientListService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val language = Locale.getDefault().language

        mediaPlayer = MediaPlayer.create(this, R.raw.siren)

        progressBarMenu = findViewById(R.id.pb_main_menu)

        buttonCheckList = findViewById(R.id.btn_check_list)
        buttonPatients = findViewById(R.id.btn_patients)

        buttonBackMainPage = findViewById(R.id.btn_back_to_main_page)

        buttonCheckList.setOnClickListener {
            val destinationActivity = CheckListActivity::class.java
            val checkListActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(checkListActivityIntent)
        }

        buttonPatients.setOnClickListener {
            val destinationActivity = PatientsActivity::class.java
            val patientsActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(patientsActivityIntent)
        }

        buttonBackMainPage.setOnClickListener {
            val destinationActivity = MainPageActivity::class.java
            val mainPageActivityIntent = Intent(this@MenuActivity, destinationActivity)
            startActivity(mainPageActivityIntent)
        }

        checkBoxListService.getAllCheckBoxesByLanguageAndStatus(language, false).enqueue(object: Callback<List<CheckBoxModel>>{
            override fun onResponse(call: Call<List<CheckBoxModel>>, response: Response<List<CheckBoxModel>>) {
                buttonCheckList.visibility = View.VISIBLE
                progressBarMenu.visibility = View.GONE
                val checkBoxModels: List<CheckBoxModel>? = response.body()
                if (checkBoxModels != null) {
                    if (checkBoxModels.isEmpty()) {
                        buttonCheckList.background = AppCompatResources.getDrawable(this@MenuActivity,R.drawable.check_list_button_green)
                    }
                }
            }

            override fun onFailure(call: Call<List<CheckBoxModel>>, t: Throwable) {
                val errorMessage = t.message
                Log.e("getCheckListsFailureByLanguageAndStatus", "Error retrieving data from server: $errorMessage")
            }

        })

        patientListService.getAllPatientsByStatus(false).enqueue(object: Callback<List<PatientModel>>{
            override fun onResponse(call: Call<List<PatientModel>>, response: Response<List<PatientModel>>) {
                buttonPatients.visibility = View.VISIBLE
                progressBarMenu.visibility = View.GONE
                val patientModels: List<PatientModel>? = response.body()
                if (patientModels != null) {
                    if (patientModels.isEmpty()) {
                        buttonPatients.background = AppCompatResources.getDrawable(this@MenuActivity,R.drawable.patients_button_green)
                    }
                }
            }

            override fun onFailure(call: Call<List<PatientModel>>, t: Throwable) {
                val errorMessage = t.message
                Log.e("getPatientsFailureByStatus", "Error retrieving data from server: $errorMessage")
            }

        })


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
