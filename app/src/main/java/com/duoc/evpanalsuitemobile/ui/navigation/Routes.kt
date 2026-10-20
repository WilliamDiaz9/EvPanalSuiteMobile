package com.duoc.evpanalsuitemobile.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val TICKETS = "tickets"
    const val TICKET_DETAIL = "ticketDetail/{ticketId}"
    const val CREATE_TICKET = "createTicket"
    const val SUPERVISOR = "supervisor"

    fun ticketDetail(ticketId: Int) = "ticketDetail/$ticketId"
}

