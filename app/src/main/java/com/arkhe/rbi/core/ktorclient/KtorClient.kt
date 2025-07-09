package com.arkhe.rbi.core.ktorclient

import com.arkhe.rbi.applications.payment.model.StatusRequest
import com.arkhe.rbi.applications.payment.model.StatusResponse
import com.arkhe.rbi.core.routes.HttpRoutesUrl
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    companion object {
        fun getClient(): HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 30000
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
            }

            install(DefaultRequest) {
                url {
                    host = HttpRoutesUrl.BASE_URL
                    protocol = URLProtocol.HTTP
                    contentType(ContentType.Application.Json)
                }
            }

            install(Logging) {
                logger = Logger.ANDROID
                level = LogLevel.ALL
            }
        }
    }

    suspend fun postGetStatus(statusRequest: StatusRequest): Result<StatusResponse> {
        return try {
            val response = getClient().post {
                url {
                    path(HttpRoutesUrl.PUBLIC_SUBSCRIBER)
                }
                setBody(statusRequest)
            }.body<StatusResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}