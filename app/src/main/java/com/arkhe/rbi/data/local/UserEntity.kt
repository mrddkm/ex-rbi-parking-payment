package com.arkhe.rbi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val email: String,
    val name: String,
    val avatar: String,
    val phone: String,
    val token: String,
    val qris: String,
    val imei: String,
    val status: String,
    val createdDate: String,
    val institutionName: String,
    val isRegistered: Boolean = false,
    val isLoggedIn: Boolean = false
)