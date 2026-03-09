package com.example.todolist.upload

import com.example.todolist.common.exception.CustomException
import com.example.todolist.common.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.UUID

@Service
class S3UploadService(
    private val s3Client: S3Client,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String,
    @Value("\${cloud.aws.s3.endpoint}") private val endpoint: String
) {
    private val allowedContentTypes = setOf(
        "image/jpeg", "image/png", "image/gif", "image/webp"
    )

    fun uploadImage(userId: Long, file: MultipartFile): UploadResponse {
        val contentType = file.contentType
            ?: throw CustomException(ErrorCode.INVALID_FILE_TYPE)

        if (contentType !in allowedContentTypes) {
            throw CustomException(ErrorCode.INVALID_FILE_TYPE)
        }

        val ext = when (contentType) {
            "image/jpeg" -> "jpg"
            "image/png" -> "png"
            "image/gif" -> "gif"
            "image/webp" -> "webp"
            else -> throw CustomException(ErrorCode.INVALID_FILE_TYPE)
        }

        val key = "uploads/$userId/${UUID.randomUUID()}.$ext"

        try {
            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType)
                    .build(),
                RequestBody.fromInputStream(file.inputStream, file.size)
            )
        } catch (e: Exception) {
            throw CustomException(ErrorCode.FILE_UPLOAD_FAILED)
        }

        val url = "$endpoint/$key"

        return UploadResponse(
            url = url,
            fileName = file.originalFilename ?: "image.$ext",
            fileSize = file.size
        )
    }
}
