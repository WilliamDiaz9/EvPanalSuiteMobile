package com.duoc.evpanalsuitemobile.repository

import com.duoc.evpanalsuitemobile.data.remote.AssignRequest
import com.duoc.evpanalsuitemobile.data.remote.CommentRequest
import com.duoc.evpanalsuitemobile.data.remote.CreateTicketRequest
import com.duoc.evpanalsuitemobile.data.remote.StatusRequest
import com.duoc.evpanalsuitemobile.data.remote.TicketsApiService
import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.HttpException

// Habla con el microservicio de tickets y guarda la lista actual en memoria
class TicketRepository(private val api: TicketsApiService) {

    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets.asStateFlow()

    // Lee todos los tickets del servidor
    suspend fun refresh() {
        _tickets.value = api.getTickets()
    }

    // Crea un ticket y lo devuelve con el id que le puso el servidor
    suspend fun create(
        title: String,
        description: String,
        category: String,
        priority: Priority,
        branch: String,
        requester: User
    ): Ticket {
        val created = api.createTicket(
            CreateTicketRequest(title, description, category, priority, branch, requester.id)
        )
        _tickets.update { it + created }
        return created
    }

    suspend fun updateStatus(ticketId: Int, status: TicketStatus, author: User) {
        replace(api.changeStatus(ticketId, StatusRequest(status, author.id)))
    }

    suspend fun assign(ticketId: Int, assignee: User, author: User) {
        replace(api.assign(ticketId, AssignRequest(assignee.id, author.id)))
    }

    suspend fun addComment(ticketId: Int, text: String, author: User) {
        replace(api.addComment(ticketId, CommentRequest(text, author.id)))
    }

    suspend fun deleteComment(ticketId: Int, commentId: Int, user: User) {
        replace(api.deleteComment(ticketId, commentId, user.id))
    }

    suspend fun deleteTicket(ticketId: Int, user: User) {
        val response = api.deleteTicket(ticketId, user.id)
        if (!response.isSuccessful) throw HttpException(response)
        _tickets.update { list -> list.filter { it.id != ticketId } }
    }

    // Cambia en la lista el ticket que devolvio el servidor
    private fun replace(updated: Ticket) {
        _tickets.update { list -> list.map { if (it.id == updated.id) updated else it } }
    }
}
