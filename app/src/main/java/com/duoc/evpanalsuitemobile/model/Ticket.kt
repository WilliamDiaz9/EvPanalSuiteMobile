package com.duoc.evpanalsuitemobile.model

data class TicketComment(
    val authorId: Int,
    val text: String,
    val createdAt: String
)

data class TicketHistoryEntry(
    val authorId: Int,
    val action: String,
    val createdAt: String
)

data class Ticket(
    val id: Int,
    val title: String,
    val description: String,
    val flowType: String,
    val category: String,
    val priority: Priority,
    val status: TicketStatus,
    val createdAt: String,
    val updatedAt: String,
    val branch: String,
    val requesterId: Int,
    val team: String,
    val assigneeId: Int?,
    val comments: List<TicketComment> = emptyList(),
    val history: List<TicketHistoryEntry> = emptyList()
)

