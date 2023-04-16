package com.farhanrv.thestory.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farhanrv.thestory.data.network.request.LoginRequest
import com.farhanrv.thestory.databinding.ActivityLoginBinding
import com.farhanrv.thestory.model.User
import com.farhanrv.thestory.ui.auth.signup.SignupActivity
import com.farhanrv.thestory.ui.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    viewModel.auth(
                        LoginRequest(email, password)
                    ) { response ->
                        if (response?.error == false) {
                            viewModel.savePreferences(
                                response.loginResult.token,
                                User(
                                    email,
                                    response.loginResult.name,
                                    password
                                )
                            )
                            navigateToHome(response.loginResult.token)
                        } else {
                            createToast("Login gagal! Harap periksa data diri anda")
                        }

                    }
                }
            }
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createToast(text: String) {
        Toast.makeText(
            this@LoginActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun navigateToHome(token: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_LOGIN, token)
        startActivity(intent)
        finish()
    }
}