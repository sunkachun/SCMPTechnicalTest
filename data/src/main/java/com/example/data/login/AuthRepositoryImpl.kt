package com.example.data.login

import android.net.Uri
import android.util.Log
import com.example.data.util.ApiUtil
import com.example.domain.login.model.LoginRequest
import com.example.domain.login.model.LoginResponse
import com.example.domain.login.repository.AuthRepository
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.core.net.toUri

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(request: LoginRequest): LoginResponse {
        try {
            val uri = "${ApiUtil.API_BASE_URL}${ApiUtil.LOGIN_API_PATH}".toUri()
                .buildUpon()
                .appendQueryParameter("delay", "5")
                .build()
            val url = URL(uri.toString())
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("x-api-key", "reqres-free-v1")
            conn.doOutput = true

            val json = JSONObject().apply {
                put("email", request.email)
                put("password", request.password)
            }.toString()

            val os: OutputStream = conn.outputStream
            os.write(json.toByteArray())
            os.close()

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val input = BufferedReader(InputStreamReader(conn.inputStream))
                val response = input.readText()
                input.close()
                val jsonResponse = JSONObject(response)
                return LoginResponse(jsonResponse.getString("token"))
            } else {
                throw Exception("Login failed: ${conn.responseMessage} (Code: ${conn.responseCode})")
            }
        } catch (e: Exception) {
            return LoginResponse("", error = "error: $e")
        }
    }
}