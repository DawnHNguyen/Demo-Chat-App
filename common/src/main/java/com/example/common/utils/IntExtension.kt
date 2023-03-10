package com.example.common.utils

import android.content.Context
import android.util.DisplayMetrics


fun Int.dpToPx(context: Context): Int {
    return this * (context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}