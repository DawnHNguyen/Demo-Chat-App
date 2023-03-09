package com.example.common.utils

import android.content.Context


fun Int.dpToPx(context: Context): Int {
    return this * context.resources.displayMetrics.density.toInt()
}