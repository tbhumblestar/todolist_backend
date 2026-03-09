package com.example.todolist.upload

import com.example.todolist.common.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/uploads")
class UploadController(
    private val s3UploadService: S3UploadService
) {
    @PostMapping("/image")
    fun uploadImage(
        authentication: Authentication,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ApiResponse<UploadResponse>> {
        val userId = authentication.principal as Long
        return ResponseEntity.ok(ApiResponse.success(s3UploadService.uploadImage(userId, file)))
    }
}
