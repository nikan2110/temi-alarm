package com.nikita.doroshenko.japanmeeting.layouts

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout

class ViewHorizontalLine(context: Context, marginStartParams: Int, marginTopParams: Int): View(context) {

    var marginStartParams = marginStartParams
    var marginTopParams = marginTopParams

    init {
        layoutParams = RelativeLayout.LayoutParams(389.dpToPx(), 3.dpToPx()).apply {
            setMargins(0, marginTopParams.dpToPx(), 0, 0)
            marginStart = marginStartParams.dpToPx()
        }
        setBackgroundColor(Color.parseColor("#221219"))
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

}