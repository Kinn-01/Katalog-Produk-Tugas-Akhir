package com.dapoer.rangoe.data.retrofit.response

import com.google.gson.annotations.SerializedName

data class NewPassword2Response(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
