package com.arkhe.rbi.data.remote

import com.arkhe.rbi.data.remote.dto.AuthResponse
import com.arkhe.rbi.data.remote.dto.LoginRequest
import com.arkhe.rbi.data.remote.dto.RegisterRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiClient(private val client: HttpClient) {
    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = client.post("/api/v1/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<AuthResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest, authToken: String): Result<AuthResponse> {
        return try {
            val response = client.post("/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                header("X-Authorization", authToken)
                setBody(request)
            }.body<AuthResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}