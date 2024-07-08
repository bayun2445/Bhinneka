package com.bayu.bhinneka.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bayu.bhinneka.R
import com.bayu.bhinneka.databinding.ActivityRegisterBinding
import com.bayu.bhinneka.helper.ROLE_EXTRA
import com.bayu.bhinneka.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            if (!binding.edSignupEmail.text.isNullOrEmpty()
                && !binding.edSignupPassword.text.isNullOrEmpty()) {
                val email = binding.edSignupEmail.text.toString()
                val password = binding.edSignupPassword.text.toString()

                viewModel.registerWithEmail(email, password)
            } else {
                showToast(getString(R.string.empty_field_alert))
            }

        }

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.currentUser.observe(this) {
            updateUI(it)
        }

        viewModel.message.observe(this) { text ->
            showToast(text)
        }
    }

    private fun showToast(text: String?) {
        text?.let{
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
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
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}