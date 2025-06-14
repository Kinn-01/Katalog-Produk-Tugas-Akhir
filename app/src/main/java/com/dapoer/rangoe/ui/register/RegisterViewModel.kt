package com.dapoer.rangoe.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapoer.rangoe.data.repository.UserRepository
import com.dapoer.rangoe.data.retrofit.response.CheckEmailResponse
import com.dapoer.rangoe.data.retrofit.response.RegisterResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    private val _emailCheckResult = MutableLiveData<Result<CheckEmailResponse>>()
    val emailCheckResult: LiveData<Result<CheckEmailResponse>> = _emailCheckResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.register(name, email, password)
                _registerResult.value = Result.success(response)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }

    fun checkEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.checkEmail(email)
                _emailCheckResult.value = Result.success(response)
            } catch (e: Exception) {
                _emailCheckResult.value = Result.failure(e)
            }
        }
    }
}