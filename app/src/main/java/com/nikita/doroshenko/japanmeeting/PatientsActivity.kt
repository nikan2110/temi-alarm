package com.nikita.doroshenko.japanmeeting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.nikita.doroshenko.japanmeeting.layouts.PatientLayout
import com.nikita.doroshenko.japanmeeting.models.PatientModel
import com.nikita.doroshenko.japanmeeting.services.PatientListService
import com.nikita.doroshenko.japanmeeting.services.RetrofitClientTemiServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientsActivity : BaseActivity(){


    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    private var picturePath: String? = null

    private lateinit var patientListContent: LinearLayout

    private lateinit var progressBarPatientsList: ProgressBar

    private lateinit var buttonBackToMenu: Button

    private var retrofit = RetrofitClientTemiServer.getClient()
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

        progressBarPatientsList = findViewById(R.id.pb_patients_list)
        progressBarPatientsList.visibility = View.VISIBLE

        buttonBackToMenu = findViewById(R.id.btn_back_to_menu)
        buttonBackToMenu.setOnClickListener {
            val destinationActivity = MenuActivity::class.java
            val menuActivityIntent = Intent(this@PatientsActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }


        patientListService.allPatients.enqueue(object: Callback<List<PatientModel>> {
            override fun onResponse(call: Call<List<PatientModel>>, response: Response<List<PatientModel>>) {
                progressBarPatientsList.visibility = View.GONE
                val patientModels: List<PatientModel>? = response.body()
                if (patientModels != null) {
                    var patientModelsSorted = patientModels.sortedByDescending {
                        it.patientStatus
                    }
                    patientModelsSorted = patientModelsSorted.sortedBy {
                        it.isChecked
                    }
                    Log.i("getPatients", "received ${patientModels.size} patient models")
                    val patientsLayout: ArrayList<PatientLayout> = createPatients(patientModelsSorted)
                    for (patientLayout in patientsLayout) {
                        patientLayout.buttonPatientIsChecked.setOnClickListener {
                            val body:HashMap<String, Boolean> = HashMap()
                            if (patientLayout.isChecked ) {
                                patientLayout.buttonPatientIsChecked.background =
                                    AppCompatResources.getDrawable(this@PatientsActivity,R.drawable.unchecked_patient_background)
                                body["patientStatusUpdate"] = false
                                changeStatusRequest(patientLayout, body)
                            } else {
                                patientLayout.buttonPatientIsChecked.background =
                                    AppCompatResources.getDrawable(this@PatientsActivity,R.drawable.checked_patient_background)
                                body["patientStatusUpdate"] = true
                                changeStatusRequest(patientLayout, body)
                            }
                        }
                        patientLayout.buttonPhone.setOnClickListener {
                            Toast.makeText(this@PatientsActivity, "Temi is calling", Toast.LENGTH_SHORT).show()
                        }
                        patientLayout.buttonDetails.setOnClickListener {
                            when(patientLayout.patientType) {
                                "Pregnant" -> {
                                    runLikeActivity("/storage/emulated/0/Download/PregnantDetails.png")
                                }
                                "Diabetic" -> {
                                    runLikeActivity("/storage/emulated/0/Download/DiabeticDetails.png")
                                }

                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<PatientModel>>, t: Throwable) {
                progressBarPatientsList.visibility = View.VISIBLE
                val errorMessage = t.message
                Log.e("getCheckListsFailure", "Error retrieving data from server: $errorMessage")
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

    private fun changeStatusRequest(patientLayout: PatientLayout, body: HashMap<String, Boolean>) {
        patientListService.updatePatientStatus(patientLayout.patientId, body).enqueue(object : Callback<PatientModel> {
            override fun onResponse(call: Call<PatientModel>, response: Response<PatientModel>) {
                val patientModel: PatientModel? = response.body()
                if (patientModel != null) {
                    Log.i("changedStatus", "changed status to ${patientModel.isChecked} ")
                    patientLayout.isChecked = patientModel.isChecked
                }
            }

            override fun onFailure(call: Call<PatientModel>, t: Throwable) {
                val errorMessage = t.message
                Log.e("changedStatusFailure", "Error retrieving data from server: $errorMessage")
            }

        })
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
