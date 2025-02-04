package com.example.linkyishop.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class OTPResponse(

	@field:SerializedName("data")
	val data: OTPResult? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class OTPResult(
	@field:SerializedName("token")
	val token: String? = null
)
