package com.dapoer.rangoe.ui.aktivasiToko

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapoer.rangoe.data.repository.UserRepository
import com.dapoer.rangoe.data.retrofit.response.AktivasiTokoResponse
import com.dapoer.rangoe.data.retrofit.response.CekUsernameResponse
import com.dapoer.rangoe.data.retrofit.response.Store
import com.dapoer.rangoe.data.retrofit.response.Visitors
import com.dapoer.rangoe.ui.product.toRequestBody
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class AktivasiTokoViewModel(private val repository: UserRepository) : ViewModel() {

    private val _cekUsernameResult = MutableLiveData<Pair<Boolean, CekUsernameResponse?>>()
    val cekUsernameResult: LiveData<Pair<Boolean, CekUsernameResponse?>> = _cekUsernameResult

    private val _activateStoreResult = MutableLiveData<Result<AktivasiTokoResponse>>()
    val activateStoreResult: LiveData<Result<AktivasiTokoResponse>> = _activateStoreResult

    private val _profileResult = MutableLiveData<Store>()
    val profileResult: LiveData<Store> = _profileResult

    private val _dashboard = MutableLiveData<Visitors>()
    val dashboard: LiveData<Visitors> get() = _dashboard

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun checkUsername(username: String, isCheck: Boolean = true) {
        viewModelScope.launch {
            try {
                val response = repository.checkUsername(username)
                _cekUsernameResult.value = Pair(isCheck, response)
            } catch (e: IOException) {
                _cekUsernameResult.value = Pair(isCheck, null)
            } catch (e: HttpException) {
                _cekUsernameResult.value = Pair(isCheck, null)
            }
        }
    }

    fun activateStore(
        name: String,
        username: String,
        description: String,
        logo: MultipartBody.Part
    ) {
        viewModelScope.launch {
            val requestBodyName = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestBodyUsername = username.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaTypeOrNull())

            try {
                val response = repository.activateStore(
                    requestBodyName,
                    requestBodyUsername,
                    requestBodyDescription,
                    logo
                )
                if (response.success == true) {
                    _activateStoreResult.value = Result.success(response)
                } else {
                    _activateStoreResult.value = Result.failure(Exception(response.message ?: "Gagal mengaktifkan toko"))
                }
            } catch (e: IOException) {
                _activateStoreResult.value = Result.failure(e)
            } catch (e: HttpException) {
                _activateStoreResult.value = Result.failure(Exception(e.message()))
            }
        }
    }

    fun getDashboard() {
        viewModelScope.launch {
            try {
                val response = repository.getAnalyze()
                _dashboard.value = response.data?.visitors!!
            } catch (e: Exception) {
                Log.e("Failed to fetch dashboard analyze", e.message ?: "Unknown error")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
           try {
               val response = repository.getStoreProfile()
               _profileResult.value = response.data?.store!!
           } catch (e: Exception) {
               Log.e("Failed to fetch store profile", e.message ?: "Unknown Error")
           } finally {
               _isLoading.value = false
           }
        }
    }

    fun deleteUserToken() {
        viewModelScope.launch {
            repository.deleteToken()
        }
    }
}
