package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.evpanalsuitemobile.data.remote.toUserMessage
import com.duoc.evpanalsuitemobile.di.AppModule
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.repository.AuthRepository
import com.duoc.evpanalsuitemobile.repository.TicketRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TicketViewModel(
    private val ticketRepository: TicketRepository = AppModule.ticketRepository,
    private val authRepository: AuthRepository = AppModule.authRepository
) : ViewModel() {

    val tickets: StateFlow<List<Ticket>> = ticketRepository.tickets
    val users: StateFlow<List<User>> = authRepository.users

    // Solo los usuarios con rol RESOLVER
    val resolvers: StateFlow<List<User>> = users
        .map { list -> list.filter { it.role == Role.RESOLVER } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Mensaje de error para mostrar en pantalla
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun clearMessage() {
        _message.value = null
    }

    fun userName(id: Int?): String =
        users.value.firstOrNull { it.id == id }?.fullName ?: "Sin asignar"

    // Vuelve a pedir usuarios y tickets al servidor
    fun refresh() {
        launchRequest { authRepository.loadUsers() }
        launchRequest { ticketRepository.refresh() }
    }

    fun updateStatus(ticketId: Int, status: TicketStatus, author: User) {
        launchRequest { ticketRepository.updateStatus(ticketId, status, author) }
    }

    fun assign(ticketId: Int, assignee: User, author: User) {
        launchRequest { ticketRepository.assign(ticketId, assignee, author) }
    }

    fun addComment(ticketId: Int, text: String, author: User) {
        launchRequest { ticketRepository.addComment(ticketId, text, author) }
    }

    fun deleteComment(ticketId: Int, commentId: Int, user: User) {
        launchRequest { ticketRepository.deleteComment(ticketId, commentId, user) }
    }

    // onDone se llama solo si se elimino bien
    fun deleteTicket(ticketId: Int, user: User, onDone: () -> Unit) {
        launchRequest {
            ticketRepository.deleteTicket(ticketId, user)
            onDone()
        }
    }

    // Ejecuta una llamada al servidor y guarda el error si falla
    private fun launchRequest(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _message.value = e.toUserMessage()
            }
        }
    }
}
