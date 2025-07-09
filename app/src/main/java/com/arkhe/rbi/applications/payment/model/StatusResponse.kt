package com.arkhe.rbi.applications.payment.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusResponse(
    val status: String? = null,
    val message: String? = null
)