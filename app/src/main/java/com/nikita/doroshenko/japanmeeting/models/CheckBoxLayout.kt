package com.nikita.doroshenko.japanmeeting.models

import android.content.Context
import android.graphics.Color
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.nikita.doroshenko.japanmeeting.R

class CheckBoxLayout(context: Context, checkBoxText:String, id:String, isDone:Boolean) : RelativeLayout(context) {

     val button = Button(context)
     val checkBoxId = id
     var checkIsDone = isDone


    init {
        // Set layout parameters for the RelativeLayout
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        // Create and add the Button to the RelativeLayout
        button.layoutParams = LayoutParams(128.dpToPx(), 128.dpToPx()).apply {
            setMargins(84.dpToPx(), 0, 0, 0)
        }
        if (checkIsDone) {
            button.background = AppCompatResources.getDrawable(context,R.drawable.checked_box_background)
        } else {
            button.background = AppCompatResources.getDrawable(context,R.drawable.unchecked_box_background)

        }
        addView(button)

        // Create and add the LinearLayout to the RelativeLayout
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LayoutParams(
            439.dpToPx(),
            600.dpToPx()
        ).apply {
            setMargins(47.dpToPx(), 45.dpToPx(), 0, 0)
        }
        linearLayout.background = AppCompatResources.getDrawable(context,R.color.black_transparent_80percent)
        linearLayout.orientation = LinearLayout.VERTICAL
        addView(linearLayout)

        // Create and add the ImageView to the LinearLayout
        val imageView = ImageView(context)
        imageView.layoutParams = LayoutParams(
            391.dpToPx(),
            289.dpToPx()
        ).apply {
            setMargins(24.dpToPx(), 29.dpToPx(), 24.dpToPx(), 0)
        }
        when(checkBoxId) {
            "6405b4aef8f770781c8d7974" -> {
                imageView.background = AppCompatResources.getDrawable(context, R.drawable.check_list_safety_zone_image)
                imageView.contentDescription = context.getString(R.string.safety_zone_sign)
            }
            "6405b71af8f770781c8d7977" -> {
                imageView.background = AppCompatResources.getDrawable(context, R.drawable.emergency_kit_image)
                imageView.contentDescription = context.getString(R.string.emergency_kit_sign)
            }
            "6405b852f8f770781c8d7978" -> {
                imageView.background = AppCompatResources.getDrawable(context, R.drawable.forward_instructions_image)
                imageView.contentDescription = context.getString(R.string.forward_instructions_sign)
            }
        }
        linearLayout.addView(imageView)

        // Create and add the TextView to the LinearLayout
        val textView = TextView(context)
        textView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(24.dpToPx(), 28.dpToPx(), 56.dpToPx(), 21.dpToPx())
        }
        textView.text = checkBoxText
        textView.setTextColor(Color.WHITE)
        textView.setTextSize(25f)
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.assistant_semibold))
        textView.setLineSpacing(8.dpToPx().toFloat(), 1f)
        linearLayout.addView(textView)
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

}