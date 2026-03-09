package com.example.todolist.upload

data class UploadResponse(
    val url: String,
    val fileName: String,
    val fileSize: Long
)
