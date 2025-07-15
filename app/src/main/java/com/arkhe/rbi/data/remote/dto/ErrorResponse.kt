package com.arkhe.rbi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
    val url: String,
    val status: String
)