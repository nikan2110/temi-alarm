package com.nikita.doroshenko.japanmeeting.layouts

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.RelativeLayout

class ViewHorizontalLine(context: Context, marginStart: Int, marginTop: Int): View(context) {

    var marginStart = marginStart
    var marginTop = marginTop

    init {
        layoutParams = RelativeLayout.LayoutParams(389.dpToPx(), 3.dpToPx()).apply {
            setMargins(marginStart.dpToPx(), marginTop.dpToPx(), 0, 0)
        }
        setBackgroundColor(Color.parseColor("#221219"))
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()

}