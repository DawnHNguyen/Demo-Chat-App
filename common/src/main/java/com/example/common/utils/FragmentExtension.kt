package com.example.common.utils

import androidx.fragment.app.Fragment
import com.example.common.utils.hideKeyboard

fun Fragment.hideKeyboard(){
    activity?.hideKeyboard()
}