package com.example.linkyishop.ui.product

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.linkyishop.BuildConfig
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.data.preferences.dataStore
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.api.ApiConfig
import com.example.linkyishop.data.retrofit.api.ApiServices
import com.example.linkyishop.data.retrofit.response.DataItem
import com.example.linkyishop.data.retrofit.response.Products
import com.example.linkyishop.utils.Event
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductViewModel(private val repository: UserRepository) : ViewModel() {

    private val _listProduct = MutableLiveData<Products>()
    val listProduct: LiveData<Products> get() = _listProduct

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    suspend fun getProducts() {
        _isLoading.value = true
        try {
            val token = repository.getUserToken()
            var currentPage = 1
            val allProducts = mutableListOf<DataItem>()
            var hasMorePages = true

            while (hasMorePages) {
                val response = ApiConfig.getApiService().getProducts("Bearer $token", currentPage)
                if (response.isSuccessful) {
                    val products = response.body()?.data?.products
                    if (products != null) {
                        allProducts.addAll((products.data ?: emptyList()) as Collection<DataItem>)
                        currentPage++
                        hasMorePages = products.currentPage ?: 0 < products.lastPage ?: 0
                    } else {
                        Log.e("Products", "Data produk null")
                        break
                    }
                } else {
                    Log.e("Products", "Error: ${response.message()}")
                    break
                }
            }

            _listProduct.postValue(Products(
                perPage = allProducts.size,
                data = allProducts,
                lastPage = 1,
                nextPageUrl = null,
                prevPageUrl = null,
                firstPageUrl = null,
                path = null,
                total = allProducts.size,
                lastPageUrl = null,
                from = 1,
                links = null,
                to = allProducts.size,
                currentPage = 1
            ))

        } catch (e: Exception) {
            Log.e("Products", "Exception: ${e.message.toString()}")
        } finally {
            _isLoading.value = false
        }
    }
}