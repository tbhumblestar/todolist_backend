package com.example.todolist.common.exception

class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)
