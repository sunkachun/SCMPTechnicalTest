package com.example.data.login

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

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(request: LoginRequest): LoginResponse {
        val url = URL("${ApiUtil.API_BASE_URL}${ApiUtil.LOGIN_API_PATH}?delay=5")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("Content-Type", "application/json")
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
    }
}