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

    suspend fun hasRegisteredUser(): Boolean = userDao.hasRegisteredUser()

    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        val result = apiClient.register(request)
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
        return result
    }

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        // Get registered user to get the auth token
        val registeredUser = userDao.getUserById(username)
        if (registeredUser == null) {
            return Result.failure(Exception("User not registered"))
        }

        val loginRequest = LoginRequest(username, password)
        val result = apiClient.login(loginRequest, registeredUser.id)

        if (result.isSuccess) {
            val response = result.getOrNull()
            response?.let { authResponse ->
                // Update user with login data
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