package com.jarvis.sprinkle.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.jarvis.sprinkle.R

object ColorChipPalette {

    private val colorList = listOf(
        R.color.category_1,
        R.color.category_2,
        R.color.category_3,
        R.color.category_4,
        R.color.category_5,
        R.color.category_6,
        R.color.category_7,
        R.color.category_8,
        R.color.category_9,
        R.color.category_10
    )

    fun getColorFor(position: Int, context: Context): Int {
        var pos = position
        if (position >= colorList.size) {
            pos %= colorList.size
        }
        return ContextCompat.getColor(context, colorList[pos])
    }
}