package com.arkhe.rbi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val userId: String,
    val phone: String,
    val imei: String,
    val brand: String,
    val type: String,
    val osVersion: String,
    val osName: String
)