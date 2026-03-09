package com.example.todolist.common.response

data class ApiResponse<T>(
    val data: T?,
    val message: String,
    val code: String? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> =
            ApiResponse(data = data, message = "success")

        fun error(message: String, code: String): ApiResponse<Nothing> =
            ApiResponse(data = null, message = message, code = code)
    }
}
