package com.nikita.doroshenko.japanmeeting.layouts

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.nikita.doroshenko.japanmeeting.R

class PatientLayout(context: Context,
                    var patientId: String, isChecked: Boolean,
                    var patientStatus: Int, var patientName: String,
                    var patientPhoneNumber: String, var patientAge: String, var patientType: String
) : RelativeLayout(context) {

    var isChecked = isChecked


    val buttonPatientIsChecked = Button(context)
    val linearLayoutPatientCard = LinearLayout(context)
    val viewHorizontalLineOne = ViewHorizontalLine(context, 22, 102)
    val textViewPatientName = TextView(context)
    val linearLayoutPhoneNumber = LinearLayout(context)
    val linearLayoutPhoneNumberText = LinearLayout(context)
    val textViewPatientNumber = TextView(context)
    val buttonPhone = Button(context)
    val viewHorizontalLineTwo = ViewHorizontalLine(context, 22, 4)
    val textViewPatientAge = TextView(context)
    val linearLayoutPatientDetails = LinearLayout(context)
    val linearLayoutPatientDetailsText = LinearLayout(context)
    val textViewPatientType = TextView(context)
    val buttonDetails = Button(context)
    val viewHorizontalLineThree = ViewHorizontalLine(context, 22, 5)
    val imageViewPatientPicture = ImageView(context)

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        // Button patient is checked params
        buttonPatientIsChecked.layoutParams = LayoutParams(128.dpToPx(), 128.dpToPx()).apply {
            marginStart = 84.dpToPx()
        }
        buttonPatientIsChecked.foreground = context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground)).getDrawable(0)
        if (isChecked) {
            buttonPatientIsChecked.background = AppCompatResources.getDrawable(context, R.drawable.checked_patient_background)
        } else {
            buttonPatientIsChecked.background = AppCompatResources.getDrawable(context, R.drawable.unchecked_patient_background)
        }
        addView(buttonPatientIsChecked)
        // =======================================================================================

        // Linear layout patient card params
        linearLayoutPatientCard.layoutParams = LayoutParams(439.dpToPx(), 600.dpToPx()).apply {
            setMargins(0.dpToPx(), 45.dpToPx(), 0, 0)
            marginStart = 60.dpToPx()
        }
        linearLayoutPatientCard.orientation = LinearLayout.VERTICAL
        when(patientStatus) {
            1 -> {
                linearLayoutPatientCard.background = ContextCompat.getDrawable(context, R.drawable.layout_bg_purple)
            }
            2 -> {
                linearLayoutPatientCard.background = ContextCompat.getDrawable(context, R.drawable.layout_bg_orange)
            }
            3 -> {
                linearLayoutPatientCard.background = ContextCompat.getDrawable(context, R.drawable.layout_bg_red)
            }
        }
        addView(linearLayoutPatientCard)
        // =======================================================================================

        // Horizontal line one
        linearLayoutPatientCard.addView(viewHorizontalLineOne)
        // =======================================================================================

        // Text view patient name params
        textViewPatientName.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            marginStart = 24.dpToPx()
        }
        textViewPatientName.typeface = ResourcesCompat.getFont(context, R.font.assistant_semibold)
        textViewPatientName.text = patientName
        textViewPatientName.setTextColor(Color.parseColor("#ffffff"))
        textViewPatientName.textSize = 46f
        textViewPatientName.setLineSpacing(-10f, 1f)
        linearLayoutPatientCard.addView(textViewPatientName)
        // =======================================================================================

        //  Linear layout phone number params
        linearLayoutPhoneNumber.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        linearLayoutPhoneNumber.orientation = LinearLayout.HORIZONTAL
        linearLayoutPatientCard.addView(linearLayoutPhoneNumber)
        // =======================================================================================

        // Linear layout for number
        linearLayoutPhoneNumberText.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        linearLayoutPhoneNumber.addView(linearLayoutPhoneNumberText)
        // =======================================================================================

        // Text view patient number params
        textViewPatientNumber.layoutParams = LayoutParams(325.dpToPx(), 54.dpToPx()).apply {
            marginStart = 24.dpToPx()
        }
        textViewPatientNumber.typeface = ResourcesCompat.getFont(context, R.font.assistant_semibold)
        textViewPatientNumber.text = patientPhoneNumber
        textViewPatientNumber.setTextColor(Color.parseColor("#ffffff"))
        textViewPatientNumber.textSize = 46f
        textViewPatientNumber.setLineSpacing(-10f, 1f)
        linearLayoutPhoneNumberText.addView(textViewPatientNumber)
        // =======================================================================================

        // Button phone params
        buttonPhone.layoutParams = LayoutParams(54.dpToPx(), 54.dpToPx())
        buttonPhone.gravity = Gravity.END
        buttonPhone.background = ContextCompat.getDrawable(context, R.drawable.patient_phone_button_background)
        linearLayoutPhoneNumber.addView(buttonPhone)
        // =======================================================================================

        // Horizontal line two
        linearLayoutPatientCard.addView(viewHorizontalLineTwo)
        // =======================================================================================

        // Text view patient age params
        textViewPatientAge.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(0, 9.dpToPx(), 0, 0)
            marginStart = 24.dpToPx()
        }
        textViewPatientAge.typeface = ResourcesCompat.getFont(context, R.font.assistant_semibold)
        textViewPatientAge.text = patientAge
        textViewPatientAge.setTextColor(Color.parseColor("#ffffff"))
        textViewPatientAge.textSize = 32f
        linearLayoutPatientCard.addView(textViewPatientAge)
        // =======================================================================================

        //  Linear layout patient details params
        linearLayoutPatientDetails.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        linearLayoutPatientDetails.orientation = LinearLayout.HORIZONTAL
        linearLayoutPatientCard.addView(linearLayoutPatientDetails)
        // =======================================================================================

        // Linear layout for type
        linearLayoutPatientDetailsText.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        linearLayoutPatientDetails.addView(linearLayoutPatientDetailsText)
        // =======================================================================================

        //  Text view patient type params
        textViewPatientType.layoutParams = LayoutParams(325.dpToPx(), 60.dpToPx()).apply {
            marginStart = 24.dpToPx()
        }
        textViewPatientType.typeface = ResourcesCompat.getFont(context, R.font.assistant_semibold)
        textViewPatientType.setLineSpacing(-10f, 1f)
        textViewPatientType.text = patientType
        textViewPatientType.setTextColor(0xffffffff.toInt())
        textViewPatientType.textSize = 46f
        textViewPatientType.setTypeface(null, Typeface.NORMAL)
        linearLayoutPatientDetailsText.addView(textViewPatientType)
        // =======================================================================================

        // Button details params
        buttonDetails.layoutParams = LayoutParams(60.dpToPx(), 60.dpToPx())
        buttonDetails.background = ContextCompat.getDrawable(context, R.drawable.patient_details_button_background)
        linearLayoutPatientDetails.addView(buttonDetails)
        // =======================================================================================

        // Horizontal line three
        linearLayoutPatientCard.addView(viewHorizontalLineThree)
        // =======================================================================================

        // Image view patient picture params
        imageViewPatientPicture.layoutParams = LinearLayout.LayoutParams(391.dpToPx(), 199.dpToPx()).apply {
            setMargins(0, 16.dpToPx(), 0,0)
            marginStart = 20.dpToPx()

        }
        when(patientType) {
            "Pregnant" -> {
                imageViewPatientPicture.setImageResource(R.drawable.pregnant_patient_image)
            }
            "Diabetic" -> {
                imageViewPatientPicture.setImageResource(R.drawable.diabetic_patient_image)
            }
            "Asthma" -> {
                imageViewPatientPicture.setImageResource(R.drawable.asthma_patient_image)
            }
            "Senior" -> {
                imageViewPatientPicture.setImageResource(R.drawable.senior_patient_image)
            }
        }
        linearLayoutPatientCard.addView(imageViewPatientPicture)
        // =======================================================================================

    }


    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

    private fun Int.spToPx(): Float = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics))


}