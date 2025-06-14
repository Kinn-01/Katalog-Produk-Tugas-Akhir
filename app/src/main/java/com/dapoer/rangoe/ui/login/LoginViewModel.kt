package com.dapoer.rangoe.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapoer.rangoe.data.repository.UserRepository
import com.dapoer.rangoe.data.retrofit.api.ApiConfig
import com.dapoer.rangoe.data.retrofit.response.DataLogin
import com.dapoer.rangoe.data.retrofit.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Result<DataLogin>>()
    val loginResult: LiveData<Result<DataLogin>> = _loginResult

    fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data != null) {
                        _loginResult.value = Result.success(data)
                    } else {
                        _loginResult.value = Result.failure(Exception("Data login kosong"))
                    }
                } else {
                    _loginResult.value = Result.failure(Exception("Login gagal: ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _loginResult.value = Result.failure(t)
            }
        })
    }

    fun saveUserToken(token: String) {
        viewModelScope.launch {
            repository.saveUserToken(token)
        }
    }
}
