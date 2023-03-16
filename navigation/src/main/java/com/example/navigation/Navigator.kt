package com.example.navigation

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity

fun navigateToCoreActivity(context: Context){
    val intent = Intent(context, Class.forName("com.example.core.presentation.ui.MainActivity"))
    startActivity(context, intent, null)
}