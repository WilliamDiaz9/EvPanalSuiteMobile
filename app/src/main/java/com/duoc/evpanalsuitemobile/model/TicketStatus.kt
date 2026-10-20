package com.duoc.evpanalsuitemobile.model

enum class TicketStatus(val label: String) {
    OPEN("Abierto"),
    IN_PROGRESS("En curso"),
    ON_HOLD("En espera"),
    RESOLVED("Resuelto"),
    CLOSED("Cerrado")
}

