package com.dapoer.rangoe.ui.login

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
import com.dapoer.rangoe.ui.main.MainActivity
import com.dapoer.rangoe.R
import com.dapoer.rangoe.data.ViewModelFactory
import com.dapoer.rangoe.databinding.ActivityLoginBinding
import com.dapoer.rangoe.ui.aktivasiToko.AktivasiTokoActivity
import com.dapoer.rangoe.ui.lupaPassword.LupaPasswordActivity
import com.dapoer.rangoe.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupLogin()

        binding.forgotPasswordTextView.setOnClickListener {
            // Tambahkan kode navigasi ke halaman lupa password di sini
            navigateToForgotPassword()
        }

        binding.signUpTextView.setOnClickListener {
            navigateToRegister()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupLogin() {
        binding.loginButton.setOnClickListener {
            val email = binding.emaileditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(email, password)) {
                viewModel.login(email, password)
                true.showLoading()
            }
        }

        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { dataLogin ->
                false.showLoading()
                viewModel.saveUserToken(dataLogin.token)
                if (dataLogin.isActive) {
                    navigateToMainScreen()
                } else {
                    navigateToAktivasiTokoScreen()
                }
            }.onFailure { throwable ->
                false.showLoading()
                val message = throwable.localizedMessage ?: "Login gagal. Silakan coba lagi."
                showError(message)
            }
        }

    }

    private fun validateInput( email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty() || password.isEmpty()) {
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

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun navigateToAktivasiTokoScreen() {
        val intent = Intent(this, AktivasiTokoActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }


    private fun navigateToForgotPassword() {
        val intent = Intent(this, LupaPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Boolean.showLoading() {
        binding.progressBar.visibility = if (this) View.VISIBLE else View.GONE
    }
}