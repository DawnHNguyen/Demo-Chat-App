package com.example.core.data.remote.dto.response


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("apple_id")
    val appleId: Any,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("birthday")
    val birthday: String,
    @SerializedName("check_login_first")
    val checkLoginFirst: Int,
    @SerializedName("check_user")
    val checkUser: Any,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("deleted_at")
    val deletedAt: Any,
    @SerializedName("email")
    val email: String,
    @SerializedName("facebook_id")
    val facebookId: Any,
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("google_id")
    val googleId: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login_type")
    val loginType: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("nurse_id")
    val nurseId: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("typeAccount")
    val typeAccount: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("zalo_id")
    val zaloId: Any
)