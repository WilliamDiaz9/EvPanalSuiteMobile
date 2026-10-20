package com.duoc.evpanalsuitemobile.model

fun Role.label(): String = when (this) {
    Role.REQUESTER -> "Solicitante"
    Role.RESOLVER -> "Resolutor"
    Role.SUPERVISOR -> "Supervisor"
    Role.ADMIN -> "Admin"
}

fun Role.canCreateTicket(): Boolean =
    this == Role.REQUESTER || this == Role.SUPERVISOR

fun Role.canViewDashboard(): Boolean =
    this == Role.SUPERVISOR || this == Role.ADMIN

fun Role.canAssign(): Boolean =
    this == Role.SUPERVISOR || this == Role.ADMIN

fun User.canView(ticket: Ticket): Boolean = when (role) {
    Role.REQUESTER -> ticket.requesterId == id
    Role.RESOLVER -> ticket.assigneeId == id || ticket.assigneeId == null
    Role.SUPERVISOR, Role.ADMIN -> true
}

fun User.canChangeStatus(ticket: Ticket): Boolean = when (role) {
    Role.REQUESTER -> false
    Role.RESOLVER -> ticket.assigneeId == id
    Role.SUPERVISOR, Role.ADMIN -> true
}

fun User.visibleTickets(tickets: List<Ticket>): List<Ticket> =
    tickets.filter { canView(it) }


