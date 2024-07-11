package com.bayu.bhinneka.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.bayu.bhinneka.BuildConfig
import com.bayu.bhinneka.R
import com.bayu.bhinneka.databinding.ActivityLoginBinding
import com.bayu.bhinneka.helper.ROLE_EXTRA
import com.bayu.bhinneka.ui.main.MainActivity
import com.bayu.bhinneka.ui.register.RegisterActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setButtonsOnClickListener()

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.currentUser.observe(this) {
            updateUI(it)
        }

        viewModel.message.observe(this) {
            showToast(it)
        }
    }

    private fun setButtonsOnClickListener() {
        binding.btnSignInGoogle.setOnClickListener {
            signInGoogle()
        }

        binding.btnSignIn.setOnClickListener {
            signInEmail()
        }

        binding.txtCreateAccount.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }

    private fun signInEmail() {
        if (!binding.edLoginEmail.text.isNullOrEmpty()
            && !binding.edLoginPassword.text.isNullOrEmpty()) {

            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            viewModel.signInWithEmail(email, password)
        } else {
            showToast(getString(R.string.empty_field_alert))
        }
    }

    private fun signInGoogle() {
        val credentialManager = CredentialManager.create(this)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.CLIENT_ID)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )

                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        viewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }
            }
            else -> {
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            viewModel.getRole().observe(this) {
                if (it == "admin") {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .putExtra(ROLE_EXTRA, true)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                            .putExtra(ROLE_EXTRA, false)
                    )
                    finish()
                }
            }

        }
    }

    private fun showToast(text: String?) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}