package com.example.authentication.presentation

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.authentication.R
import com.example.authentication.databinding.ActivityAuthenticationBinding
import com.example.authentication.databinding.AuthCustomLoadingDialogBinding
import com.example.authentication.domain.entity.SSOEntity
import com.example.common.presentation.utils.AsteriskPasswordTransformationMethod
import com.example.common.presentation.utils.showHidePass
import com.example.navigation.navigateToCoreActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var customDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        initCustomDialog()

        viewModel.loginStateFlow.observe(this) {
            when {
                it.isSuccessful() -> {
                    Log.d("Login", it.data?.data?.accessToken ?: "nothing here")
                    customDialog.cancel()
                    navigateToCoreActivity(this)
                }
                it.isError() -> {
                    Toast.makeText(
                        this,
                        "oh no, it's an error: ${it.data?.msg}",
                        Toast.LENGTH_SHORT
                    ).show()
                    customDialog.cancel()
                }
            }
        }

        binding.textViewLoginRegister.text = Html.fromHtml("<u>Đăng ký tài khoản</u>")

        val ssoData = listOf(
            SSOEntity(R.drawable.zalo_logo, "Zalo"),
            SSOEntity(R.drawable.facebook_logo, "Facebook"),
            SSOEntity(R.drawable.apple_logo, "Apple"),
            SSOEntity(R.drawable.google_logo, "Google"),
        )

        binding.recyclerViewLoginSso.apply {
            adapter = SSORecyclerViewAdapter(ssoData)
            layoutManager = LinearLayoutManager(this@AuthenticationActivity)
        }

        binding.editTextLoginPassword.transformationMethod = AsteriskPasswordTransformationMethod()

        binding.imageButtonLoginShowHidePass.setOnClickListener {
            showHidePass(this, binding.editTextLoginPassword, binding.imageButtonLoginShowHidePass)
        }

        binding.buttonLoginLoginButton.setOnClickListener {
            viewModel.login()
            customDialog.show()
        }
    }

    private fun initCustomDialog() {
        val dialogBinding: AuthCustomLoadingDialogBinding? =
            DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.auth_custom_loading_dialog,
                null,
                false
            )

        customDialog = AlertDialog.Builder(this, 0).create()

        customDialog.apply {
            setView(dialogBinding?.root)
            setCancelable(false)
        }
    }
}