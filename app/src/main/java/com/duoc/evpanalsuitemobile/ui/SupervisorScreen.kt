package com.duoc.evpanalsuitemobile.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.Ticket
import com.duoc.evpanalsuitemobile.model.TicketStatus
import com.duoc.evpanalsuitemobile.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupervisorScreen(
    tickets: List<Ticket>,
    resolvers: List<User>,
    onBack: () -> Unit
) {
    val total = tickets.size
    val unassigned = tickets.count { it.assigneeId == null }
    val critical = tickets.count { it.priority == Priority.CRITICAL }
    val finished = setOf(TicketStatus.RESOLVED, TicketStatus.CLOSED)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de seguimiento") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Volver") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Resumen del equipo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("$total", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text("tickets en seguimiento", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("$unassigned sin responsable  ·  $critical críticos", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            HorizontalDivider()
            Text("Por estado", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            TicketStatus.entries.forEach { status ->
                val count = tickets.count { it.status == status }
                CountBar(label = status.label, count = count, total = total)
            }

            HorizontalDivider()
            Text("Carga por resolutor (tickets activos)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            resolvers.forEach { resolver ->
                val active = tickets.count {
                    it.assigneeId == resolver.id && it.status !in finished
                }
                CountBar(label = resolver.fullName, count = active, total = total)
            }
        }
    }
}

@Composable
fun CountBar(label: String, count: Int, total: Int) {
    val fraction = if (total == 0) 0f else count.toFloat() / total.toFloat()
    val animatedFraction by animateFloatAsState(targetValue = fraction, label = "countBar")

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        LinearProgressIndicator(
            progress = { animatedFraction },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

