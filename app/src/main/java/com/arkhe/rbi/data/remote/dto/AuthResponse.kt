package com.arkhe.rbi.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val data: AuthData,
    val message: String,
    val url: String,
    val status: String
)

@Serializable
data class AuthData(
    val id: String,
    val userId: String? = null,
    val user: User? = null,
    val email: String? = null,
    val name: String? = null,
    val avatar: String? = null,
    val phone: String? = null,
    val phoneLabel: String? = null,
    val imei: String? = null,
    val brand: String? = null,
    val type: String? = null,
    val osName: String? = null,
    val osVersion: String? = null,
    val token: String,
    val activationCode: String? = null,
    val status: String,
    val statusLabel: String,
    val createdDate: String,
    val createdBy: String,
    val updatedDate: String,
    val updatedBy: String? = null,
    val deviceLabel: String? = null,
    val additionals: Additionals? = null,
    val institution: Institution? = null,
    val roles: Map<String, String>? = null,
    val qris: String? = null
)

@Serializable
data class User(
    val id: String,
    val institution: Institution,
    val email: String,
    val name: String,
    val avatar: String,
    val status: String,
    val statusLabel: String,
    val createdDate: String,
    val createdBy: String,
    val updatedDate: String,
    val roles: Map<String, String>
)

@Serializable
data class Institution(
    val id: String,
    val nickname: String,
    val name: String,
    val avatar: String,
    val status: String,
    val statusLabel: String,
    val createdDate: String,
    val createdBy: String,
    val updatedDate: String
)

@Serializable
data class Additionals(
    val imei: String? = null,
    val qris: String? = null,
)