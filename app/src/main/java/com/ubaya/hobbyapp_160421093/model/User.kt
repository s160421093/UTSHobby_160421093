package com.ubaya.hobbyapp_160421093.model
import com.google.gson.annotations.SerializedName

data class User (
    val id:String?,
    @SerializedName("username")
    val username:String?,
    @SerializedName("first_name")
    val firstName:String?,
    @SerializedName("last_name")
    val lastName:String?,
    val email:String?,
    val password:String?,
)
