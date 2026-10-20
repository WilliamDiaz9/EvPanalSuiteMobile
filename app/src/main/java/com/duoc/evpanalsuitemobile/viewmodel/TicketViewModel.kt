package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.repository.AuthRepository
import com.duoc.evpanalsuitemobile.repository.TicketRepository
import kotlinx.coroutines.flow.StateFlow

class TicketViewModel : ViewModel() {

    val tickets: StateFlow<List<Ticket>> = TicketRepository.tickets

    val resolvers: List<User> = AuthRepository.resolvers()

    fun userName(id: Int?): String = AuthRepository.userName(id)

    fun updateStatus(ticketId: Int, status: TicketStatus, author: User) {
        TicketRepository.updateStatus(ticketId, status, author)
    }

    fun assign(ticketId: Int, assignee: User, author: User) {
        TicketRepository.assign(ticketId, assignee, author)
    }

    fun addComment(ticketId: Int, text: String, author: User) {
        TicketRepository.addComment(ticketId, text, author)
    }
}

