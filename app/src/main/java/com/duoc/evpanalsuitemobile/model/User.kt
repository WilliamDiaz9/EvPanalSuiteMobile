package com.duoc.evpanalsuitemobile.model

data class User(
    val id: Int,
    val username: String,
    val password: String,
    val fullName: String,
    val role: Role
)

