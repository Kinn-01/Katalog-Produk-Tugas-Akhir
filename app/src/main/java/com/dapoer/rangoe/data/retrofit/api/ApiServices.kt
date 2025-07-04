package com.dapoer.rangoe.data.retrofit.api

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
import com.dapoer.rangoe.data.retrofit.response.LoginResponse
import com.dapoer.rangoe.data.retrofit.response.LupaPasswordResponse
import com.dapoer.rangoe.data.retrofit.response.NewPassword2Response
import com.dapoer.rangoe.data.retrofit.response.OTPResponse
import com.dapoer.rangoe.data.retrofit.response.PredictionResponse
import com.dapoer.rangoe.data.retrofit.response.ProductStatusResponse
import com.dapoer.rangoe.data.retrofit.response.ProductsResponse
import com.dapoer.rangoe.data.retrofit.response.ProfileResponse
import com.dapoer.rangoe.data.retrofit.response.RegisterResponse
import com.dapoer.rangoe.data.retrofit.response.ResendOtpResponse
import com.dapoer.rangoe.data.retrofit.response.UpdatePasswordResponse
import com.dapoer.rangoe.data.retrofit.response.UpdateProductResponse
import com.dapoer.rangoe.data.retrofit.response.UpdateTokoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("auth/otp-confirmation")
    fun OTP(
        @Field("code") code: Int,
        @Field("email") email: String?
    ): Call<OTPResponse>

    @FormUrlEncoded
    @POST("auth/otp-confirmation-resend")
    suspend fun resendOTP(
        @Field("email") email: String?
    ): ResendOtpResponse

    @FormUrlEncoded
    @POST("auth/forgot-password")
    suspend fun lupapassword(
        @Field("email") email: String
    ): LupaPasswordResponse

    @FormUrlEncoded
    @POST("auth/new-password")
    suspend fun newPassword(
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("reset_pass_token") otp: Int
    ): NewPassword2Response

    @GET("dashboard/products/")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Response<ProductsResponse>

    @GET("dashboard/bio-links")
    suspend fun getLinkyi(
        @Header("Authorization") token: String
    ): LinkyiResponse

    @GET("dashboard/bio-links/{id}")
    suspend fun getLinkyi(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): LinkyiDetailResponse

    @Multipart
    @POST("dashboard/products/create")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part file: MultipartBody.Part,
        @Part("is_active") isActive: RequestBody,
        @Part links: List<MultipartBody.Part>
    ): DeleteProductResponse

    @GET("dashboard/products/{id}")
    suspend fun getProductDetail(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): DetailProductResponse

    @DELETE("dashboard/products/delete/{id}")
    suspend fun deleteProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String
    ): DeleteProductResponse

    @FormUrlEncoded
    @POST("dashboard/products/{id}/link/create")
    suspend fun addProductLink(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Field("link") link: String
    ): DeleteProductResponse

    @DELETE("dashboard/products/{id}/link/delete/{link}")
    suspend fun deleteLinkProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Path("link") linkId: String
    ): DeleteProductResponse

    @FormUrlEncoded
    @POST("dashboard/bio-links/create")
    suspend fun addLinkyi(
        @Header("Authorization") token: String,
        @Field("link") link: String? = null,
        @Field("type") type: String,
        @Field("name") name: String,
        @Field("is_active") is_active: String
    ): AddLinkyiResponse

    @DELETE("dashboard/bio-links/delete/{id}")
    suspend fun deleteLinkyi(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AddLinkyiResponse

    @FormUrlEncoded
    @POST("profile/check-username")
    suspend fun checkUsername(
        @Header("Authorization") token: String,
        @Field("username") username: String
    ): CekUsernameResponse
    @Multipart
    @POST("profile/store-activation")
    suspend fun activateStore(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("description") description: RequestBody,
        @Part logo: MultipartBody.Part
    ): AktivasiTokoResponse

    @FormUrlEncoded
    @POST("profile/update-password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("current_password") currentPassword: String
    ): UpdatePasswordResponse

    @FormUrlEncoded
    @POST("dashboard/products/update-status/{id}")
    suspend fun productStatus(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Field("is_active") isActive: String
    ): ProductStatusResponse

    @FormUrlEncoded
    @POST("dashboard/bio-links/update-status/{id}")
    suspend fun LinkyiStatus(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Field("is_active") is_active: String
    ): AddLinkyiResponse

    @FormUrlEncoded
    @POST("dashboard/bio-links/update/{id}")
    suspend fun LinkyiUpdate(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Field("link") link: String? = null,
        @Field("name") name: String,
        @Field("is_active") is_active: String
    ): AddLinkyiResponse

    @Multipart
    @POST("dashboard/products/update/{id}")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Part("title") title: RequestBody,
        @Part("price") price: RequestBody,
        @Part("category") category: RequestBody,
        @Part thumbnail: MultipartBody.Part?
    ): UpdateProductResponse

    @Multipart
    @POST("dashboard/store/update")
    suspend fun updateStore(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody?,
        @Part logo: MultipartBody.Part?
    ): UpdateTokoResponse

    @GET("dashboard")
    suspend fun getAnalyzeStore(
        @Header("Authorization") token: String
    ): DashboardResponse

    @GET("profile")
    suspend fun getStoreProfile(
        @Header("Authorization") token: String
    ): ProfileResponse

    @GET("dashboard/product/category")
    suspend fun getCategory(
        @Header("Authorization") token: String
    ): KategoriResponse
    @FormUrlEncoded
    @POST("auth/check-email-available")
    suspend fun checkEmail(
        @Field("email") email: String
    ): CheckEmailResponse

    @Multipart
    @POST("predict")
    suspend fun predictImage(
        @Header("X-SECRET-KEY") secret: String,
        @Part file: MultipartBody.Part
    ): PredictionResponse
}