package com.example.data.staff

import android.util.Log
import com.example.data.util.ApiUtil
import com.example.domain.staff.model.StaffListResponse
import com.example.domain.staff.model.Staff
import com.example.domain.staff.repository.StaffRepository
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class StaffRepositoryImpl : StaffRepository {
    override suspend fun getStaff(page: Int): StaffListResponse {
        val url = URL("${ApiUtil.API_BASE_URL}${ApiUtil.STAFF_API_PATH}?page=$page")
        val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("x-api-key", "reqres-free-v1")

        if (conn.responseCode == HttpURLConnection.HTTP_OK) {
            val input = BufferedReader(InputStreamReader(conn.inputStream))
            val response = input.readText()
            input.close()

            val jsonResponse = JSONObject(response)
            val userList = mutableListOf<Staff>()
            val dataArray = jsonResponse.getJSONArray("data")

            for (i in 0 until dataArray.length()) {
                val userObject = dataArray.getJSONObject(i)
                userList.add(
                    Staff(
                        id = userObject.getInt("id"),
                        email = userObject.getString("email"),
                        firstName = userObject.getString("first_name"),
                        lastName = userObject.getString("last_name"),
                        avatar = userObject.getString("avatar")
                    )
                )
            }

            return StaffListResponse(
                page = jsonResponse.getInt("page"),
                perPage = jsonResponse.getInt("per_page"),
                total = jsonResponse.getInt("total"),
                totalPages = jsonResponse.getInt("total_pages"),
                data = userList
            )
        } else {
            throw Exception("Get staff list failed: ${conn.responseMessage} (Code: ${conn.responseCode})")
        }
    }
}