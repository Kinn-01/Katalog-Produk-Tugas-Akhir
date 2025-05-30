package com.example.linkyishop.data

import com.example.linkyishop.ui.aktivasiToko.AktivasiTokoViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.di.Injection
import com.example.linkyishop.ui.aktivasiToko.UpdateStoreViewModel
import com.example.linkyishop.ui.detailProduct.DetailProductViewModel
import com.example.linkyishop.ui.linkyi.LinkyiViewModel
import com.example.linkyishop.ui.listKategori.ListKategoriViewModel
import com.example.linkyishop.ui.login.LoginViewModel
import com.example.linkyishop.ui.lupaPassword.LupaPasswordViewModel
import com.example.linkyishop.ui.main.MainViewModel
import com.example.linkyishop.ui.newPassword.NewPasswordViewModel
import com.example.linkyishop.ui.otp.OtpViewModel
import com.example.linkyishop.ui.product.AddProductViewModel
import com.example.linkyishop.ui.product.ProductViewModel
import com.example.linkyishop.ui.product.UpdateProductViewModel
import com.example.linkyishop.ui.register.RegisterViewModel
import com.example.linkyishop.ui.updatePassword.UpdatePasswordViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OtpViewModel::class.java) -> {
                OtpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LupaPasswordViewModel::class.java) -> {
                LupaPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LinkyiViewModel::class.java) -> {
                LinkyiViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NewPasswordViewModel::class.java) -> {
                NewPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailProductViewModel::class.java) -> {
                DetailProductViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
                ProductViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddProductViewModel::class.java) -> {
                AddProductViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AktivasiTokoViewModel::class.java) -> {
                AktivasiTokoViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdatePasswordViewModel::class.java) -> {
                UpdatePasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateProductViewModel::class.java) -> {
                UpdateProductViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateStoreViewModel::class.java) -> {
                UpdateStoreViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ListKategoriViewModel::class.java) -> {
                ListKategoriViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}