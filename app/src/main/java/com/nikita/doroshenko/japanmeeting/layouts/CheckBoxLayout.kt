package com.nikita.doroshenko.japanmeeting.layouts

import android.content.Context
import android.graphics.Color
import android.text.method.ScrollingMovementMethod
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import com.nikita.doroshenko.japanmeeting.R

class CheckBoxLayout(context: Context, checkBoxText:String, id:String, isDone:Boolean, tag: String) : RelativeLayout(context) {

    val button = Button(context)
    val checkBoxId = id
    var checkIsDone = isDone


    init {
        // Set layout parameters for the RelativeLayout
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            marginStart = 15.dpToPx()
        }
        // Create and add the Button to the RelativeLayout
        button.layoutParams = LayoutParams(128.dpToPx(), 128.dpToPx()).apply {
            marginStart = 84.dpToPx()
        }
        button.foreground = context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground)).getDrawable(0)
        if (checkIsDone) {
            button.background = AppCompatResources.getDrawable(context,R.drawable.checked_box_background)
        } else {
            button.background = AppCompatResources.getDrawable(context,R.drawable.unchecked_box_background)

        }
        addView(button)
        // =======================================================================================

        // Create and add the LinearLayout to the RelativeLayout
        val linearLayout = LinearLayout(context)
        linearLayout.layoutParams = LayoutParams(
            439.dpToPx(),
            600.dpToPx()
        ).apply {
            setMargins(0, 45.dpToPx(), 0, 0)
            marginStart = 47.dpToPx()
        }
        linearLayout.background = AppCompatResources.getDrawable(context,R.color.black_transparent_80percent)
        linearLayout.orientation = LinearLayout.VERTICAL
        addView(linearLayout)
        // =======================================================================================

        // Create and add the ImageView to the LinearLayout
        val imageView = ImageView(context)
        imageView.layoutParams = LayoutParams(
            391.dpToPx(),
            289.dpToPx()
        ).apply {
            setMargins(0, 29.dpToPx(), 0, 0)
            marginStart = 24.dpToPx()
            marginEnd = 24.dpToPx()
        }
        when(tag) {
            "integrity" -> {
                imageView.background = AppCompatResources.getDrawable(context, R.drawable.check_list_safety_zone_image)
                imageView.contentDescription = context.getString(R.string.safety_zone_sign)
            }
            "kit" -> {
                imageView.background = AppCompatResources.getDrawable(context, R.drawable.emergency_kit_image)
                imageView.contentDescription = context.getString(R.string.emergency_kit_sign)
            }
            "contact" -> {
                imageView.background = AppCompatResources.getDrawable(context, R.drawable.forward_instructions_image)
                imageView.contentDescription = context.getString(R.string.forward_instructions_sign)
            }
        }
        linearLayout.addView(imageView)
        // =======================================================================================

        // Create and add the TextView to the LinearLayout
        val textView = TextView(context)
        textView.layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 28.dpToPx(), 0, 21.dpToPx())
            marginStart = 24.dpToPx()
            marginEnd = 56.dpToPx()

        }
        textView.text = checkBoxText
        textView.setTextColor(Color.WHITE)
        textView.setTextSize(25f)
        textView.setTypeface(ResourcesCompat.getFont(context, R.font.assistant_semibold))
        textView.setLineSpacing(8.dpToPx().toFloat(), 1f)
        textView.movementMethod = ScrollingMovementMethod()
        linearLayout.addView(textView)
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

}