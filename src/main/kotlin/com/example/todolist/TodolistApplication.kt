package com.example.todolist

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@SpringBootApplication
class TodolistApplication

fun main(args: Array<String>) {
	runApplication<TodolistApplication>(*args)
}

@Component
class StartupLogger(private val env: Environment) {
	private val log = LoggerFactory.getLogger(javaClass)

	@EventListener(ApplicationReadyEvent::class)
	fun logConfig() {
		log.info("=== Startup Config ===")
		log.info("Active profiles: ${env.activeProfiles.joinToString()}")
		log.info("DB URL: ${env.getProperty("spring.datasource.url")}")
		log.info("DB Username: ${env.getProperty("spring.datasource.username")}")
		log.info("GOOGLE_CLIENT_ID: ${env.getProperty("GOOGLE_CLIENT_ID")?.take(20) ?: "NOT SET"}...")
		log.info("AWS_ACCESS_KEY_ID: ${env.getProperty("AWS_ACCESS_KEY_ID")?.take(8) ?: "NOT SET"}...")
		log.info("AWS_REGION: ${env.getProperty("cloud.aws.region") ?: "NOT SET"}")
		log.info("S3_BUCKET: ${env.getProperty("cloud.aws.s3.bucket") ?: "NOT SET"}")
		log.info("JWT_SECRET set: ${!env.getProperty("jwt.secret").isNullOrBlank()}")
		log.info("======================")
	}
}
