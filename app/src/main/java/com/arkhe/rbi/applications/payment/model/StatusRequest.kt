package com.arkhe.rbi.applications.payment.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusRequest(
    val id: String? = null,
)