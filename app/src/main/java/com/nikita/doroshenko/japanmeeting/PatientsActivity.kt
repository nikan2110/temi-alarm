package com.nikita.doroshenko.japanmeeting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nikita.doroshenko.japanmeeting.layouts.PatientLayout
import com.nikita.doroshenko.japanmeeting.models.PatientModel
import com.nikita.doroshenko.japanmeeting.services.PatientListService
import com.nikita.doroshenko.japanmeeting.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientsActivity : BaseActivity(){


    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    private var picturePath: String? = null

    private lateinit var patientListContent: LinearLayout

    private lateinit var buttonBackToMenu: Button
    private lateinit var buttonForwardToFinish: Button

    private var retrofit = RetrofitClient.getClient()
    private var patientListService = retrofit.create(PatientListService::class.java)

    private val storageResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted for read storage", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission denied for read storage", Toast.LENGTH_LONG).show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patients)

        patientListContent = findViewById(R.id.ll_patient_content)

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


        patientListService.allPatients.enqueue(object: Callback<List<PatientModel>> {
            override fun onResponse(call: Call<List<PatientModel>>, response: Response<List<PatientModel>>) {
                val patientModels: List<PatientModel>? = response.body()
                if (patientModels != null) {
                    println(patientModels)
                    Log.i("getPatients", "received ${patientModels.size} patient models")
                    val patientsLayout: ArrayList<PatientLayout> = createPatients(patientModels)
                }
            }

            override fun onFailure(call: Call<List<PatientModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun createPatients(patientModels: List<PatientModel>): ArrayList<PatientLayout> {
        val patientsLayout = ArrayList<PatientLayout>()
        for (patientModel in patientModels) {
            val patientLayout = PatientLayout(this, patientModel.id, patientModel.isChecked, patientModel.patientStatus, patientModel.patientName,
            patientModel.patientPhone, patientModel.patientAge, patientModel.patientType
            )
            patientListContent.addView(patientLayout)
            patientsLayout.add(patientLayout)
        }





        return patientsLayout
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
