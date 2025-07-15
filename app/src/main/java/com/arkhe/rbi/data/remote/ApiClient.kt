package com.arkhe.rbi.data.remote

import com.arkhe.rbi.data.remote.dto.AuthResponse
import com.arkhe.rbi.data.remote.dto.ErrorResponse
import com.arkhe.rbi.data.remote.dto.LoginRequest
import com.arkhe.rbi.data.remote.dto.RegisterRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ApiClient(private val client: HttpClient) {
    suspend fun register(request: RegisterRequest): Result<AuthResponse> {
        return try {
            val response = client.post("/api/v1/auth/register") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            val responseBody = response.bodyAsText()

            val jsonElement = Json.parseToJsonElement(responseBody)
            val jsonObject = jsonElement.jsonObject
            val status = jsonObject["status"]?.jsonPrimitive?.content

            if (status == "success" && jsonObject.containsKey("data")) {
                val authResponse = Json.decodeFromJsonElement<AuthResponse>(jsonElement)
                Result.success(authResponse)
            } else {
                val errorResponse = Json.decodeFromJsonElement<ErrorResponse>(jsonElement)
                Result.failure(Exception(errorResponse.message))
            }
        } catch (e: Exception) {
            println("Error during register ApiClient: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest, authToken: String): Result<AuthResponse> {
        return try {
            val response = client.post("/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                header("X-Authorization", authToken)
                setBody(request)
            }
            val responseBody = response.bodyAsText()

            val jsonElement = Json.parseToJsonElement(responseBody)
            val jsonObject = jsonElement.jsonObject
            val status = jsonObject["status"]?.jsonPrimitive?.content

            if (status == "success" && jsonObject.containsKey("data")) {
                val authResponse = Json.decodeFromJsonElement<AuthResponse>(jsonElement)
                Result.success(authResponse)
            } else {
                val errorResponse = Json.decodeFromJsonElement<ErrorResponse>(jsonElement)
                Result.failure(Exception(errorResponse.message))
            }
        } catch (e: Exception) {
            println("Error during login ApiClient: ${e.message}")
            Result.failure(e)
        }
    }
}