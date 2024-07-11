package com.bayu.bhinneka.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bayu.bhinneka.R
import com.bayu.bhinneka.databinding.ActivitySplashBinding
import com.bayu.bhinneka.helper.ROLE_EXTRA
import com.bayu.bhinneka.ui.login.LoginActivity
import com.bayu.bhinneka.ui.main.MainActivity
import com.google.firebase.auth.FirebaseUser

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.currentUser.observe(this) {
                updateUI(it)
            }
        }, 600)
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

        } else {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }
    }
}