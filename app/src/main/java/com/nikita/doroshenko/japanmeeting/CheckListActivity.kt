package com.nikita.doroshenko.japanmeeting

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.content.res.AppCompatResources
import com.nikita.doroshenko.japanmeeting.layouts.CheckBoxLayout
import com.nikita.doroshenko.japanmeeting.models.CheckBoxModel
import com.nikita.doroshenko.japanmeeting.utils.RetrofitClient
import com.nikita.doroshenko.japanmeeting.services.CheckBoxListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CheckListActivity : BaseActivity() {

    private lateinit var buttonBackMenu: Button
    private lateinit var checkBoxContent: LinearLayout
    private lateinit var progressBarCheckList: ProgressBar

    private var retrofit = RetrofitClient.getClient()
    private var checkBoxListService = retrofit.create(CheckBoxListService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val language = Locale.getDefault().language
        Log.i("CheckListActivity", "received language for checklist $language")

        checkBoxContent = findViewById(R.id.ll_check_box_content)

        progressBarCheckList = findViewById(R.id.pb_check_list)
        progressBarCheckList.visibility = View.VISIBLE

        buttonBackMenu = findViewById(R.id.btn_back_to_menu)
        buttonBackMenu.setOnClickListener {
            val destinationActivity = MenuActivity::class.java
            val menuActivityIntent = Intent(this@CheckListActivity, destinationActivity)
            startActivity(menuActivityIntent)
        }


        checkBoxListService.getAllCheckBoxesByLanguage(language).enqueue(object : Callback<List<CheckBoxModel>> {
            override fun onResponse(call: Call<List<CheckBoxModel>>, response: Response<List<CheckBoxModel>>) {
                progressBarCheckList.visibility = View.GONE
                val checkBoxModels: List<CheckBoxModel>? = response.body()
                if (checkBoxModels != null) {
                    Log.i("getCheckLists", "received ${checkBoxModels.size} checkLists models")
                    val checkBoxModelsSorted = checkBoxModels.sortedBy {
                        it.isDone
                    }
                    val checkBoxLayouts: ArrayList<CheckBoxLayout> = createCheckBoxes(checkBoxModelsSorted)
                    for (checkBoxLayout in checkBoxLayouts){
                        checkBoxLayout.button.setOnClickListener {
                            val body:HashMap<String, Boolean> = HashMap()
                            if (checkBoxLayout.checkIsDone ) {
                                checkBoxLayout.button.background = AppCompatResources.getDrawable(this@CheckListActivity,R.drawable.unchecked_box_background)
                                body["checkBoxStatusUpdate"] = false
                                changeStatusRequest(checkBoxLayout, body)
                            } else {
                                checkBoxLayout.button.background = AppCompatResources.getDrawable(this@CheckListActivity,R.drawable.checked_box_background)
                                body["checkBoxStatusUpdate"] = true
                                changeStatusRequest(checkBoxLayout, body)
                            }
                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<CheckBoxModel>>, t: Throwable) {
                progressBarCheckList.visibility = View.VISIBLE
                val errorMessage = t.message
                Log.e("getCheckListsFailureByLanguage", "Error retrieving data from server: $errorMessage")
            }
        })

    }

    private fun changeStatusRequest(checkBoxLayout: CheckBoxLayout, body: HashMap<String, Boolean>) {
        checkBoxListService.updateCheckBoxStatus(checkBoxLayout.checkBoxId, body).enqueue(object : Callback<CheckBoxModel> {
            override fun onResponse(call: Call<CheckBoxModel>, response: Response<CheckBoxModel>) {
                val checkBoxModel: CheckBoxModel? = response.body()
                if (checkBoxModel != null) {
                    Log.i("changedStatus", "changed status to ${checkBoxModel.done()} ")
                    checkBoxLayout.checkIsDone = checkBoxModel.done()
                }
            }

            override fun onFailure(call: Call<CheckBoxModel>, t: Throwable) {
                val errorMessage = t.message
                Log.e("changedStatusFailure", "Error retrieving data from server: $errorMessage")
            }

        })
    }

    private fun createCheckBoxes(checkBoxModels: List<CheckBoxModel>): ArrayList<CheckBoxLayout> {
        val checkBoxesLayout = ArrayList<CheckBoxLayout>()
        for (checkListModel in checkBoxModels) {
            val checkBoxLayout = CheckBoxLayout(this, checkListModel.text, checkListModel.id, checkListModel.done(), checkListModel.tag)
//            val layoutParams = RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//            )
//            layoutParams.marginEnd= 16.dpToPx()
            checkBoxContent.addView(checkBoxLayout)
            checkBoxesLayout.add(checkBoxLayout)
        }
        return checkBoxesLayout
    }

    private fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }

}


