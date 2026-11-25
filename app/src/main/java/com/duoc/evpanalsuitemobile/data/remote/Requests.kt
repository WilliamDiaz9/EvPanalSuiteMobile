package com.duoc.evpanalsuitemobile.data.remote

import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.TicketStatus

// Datos que la app envia a los microservicios

data class LoginRequest(
    val username: String,
    val password: String
)

data class CreateTicketRequest(
    val title: String,
    val description: String,
    val category: String,
    val priority: Priority,
    val branch: String,
    val requesterId: Int
)

data class StatusRequest(
    val status: TicketStatus,
    val authorId: Int
)

data class AssignRequest(
    val assigneeId: Int,
    val authorId: Int
)

data class CommentRequest(
    val text: String,
    val authorId: Int
)
