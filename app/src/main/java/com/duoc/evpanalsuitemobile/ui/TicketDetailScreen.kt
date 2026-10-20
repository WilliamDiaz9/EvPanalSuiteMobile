package com.duoc.evpanalsuitemobile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.model.canAssign
import com.duoc.evpanalsuitemobile.model.canChangeStatus
import com.duoc.evpanalsuitemobile.model.canView
import com.duoc.evpanalsuitemobile.util.simulatedLocation
import com.duoc.evpanalsuitemobile.viewmodel.TicketViewModel

// Esta función conoce al ViewModel
@Composable
fun TicketDetailScreen(
    ticketId: Int,
    user: User,
    ticketViewModel: TicketViewModel,
    onBack: () -> Unit
) {
    val tickets by ticketViewModel.tickets.collectAsState()
    val ticket = tickets.firstOrNull { it.id == ticketId }

    TicketDetailContent(
        ticket = ticket,
        user = user,
        resolvers = ticketViewModel.resolvers,
        userName = { id -> ticketViewModel.userName(id) },
        onStatusChange = { status -> ticketViewModel.updateStatus(ticketId, status, user) },
        onAssign = { resolver -> ticketViewModel.assign(ticketId, resolver, user) },
        onAddComment = { text -> ticketViewModel.addComment(ticketId, text, user) },
        onBack = onBack
    )
}

// Esta función NO conoce al ViewModel: solo dibuja lo que recibe
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailContent(
    ticket: Ticket?,
    user: User,
    resolvers: List<User>,
    userName: (Int?) -> String,
    onStatusChange: (TicketStatus) -> Unit,
    onAssign: (User) -> Unit,
    onAddComment: (String) -> Unit,
    onBack: () -> Unit
) {
    // Estado propio de esta pantalla
    var commentText by remember { mutableStateOf("") }
    var commentError by remember { mutableStateOf<String?>(null) }
    var showHistory by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (ticket != null) "Ticket #${ticket.id}" else "Ticket") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        if (ticket == null || !user.canView(ticket)) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("No tienes acceso a este ticket o no existe.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Ticket #${ticket.id} · ${ticket.status.label}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                        Text(ticket.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(ticket.description, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }

                HorizontalDivider()

                InfoRow("Estado", ticket.status.label)
                InfoRow("Prioridad", ticket.priority.label)
                InfoRow("Categoría", ticket.category)
                InfoRow("Tipo de flujo", ticket.flowType)
                InfoRow("Sucursal", ticket.branch)
                InfoRow("Ubicación (simulada)", simulatedLocation(ticket.branch))
                InfoRow("Equipo", ticket.team)
                InfoRow("Solicitante", userName(ticket.requesterId))
                InfoRow("Responsable", userName(ticket.assigneeId))
                InfoRow("Creado", ticket.createdAt)
                InfoRow("Actualizado", ticket.updatedAt)

                if (user.canChangeStatus(ticket)) {
                    HorizontalDivider()
                    Text("Cambiar estado", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TicketStatus.entries
                            .filter { it != ticket.status }
                            .forEach { status ->
                                OutlinedButton(onClick = { onStatusChange(status) }) {
                                    Text(status.label)
                                }
                            }
                    }
                }

                if (user.role.canAssign()) {
                    HorizontalDivider()
                    Text("Asignar a", fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        resolvers.forEach { resolver ->
                            OutlinedButton(
                                onClick = { onAssign(resolver) },
                                enabled = ticket.assigneeId != resolver.id
                            ) {
                                Text(resolver.fullName)
                            }
                        }
                    }
                }

                HorizontalDivider()
                Text("Comentarios (${ticket.comments.size})", fontWeight = FontWeight.Bold)
                if (ticket.comments.isEmpty()) {
                    Text("Sin comentarios", style = MaterialTheme.typography.bodySmall)
                } else {
                    ticket.comments.forEach { comment ->
                        Column {
                            Text(
                                text = "${userName(comment.authorId)} · ${comment.createdAt}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(comment.text)
                        }
                    }
                }

                OutlinedTextField(
                    value = commentText,
                    onValueChange = {
                        commentText = it
                        commentError = null
                    },
                    label = { Text("Nuevo comentario") },
                    isError = commentError != null,
                    supportingText = { commentError?.let { Text(it) } },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (commentText.isBlank()) {
                            commentError = "Escribe un comentario"
                        } else {
                            onAddComment(commentText.trim())
                            commentText = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar comentario")
                }

                HorizontalDivider()
                TextButton(onClick = { showHistory = !showHistory }) {
                    Text(
                        if (showHistory) "Ocultar historial"
                        else "Ver historial (${ticket.history.size})"
                    )
                }
                AnimatedVisibility(visible = showHistory) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        ticket.history.forEach { entry ->
                            Text(
                                text = "${entry.createdAt} · ${userName(entry.authorId)}: ${entry.action}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(text = value, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
    }
}

