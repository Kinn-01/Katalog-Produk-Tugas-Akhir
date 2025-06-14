package com.dapoer.rangoe.di

import android.content.Context
import com.dapoer.rangoe.data.preferences.UserPreference
import com.dapoer.rangoe.data.preferences.dataStore
import com.dapoer.rangoe.data.repository.UserRepository
import com.dapoer.rangoe.data.retrofit.api.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUser().first() }
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}