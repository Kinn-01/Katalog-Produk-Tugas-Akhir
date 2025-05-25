package com.example.linkyishop.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityRegisterBinding
import com.example.linkyishop.ui.aktivasiToko.AktivasiTokoActivity
import com.example.linkyishop.ui.login.LoginActivity
import com.example.linkyishop.ui.otp.OtpVerifActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var email: String

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRegister()

        // Tambahan: hilangkan error saat user mengetik ulang
        binding.emaileditText.addTextChangedListener {
            binding.emaileditText.error = null
        }

        binding.passwordEditText.addTextChangedListener {
            binding.passwordEditText.error = null
        }

        binding.signUpTextView.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun setupRegister() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            email = binding.emaileditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(name, email, password)) {
                true.showLoading()
                viewModel.checkEmail(email)
                viewModel.register(name, email, password)
            } else {
                false.showLoading()
            }
        }

        viewModel.emailCheckResult.observe(this) { result ->
            result.onSuccess { response ->
                when (response.data?.status) {
                    null -> {
                        showError(response.message ?: "Email sudah digunakan. Silakan masuk.")
                        binding.emaileditText.error = "Email sudah digunakan. Silakan masuk."
                    }
                    false, true -> {
                        navigateToOtpScreen(email)
                    }
                }
            }.onFailure { throwable ->
                val message = throwable.localizedMessage ?: "Terjadi kesalahan saat memeriksa email."
                showError(message)
            }
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { response ->
                false.showLoading()
                if (response.success == true) {
                    navigateToOtpScreen(email)
                } else {
                    val errorMessage = response.registerResult?.email ?: response.message ?: "Registrasi gagal. Silakan coba lagi."
                    binding.emaileditText.error = response.registerResult?.email // Tampilkan di bawah input
                    showError(errorMessage)
                }
            }.onFailure { throwable ->
                false.showLoading()
                val message = throwable.localizedMessage ?: "Terjadi kesalahan. Silakan coba lagi."
                showError(message)
            }
        }
    }

    private fun navigateToOtpScreen(email: String) {
        val intent = Intent(this@RegisterActivity, OtpVerifActivity::class.java)
        intent.putExtra(OtpVerifActivity.EXTRA_EMAIL, email)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.validasiRegister))
            isValid = false
        }

        if (!isEmailValid(email)) {
            binding.emaileditText.error = "Format email tidak valid"
            isValid = false
        }

        if (password.length < 6) {
            binding.passwordEditText.error = "Password minimal 6 karakter"
            isValid = false
        }

        return isValid
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Boolean.showLoading() {
        binding.progressBar.visibility = if (this) View.VISIBLE else View.GONE
    }
}
