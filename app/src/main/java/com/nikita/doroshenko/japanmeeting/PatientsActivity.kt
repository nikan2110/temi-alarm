package com.nikita.doroshenko.japanmeeting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nikita.doroshenko.japanmeeting.utils.Constants

class PatientsActivity : BaseActivity(){


    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    private var picturePath: String? = null
    private var isCheckedPregnant: Boolean = false
    private var isCheckedDiabetic: Boolean = false
    private var isCheckedAsthma: Boolean = false
    private var isCheckedSenior: Boolean = false

    private val storageResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted for read storage", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission denied for read storage", Toast.LENGTH_LONG).show()
            }
        }

    private lateinit var buttonPregnantIsChecked: Button
    private lateinit var buttonPregnantPhone: Button
    private lateinit var buttonPregnantDetails: Button

    private lateinit var buttonDiabeticIsChecked: Button
    private lateinit var buttonDiabeticPhone: Button
    private lateinit var buttonDiabeticDetails: Button

    private lateinit var buttonAsthmaIsChecked: Button
    private lateinit var buttonAsthmaPhone: Button

    private lateinit var buttonSeniorIsChecked: Button
    private lateinit var buttonSeniorPhone: Button

    private lateinit var buttonBackToMenu: Button
    private lateinit var buttonForwardToFinish: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients)

        buttonPregnantIsChecked = findViewById(R.id.btn_pregnant_is_checked)
        buttonPregnantPhone = findViewById(R.id.btn_pregnant_phone)
        buttonPregnantDetails = findViewById(R.id.btn_pregnant_details)

        buttonDiabeticIsChecked = findViewById(R.id.btn_diabetic_is_checked)
        buttonDiabeticPhone = findViewById(R.id.btn_diabetic_phone)
        buttonDiabeticDetails = findViewById(R.id.btn_diabetic_details)

        buttonAsthmaIsChecked = findViewById(R.id.btn_asthma_is_checked)
        buttonAsthmaPhone = findViewById(R.id.btn_asthma_phone)

        buttonSeniorIsChecked = findViewById(R.id.btn_senior_is_checked)
        buttonSeniorPhone = findViewById(R.id.btn_senior_phone)

        pregnantButtonListeners()
        diabeticButtonListeners()
        asthmaButtonListeners()
        seniorButtonListeners()

        buttonBackToMenu = findViewById(R.id.btn_back_to_menu)
        buttonBackToMenu.setOnClickListener {
            val destinationActivity = MenuActivity::class.java
            val menuActivityIntent = Intent(this@PatientsActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }

        buttonForwardToFinish = findViewById(R.id.btn_forward_to_finish)
        buttonForwardToFinish.setOnClickListener {
            val destinationActivity = FinishActivity::class.java
            val finishActivityIntent = Intent(this@PatientsActivity, destinationActivity)
            startActivity(finishActivityIntent)
        }

    }

    private fun seniorButtonListeners() {
        buttonSeniorIsChecked.setOnClickListener {
            if (isCheckedSenior) {
                buttonSeniorIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.unchecked_patient_background)
                isCheckedSenior = false
            } else {
                buttonSeniorIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.checked_patient_background)
                isCheckedSenior = true
            }
        }

        buttonSeniorPhone.setOnClickListener {
//            MainPageActivity.instance.call(Constants.userName, Constants.userId)
            Toast.makeText(this, "Temi is calling", Toast.LENGTH_SHORT).show()
        }
    }

    private fun asthmaButtonListeners() {
        buttonAsthmaIsChecked.setOnClickListener {
            if (isCheckedAsthma) {
                buttonAsthmaIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.unchecked_patient_background)
                isCheckedAsthma = false
            } else {
                buttonAsthmaIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.checked_patient_background)
                isCheckedAsthma = true
            }
        }

        buttonAsthmaPhone.setOnClickListener {
//            MainPageActivity.instance.call(Constants.userName, Constants.userId)
            Toast.makeText(this, "Temi is calling", Toast.LENGTH_SHORT).show()
        }
    }

    private fun diabeticButtonListeners() {
        buttonDiabeticIsChecked.setOnClickListener {
            if (isCheckedDiabetic) {
                buttonDiabeticIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.unchecked_patient_background)
                isCheckedDiabetic = false
            } else {
                buttonDiabeticIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.checked_patient_background)
                isCheckedDiabetic = true
            }
        }

        buttonDiabeticPhone.setOnClickListener {
//            MainPageActivity.instance.call(Constants.userName, Constants.userId)
            Toast.makeText(this, "Temi is calling", Toast.LENGTH_SHORT).show()
        }

        buttonDiabeticDetails.setOnClickListener {
            runLikeActivity("/storage/emulated/0/Download/DiabeticDetails.png")
        }
    }

    private fun pregnantButtonListeners() {
        buttonPregnantIsChecked.setOnClickListener {
            if (isCheckedPregnant) {
                buttonPregnantIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.unchecked_patient_background)
                isCheckedPregnant = false
            } else {
                buttonPregnantIsChecked.background = ActivityCompat.getDrawable(this, R.drawable.checked_patient_background)
                isCheckedPregnant = true
            }
        }

        buttonPregnantPhone.setOnClickListener {
//            MainPageActivity.instance.call(Constants.userName, Constants.userId)
            Toast.makeText(this, "Temi is calling", Toast.LENGTH_SHORT).show()
        }

        buttonPregnantDetails.setOnClickListener {
            runLikeActivity("/storage/emulated/0/Download/PregnantDetails.png")
        }
    }

    private fun startCustomerDetailsActivity() {
        val destinationActivity = CustomerDetailsActivity::class.java
        val customerDetailsActivityIntent = Intent(this@PatientsActivity, destinationActivity)
        customerDetailsActivityIntent.putExtra("picturePath", picturePath)
        startActivity(customerDetailsActivityIntent)
    }

    private fun runLikeActivity(picturePath: String) {
        this.picturePath = picturePath
        val permissionCheck = ContextCompat.checkSelfPermission(this@PatientsActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startCustomerDetailsActivity()
        } else {
            ActivityCompat.requestPermissions(this@PatientsActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCustomerDetailsActivity()
            } else {
                Toast.makeText(this@PatientsActivity, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
