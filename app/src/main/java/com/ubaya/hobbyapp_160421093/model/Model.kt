package com.ubaya.hobbyapp_160421093.model

import com.google.gson.annotations.SerializedName

data class Model(
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("direction")
    val direction: String?,
    @SerializedName("short_description")
    val shortDescription: String?,
    @SerializedName("full_description")
    val fullDescription: String?,
    @SerializedName("images")
    val images: String?,
)