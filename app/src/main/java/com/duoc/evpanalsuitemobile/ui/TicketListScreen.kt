package com.duoc.evpanalsuitemobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.model.canCreateTicket
import com.duoc.evpanalsuitemobile.model.canViewDashboard
import com.duoc.evpanalsuitemobile.model.label
import com.duoc.evpanalsuitemobile.repository.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    user: User,
    users: List<User>,
    tickets: List<Ticket>,
    userName: (Int?) -> String,
    onTicketClick: (Int) -> Unit,
    onCreateClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onUserSelected: (User) -> Unit
) {
    // Estado propio de esta pantalla: filtro por estado
    var selectedStatus by remember { mutableStateOf<TicketStatus?>(null) }
    val filtered = if (selectedStatus == null) {
        tickets
    } else {
        tickets.filter { it.status == selectedStatus }
    }

    var showUserPicker by remember { mutableStateOf(false) }

    if (showUserPicker) {
        UserPickerDialog(
            users = users,
            selectedUser = user,
            onDismiss = { showUserPicker = false },
            onConfirm = { selected ->
                onUserSelected(selected)
                showUserPicker = false
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        PanalMark(Modifier.size(36.dp))
                        Text("PanalSuite")
                    }
                },
                actions = {
                    TextButton(onClick = { showUserPicker = true }) {
                        RoleAvatar(user.role, Modifier.size(30.dp))
                        Text("Usuario", modifier = Modifier.padding(start = 6.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Hola, ${user.fullName}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("${user.role.label()} · ${filtered.size} tickets en tu bandeja", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (user.role.canCreateTicket()) {
                    Button(onClick = onCreateClick) { Text("Nuevo ticket") }
                }
                if (user.role.canViewDashboard()) {
                    OutlinedButton(onClick = onDashboardClick) { Text("Panel") }
                }
            }

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("Todos") }
                )
                TicketStatus.entries.forEach { status ->
                    FilterChip(
                        selected = selectedStatus == status,
                        onClick = { selectedStatus = status },
                        label = { Text(status.label) }
                    )
                }
            }

            if (filtered.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Todo en orden", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("No hay tickets para mostrar con este filtro.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.id }) { ticket ->
                        TicketCard(
                            ticket = ticket,
                            assigneeName = userName(ticket.assigneeId),
                            onClick = { onTicketClick(ticket.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TicketCard(
    ticket: Ticket,
    assigneeName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(text = "#${ticket.id} · ${ticket.title}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "${ticket.branch} · ${ticket.category}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Estado: ${ticket.status.label} · Prioridad: ${ticket.priority.label}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Responsable: $assigneeName",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicketListScreenPreview() {
    TicketListScreen(
        user = User(3, "supervisor", "1234", "Alexis Encalada", Role.SUPERVISOR),
        users = SampleData.users,
        tickets = SampleData.tickets,
        userName = { id -> SampleData.users.firstOrNull { it.id == id }?.fullName ?: "Sin asignar" },
        onTicketClick = {},
        onCreateClick = {},
        onDashboardClick = {},
        onUserSelected = {}
    )
}






