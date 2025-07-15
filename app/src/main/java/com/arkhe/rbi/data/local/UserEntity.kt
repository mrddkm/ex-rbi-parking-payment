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
) {
    companion object {
        fun fakeUserEntity() = UserEntity(
            id = "DISHUB02",
            userId = "user123",
            email = "user@example.com",
            name = "Kolektor DISHUB 02",
            avatar = "https://example.com/avatar.jpg",
            phone = "08123456789",
            token = "84e66d73-f472-4975-bed0-7282919ece92",
            qris = "00020101021126610017ID.CO.BANKBJB.WWW0118936001103001278321020713436290303UMI51440014ID.CO.QRIS.WWW0215ID10221796982980303UMI5204546253033605802ID5919PARKIR BERLANGGANAN6007CIANJUR61054321162070703A016304F00C",
            imei = "865150034684065",
            status = "active",
            createdDate = "2024-06-01",
            institutionName = "Dinas Perhubungan Kabupaten Cianjur",
            isRegistered = true,
            isLoggedIn = true
        )
    }
}