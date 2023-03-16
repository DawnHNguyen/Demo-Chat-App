package com.example.authentication.presentation

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.example.authentication.domain.entity.SSOPlatform
import com.example.common.presentation.utils.AsteriskPasswordTransformationMethod
import com.example.common.presentation.utils.showHidePass
import com.example.navigation.navigateToCoreActivity
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthenticationBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var customDialog: AlertDialog

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var googleLoginForResult: ActivityResultLauncher<Intent>

    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        googleLoginForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    val acc = GoogleSignIn.getSignedInAccountFromIntent(it.data).getResult(ApiException::class.java)
                    Toast.makeText(this, "Login with Google success", Toast.LENGTH_SHORT).show()
                    Log.d("Login", "Login with Google success, ${acc.email}")
                }
                catch (e: ApiException) {
                    Log.d("Login", "signInResult:failed code=" + e.statusCode)
                    Toast.makeText(this, "Login with Google fail: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        initCustomDialog()

        initGoogleSSO()

        initFacebookSSO()

        viewModel.loginStateFlow.observe(this) {
            when {
                it.isSuccessful() -> {
                    Log.d("Login", it.data?.data?.accessToken ?: "nothing here")
                    customDialog.cancel()
                    navigateToCoreActivity(this)
                    finish()
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
            SSOEntity(R.drawable.zalo_logo, "Zalo", SSOPlatform.Zalo),
            SSOEntity(R.drawable.facebook_logo, "Facebook", SSOPlatform.Facebook),
            SSOEntity(R.drawable.apple_logo, "Apple", SSOPlatform.Apple),
            SSOEntity(R.drawable.google_logo, "Google", SSOPlatform.Google),
        )

        binding.recyclerViewLoginSso.apply {
            adapter = SSORecyclerViewAdapter(ssoData, object: OnClickSSO {
                override fun loginSSO(type: SSOPlatform) {
                    when(type){
                        SSOPlatform.Google -> googleSignIn()

                        SSOPlatform.Facebook -> facebookSignIn()

                        else -> {
                            Log.d("Login", "signout")
                            googleSignInClient.signOut()
                            googleSignInClient.revokeAccess()
                        }
                    }
                }
            })
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

    private fun initFacebookSSO() {
        callbackManager = create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Login with Facebook success",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onCancel() {
                    // App code
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(
                        this@AuthenticationActivity,
                        "Login with Facebook fail: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Login", "${error.message}")
                }
            })
    }

    private fun initGoogleSSO() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
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

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent

        googleLoginForResult.launch(signInIntent)
    }

    private fun facebookSignIn(){
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}