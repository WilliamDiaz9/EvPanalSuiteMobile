package com.duoc.evpanalsuitemobile.repository

import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketComment
import com.duoc.evpanalsuitemobile.model.TicketHistoryEntry
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.util.nowString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object TicketRepository {

    private val _tickets = MutableStateFlow(SampleData.tickets)
    val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

    fun create(
        title: String,
        description: String,
        category: String,
        priority: Priority,
        branch: String,
        requester: User
    ): Int {
        val now = nowString()
        val newId = (_tickets.value.maxOfOrNull { it.id } ?: 100) + 1
        val ticket = Ticket(
            id = newId,
            title = title,
            description = description,
            flowType = "Soporte interno",
            category = category,
            priority = priority,
            status = TicketStatus.OPEN,
            createdAt = now,
            updatedAt = now,
            branch = branch,
            requesterId = requester.id,
            team = "Soporte TI",
            assigneeId = null,
            history = listOf(TicketHistoryEntry(requester.id, "Ticket creado", now))
        )
        _tickets.update { it + ticket }
        return newId
    }

    fun updateStatus(ticketId: Int, status: TicketStatus, author: User) {
        modify(ticketId) { ticket, now ->
            ticket.copy(
                status = status,
                updatedAt = now,
                history = ticket.history +
                    TicketHistoryEntry(author.id, "Estado: ${status.label}", now)
            )
        }
    }

    fun assign(ticketId: Int, assignee: User, author: User) {
        modify(ticketId) { ticket, now ->
            ticket.copy(
                assigneeId = assignee.id,
                status = if (ticket.status == TicketStatus.OPEN) TicketStatus.IN_PROGRESS else ticket.status,
                updatedAt = now,
                history = ticket.history +
                    TicketHistoryEntry(author.id, "Asignado a ${assignee.fullName}", now)
            )
        }
    }

    fun addComment(ticketId: Int, text: String, author: User) {
        modify(ticketId) { ticket, now ->
            ticket.copy(
                updatedAt = now,
                comments = ticket.comments + TicketComment(author.id, text, now),
                history = ticket.history +
                    TicketHistoryEntry(author.id, "Comentario agregado", now)
            )
        }
    }

    private fun modify(ticketId: Int, transform: (Ticket, String) -> Ticket) {
        val now = nowString()
        _tickets.update { list ->
            list.map { if (it.id == ticketId) transform(it, now) else it }
        }
    }
}

