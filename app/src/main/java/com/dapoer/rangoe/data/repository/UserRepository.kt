package com.dapoer.rangoe.data.repository

import com.dapoer.rangoe.data.preferences.UserModel
import com.dapoer.rangoe.data.preferences.UserPreference
import com.dapoer.rangoe.data.retrofit.api.ApiServices
import com.dapoer.rangoe.data.retrofit.response.AddLinkyiResponse
import com.dapoer.rangoe.data.retrofit.response.AktivasiTokoResponse
import com.dapoer.rangoe.data.retrofit.response.CekUsernameResponse
import com.dapoer.rangoe.data.retrofit.response.CheckEmailResponse
import com.dapoer.rangoe.data.retrofit.response.DashboardResponse
import com.dapoer.rangoe.data.retrofit.response.DeleteProductResponse
import com.dapoer.rangoe.data.retrofit.response.DetailProductResponse
import com.dapoer.rangoe.data.retrofit.response.KategoriResponse
import com.dapoer.rangoe.data.retrofit.response.LinkyiDetailResponse
import com.dapoer.rangoe.data.retrofit.response.LinkyiResponse
import com.dapoer.rangoe.data.retrofit.response.LupaPasswordResponse
import com.dapoer.rangoe.data.retrofit.response.NewPassword2Response
import com.dapoer.rangoe.data.retrofit.response.ProductStatusResponse
import com.dapoer.rangoe.data.retrofit.response.ProfileResponse
import com.dapoer.rangoe.data.retrofit.response.RegisterResponse
import com.dapoer.rangoe.data.retrofit.response.ResendOtpResponse
import com.dapoer.rangoe.data.retrofit.response.UpdatePasswordResponse
import com.dapoer.rangoe.data.retrofit.response.UpdateProductResponse
import com.dapoer.rangoe.data.retrofit.response.UpdateTokoResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UserRepository private constructor(private val pref: UserPreference, private val apiServices: ApiServices) {
    fun getUser(): Flow<UserModel> {
        return pref.getUser()
    }

    suspend fun register(name: String, email: String, password: String) : RegisterResponse {
        return apiServices.register(name, email, password)
    }

    suspend fun getProductDetail(productId: String): DetailProductResponse {
        return apiServices.getProductDetail("Bearer ${pref.getUserToken()}", productId)
    }
    suspend fun getLinkyi(): LinkyiResponse {
        return apiServices.getLinkyi("Bearer ${pref.getUserToken()}")
    }

    suspend fun getCategory(): KategoriResponse {
        return apiServices.getCategory("Bearer ${pref.getUserToken()}")
    }

    suspend fun getAnalyze(): DashboardResponse {
        return apiServices.getAnalyzeStore("Bearer ${pref.getUserToken()}")
    }

    suspend fun getLinkyi(id: String): LinkyiDetailResponse {
        return apiServices.getLinkyi("Bearer ${pref.getUserToken()}", id)
    }

    suspend fun deleteProduct(productId: String): DeleteProductResponse {
        return apiServices.deleteProduct("Bearer ${pref.getUserToken()}", productId)
    }

    suspend fun addLinkProduct(productId: String, link: String): DeleteProductResponse {
        return apiServices.addProductLink("Bearer ${pref.getUserToken()}", productId, link)
    }

    suspend fun deleteLinkProduct(productId: String, linkId: String): DeleteProductResponse {
        return apiServices.deleteLinkProduct("Bearer ${pref.getUserToken()}", productId, linkId)
    }

    suspend fun addLinkyi(link: String? = null, type: String, name: String, is_active: String): AddLinkyiResponse {
        return apiServices.addLinkyi("Bearer ${pref.getUserToken()}", link, type, name, is_active)
    }

    suspend fun LinkyiStatus(id: String, is_active: String): AddLinkyiResponse {
        return apiServices.LinkyiStatus("Bearer ${pref.getUserToken()}", id, is_active)
    }

    suspend fun LinkyiUpdate(id: String, link: String? = null, name: String, is_active: String): AddLinkyiResponse {
        return apiServices.LinkyiUpdate("Bearer ${pref.getUserToken()}", id, link, name, is_active)
    }
    suspend fun deleteLinkyi(id: String): AddLinkyiResponse {
        return apiServices.deleteLinkyi("Bearer ${pref.getUserToken()}", id)
    }

    suspend fun checkUsername(username: String): CekUsernameResponse {
        return apiServices.checkUsername("Bearer ${pref.getUserToken()}",username)
    }

    suspend fun checkEmail(email: String): CheckEmailResponse {
        return apiServices.checkEmail(email)
    }
    suspend fun activateStore(
        name: RequestBody,
        username: RequestBody,
        description: RequestBody,
        logo: MultipartBody.Part
    ): AktivasiTokoResponse {
        return apiServices.activateStore("Bearer ${pref.getUserToken()}",name, username, description, logo)
    }

    suspend fun updateProduct(
        productId: String,
        title: RequestBody,
        price: RequestBody,
        category: RequestBody,
        thumbnail: MultipartBody.Part?
    ): UpdateProductResponse {
        return apiServices.updateProduct("Bearer ${getUserToken()}", productId, title, price, category, thumbnail)
    }

    suspend fun updateStore(
        name: RequestBody,
        description: RequestBody?,
        logo: MultipartBody.Part?
    ): UpdateTokoResponse {
        return apiServices.updateStore("Bearer ${getUserToken()}", name, description, logo)
    }

    suspend fun addProduct(
        title: RequestBody,
        price: RequestBody,
        category: RequestBody,
        thumbnail: MultipartBody.Part,
        isActive: RequestBody,
        links: List<MultipartBody.Part>
    ): DeleteProductResponse {
        return apiServices.addProduct("Bearer ${getUserToken()}", title, price, category, thumbnail, isActive, links)
    }
    suspend fun productStatus(
        productId: String,
        isActive: String,
    ): ProductStatusResponse {
        return apiServices.productStatus("Bearer ${getUserToken()}", productId, isActive)
    }
    suspend fun getStoreProfile(): ProfileResponse {
        return apiServices.getStoreProfile("Bearer ${getUserToken()}")
    }
    suspend fun updatePassword(
        password: String,
        confirmPassword: String,
        currentPassword: String
    ): UpdatePasswordResponse {
        return apiServices.updatePassword("Bearer ${pref.getUserToken()}", password, confirmPassword, currentPassword)
    }

//    suspend fun login(email: String, password: String) : LoginResponse {
//        return apiServices.login(email, password)
//    }

//    suspend fun otpVerification(code: Int, email: String?) : OTPResponse {
//        return apiServices.OTP(code, email)
//    }

    suspend fun resendOtp(email: String?): ResendOtpResponse {
        return apiServices.resendOTP(email)
    }

    suspend fun lupaPassword(email: String): LupaPasswordResponse {
        return apiServices.lupapassword(email)
    }

    suspend fun newPassword(password: String, confirmPassword: String, otp: Int): NewPassword2Response {
        return apiServices.newPassword(password, confirmPassword, otp)
    }

    suspend fun saveUserToken(token: String) {
        pref.saveToken(token)
    }
    suspend fun deleteToken() {
        pref.deleteToken()
    }
    suspend fun getUserToken(): String {
       return pref.getUserToken()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            pref: UserPreference,
            apiServices: ApiServices
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(pref, apiServices)
            }.also { instance = it }
    }
}