package com.farhanrv.thestory.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farhanrv.thestory.data.network.request.SignupRequest
import com.farhanrv.thestory.databinding.ActivitySignupBinding
import com.farhanrv.thestory.ui.auth.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            when {
                name.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan nama"
                }
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Masukkan password"
                }
                else -> {
                    viewModel.register(
                        SignupRequest(name, email, password)
                    ) { response ->
                        if (response?.error == false) {
                            createToast("Berhasil mendaftar!")
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            if (response != null) {
                                createToast(response.message)
                                Log.e("SignupActivity", response.message)
                            }
                        }
                    }
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun createToast(text: String) {
        Toast.makeText(
            this@SignupActivity,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}