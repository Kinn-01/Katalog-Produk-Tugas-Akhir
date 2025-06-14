package com.dapoer.rangoe.ui.otp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dapoer.rangoe.R
import com.dapoer.rangoe.data.ViewModelFactory
import com.dapoer.rangoe.databinding.ActivityOtpVerifBinding
import com.dapoer.rangoe.ui.aktivasiToko.AktivasiTokoActivity
import com.dapoer.rangoe.ui.login.LoginViewModel

class OtpVerifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerifBinding

    private val viewModel by viewModels<OtpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpVerifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = intent.getStringExtra(EXTRA_EMAIL)

        setupOtpInputs()

        binding.btnVerify.setOnClickListener {
            val otpCode = getOtpCode()
            if (otpCode != null) {
                setVerifyButtonEnabled(false)
                true.showLoading()
                viewModel.verifyOTP(otpCode, email)
            } else {
                showError(getString(R.string.errorOTP))
                setOtpErrorVisual(true)
            }
        }

        binding.btnResendOtp.setOnClickListener {
            viewModel.resendOtp(email)
            true.showLoading()
        }

        viewModel.otpResult.observe(this) { result ->
            false.showLoading()
            setVerifyButtonEnabled(true)

            if (result.success == true) {
                loginViewModel.saveUserToken(result.data?.token.orEmpty())
                navigateToAktivasiScreen()
            } else {
                setOtpErrorVisual(true)
                showError(result.message ?: getString(R.string.errorOTP))
            }
        }


        viewModel.resendOtpResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(
                    this,
                    it.message ?: getString(R.string.otp_resent),
                    Toast.LENGTH_SHORT
                ).show()
                false.showLoading()
            }.onFailure {
                showError(it.message)
                false.showLoading()
            }
        }
    }

    private fun setupOtpInputs() {
        val otpFields = listOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)

        for (i in otpFields.indices) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    setOtpErrorVisual(false)
                    if (s?.length == 1 && i < otpFields.size - 1) {
                        otpFields[i + 1].requestFocus()
                    }
                }
            })
        }
    }

    private fun getOtpCode(): Int? {
        val otp = listOf(
            binding.etOtp1.text.toString(),
            binding.etOtp2.text.toString(),
            binding.etOtp3.text.toString(),
            binding.etOtp4.text.toString(),
            binding.etOtp5.text.toString(),
            binding.etOtp6.text.toString()
        ).joinToString("")

        return if (otp.length == 6 && otp.all { it.isDigit() }) otp.toIntOrNull() else null
    }

    private fun navigateToAktivasiScreen() {
        val intent = Intent(this, AktivasiTokoActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String?) {
        Toast.makeText(this, message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }

    private fun Boolean.showLoading() {
        binding.progressBar.visibility = if (this) View.VISIBLE else View.GONE
    }

    private fun setVerifyButtonEnabled(enabled: Boolean) {
        binding.btnVerify.isEnabled = enabled
        binding.btnVerify.alpha = if (enabled) 1.0f else 0.6f
    }

    private fun setOtpErrorVisual(show: Boolean) {
        val fields = listOf(binding.etOtp1, binding.etOtp2, binding.etOtp3, binding.etOtp4, binding.etOtp5, binding.etOtp6)
        fields.forEach {
            it.backgroundTintList = ContextCompat.getColorStateList(this,
                if (show) R.color.design_default_color_error else R.color.colorPrimary)
        }
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}

