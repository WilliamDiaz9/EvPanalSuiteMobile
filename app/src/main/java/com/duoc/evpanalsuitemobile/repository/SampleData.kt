package com.duoc.evpanalsuitemobile.repository

import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketComment
import com.duoc.evpanalsuitemobile.model.TicketHistoryEntry
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User

object SampleData {
    val users = listOf(
        User(4, "axel", "1234", "Axel Contreras", Role.ADMIN),
        User(3, "alexis", "1234", "Alexis Encalada", Role.SUPERVISOR),
        User(1, "benjamin", "1234", "Benjamin Parra", Role.REQUESTER),
        User(2, "joaquin", "1234", "Joaquin Diaz", Role.RESOLVER)
    )

    val tickets = listOf(
        Ticket(
            id = 101,
            title = "Caja 3 sin conexión",
            description = "La caja 3 no abre el sistema de ventas.",
            flowType = "Soporte interno",
            category = "Infraestructura",
            priority = Priority.HIGH,
            status = TicketStatus.IN_PROGRESS,
            createdAt = "2026-10-12 09:15",
            updatedAt = "2026-10-12 10:40",
            branch = "Tienda Centro",
            requesterId = 1,
            team = "Soporte TI",
            assigneeId = 2,
            comments = listOf(
                TicketComment(2, "Revisando el cableado.", "2026-10-12 10:40")
            ),
            history = listOf(
                TicketHistoryEntry(1, "Ticket creado", "2026-10-12 09:15"),
                TicketHistoryEntry(3, "Asignado a Joaquin Diaz", "2026-10-12 09:30")
            )
        ),
        Ticket(
            id = 102,
            title = "Stock distinto al real en farmacia",
            description = "El sistema muestra un stock distinto al que hay en bodega.",
            flowType = "Atención en tienda",
            category = "Inventario",
            priority = Priority.MEDIUM,
            status = TicketStatus.OPEN,
            createdAt = "2026-10-12 11:00",
            updatedAt = "2026-10-12 11:00",
            branch = "Farmacia Norte",
            requesterId = 1,
            team = "Soporte TI",
            assigneeId = null,
            history = listOf(
                TicketHistoryEntry(1, "Ticket creado", "2026-10-12 11:00")
            )
        ),
        Ticket(
            id = 103,
            title = "Impresora de etiquetas",
            description = "No imprime las etiquetas de precio.",
            flowType = "Soporte interno",
            category = "Hardware",
            priority = Priority.LOW,
            status = TicketStatus.RESOLVED,
            createdAt = "2026-10-10 15:20",
            updatedAt = "2026-10-11 09:00",
            branch = "Tienda Sur",
            requesterId = 1,
            team = "Soporte TI",
            assigneeId = 2,
            history = listOf(
                TicketHistoryEntry(1, "Ticket creado", "2026-10-10 15:20"),
                TicketHistoryEntry(2, "Estado: Resuelto", "2026-10-11 09:00")
            )
        ),
        Ticket(
            id = 104,
            title = "Proveedor no puede ingresar pedidos",
            description = "El portal de proveedores rechaza las órdenes nuevas.",
            flowType = "Proveedores",
            category = "Software",
            priority = Priority.CRITICAL,
            status = TicketStatus.IN_PROGRESS,
            createdAt = "2026-10-13 08:05",
            updatedAt = "2026-10-13 08:50",
            branch = "Oficina central",
            requesterId = 1,
            team = "Soporte TI",
            assigneeId = 2,
            comments = listOf(
                TicketComment(2, "Reproduje el error, escalando a desarrollo.", "2026-10-13 08:50")
            ),
            history = listOf(
                TicketHistoryEntry(1, "Ticket creado", "2026-10-13 08:05"),
                TicketHistoryEntry(3, "Asignado a Joaquin Diaz", "2026-10-13 08:10")
            )
        ),
        Ticket(
            id = 105,
            title = "Lector de código de barras",
            description = "El lector de la caja 1 lee códigos de forma intermitente.",
            flowType = "Atención en tienda",
            category = "Hardware",
            priority = Priority.MEDIUM,
            status = TicketStatus.ON_HOLD,
            createdAt = "2026-10-11 16:30",
            updatedAt = "2026-10-12 12:00",
            branch = "Tienda Sur",
            requesterId = 1,
            team = "Soporte TI",
            assigneeId = 2,
            comments = listOf(
                TicketComment(2, "Esperando repuesto del proveedor.", "2026-10-12 12:00")
            ),
            history = listOf(
                TicketHistoryEntry(1, "Ticket creado", "2026-10-11 16:30"),
                TicketHistoryEntry(2, "Estado: En espera", "2026-10-12 12:00")
            )
        ),
        Ticket(
            id = 106,
            title = "Alerta de temperatura en refrigerador",
            description = "El refrigerador de la farmacia marca una temperatura fuera de rango.",
            flowType = "Fallas de infraestructura",
            category = "Infraestructura",
            priority = Priority.HIGH,
            status = TicketStatus.OPEN,
            createdAt = "2026-10-13 09:20",
            updatedAt = "2026-10-13 09:20",
            branch = "Farmacia Norte",
            requesterId = 1,
            team = "Soporte TI",
            assigneeId = null,
            history = listOf(
                TicketHistoryEntry(1, "Ticket creado", "2026-10-13 09:20")
            )
        )
    )
}





