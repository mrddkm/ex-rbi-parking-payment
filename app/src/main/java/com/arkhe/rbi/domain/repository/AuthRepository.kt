package com.arkhe.rbi.domain.repository

import com.arkhe.rbi.data.local.UserEntity
import com.arkhe.rbi.data.local.dao.UserDao
import com.arkhe.rbi.data.remote.ApiClient
import com.arkhe.rbi.data.remote.dto.AuthResponse
import com.arkhe.rbi.data.remote.dto.LoginRequest
import com.arkhe.rbi.data.remote.dto.RegisterRequest
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val apiClient: ApiClient,
    private val userDao: UserDao
) {
    fun getCurrentUser(): Flow<UserEntity?> = userDao.getCurrentUser()

    suspend fun getUser(): String = userDao.getUser()

    suspend fun hasRegisteredUser(): Boolean = userDao.hasRegisteredUser()

    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        val result = apiClient.register(request)
        try {
            if (result.isSuccess) {
                val response = result.getOrNull()
                response?.let { authResponse ->
                    val userEntity = UserEntity(
                        id = authResponse.data.id,
                        userId = authResponse.data.userId ?: "",
                        email = authResponse.data.user?.email ?: "",
                        name = authResponse.data.user?.name ?: "",
                        avatar = authResponse.data.user?.avatar ?: "",
                        phone = authResponse.data.phone ?: "",
                        token = authResponse.data.token,
                        qris = authResponse.data.additionals?.qris ?: "",
                        imei = authResponse.data.imei ?: "",
                        status = authResponse.data.status,
                        createdDate = authResponse.data.createdDate,
                        institutionName = authResponse.data.user?.institution?.name ?: "",
                        isRegistered = true,
                        isLoggedIn = false
                    )
                    userDao.insertUser(userEntity)
                }
            }
        } catch (e: Exception) {
            print("Error during registration: ${e.message}")
            return Result.failure(e)
        }
        return result
    }

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        val registeredUser = userDao.getUserById(username)
        println("Registered user: $registeredUser")
        if (registeredUser == null) {
            return Result.failure(Exception("User not registered"))
        }

        val loginRequest = LoginRequest(username, password)
        val result = apiClient.login(loginRequest, registeredUser.id)

        if (result.isSuccess) {
            val response = result.getOrNull()
            response?.let { authResponse ->
                val updatedUser = registeredUser.copy(
                    token = authResponse.data.token,
                    qris = authResponse.data.qris ?: registeredUser.qris,
                    isLoggedIn = true
                )
                userDao.logoutAllUsers() // Logout all users first
                userDao.updateUser(updatedUser)
            }
        }
        return result
    }

    suspend fun logout() {
        userDao.logoutAllUsers()
    }
}