package com.duoc.evpanalsuitemobile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.evpanalsuitemobile.model.Indicator
import com.duoc.evpanalsuitemobile.repository.ProviderStatus
import com.duoc.evpanalsuitemobile.viewmodel.ProviderStatusViewModel

// Pantalla conectada al ViewModel
@Composable
fun ProviderStatusScreen(
    onBack: () -> Unit,
    viewModel: ProviderStatusViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    ProviderStatusContent(
        isLoading = state.isLoading,
        items = state.items,
        onRefresh = { viewModel.load() },
        onBack = onBack
    )
}

// Pantalla solo de dibujo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProviderStatusContent(
    isLoading: Boolean,
    items: List<ProviderStatus>,
    onRefresh: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estado de proveedores") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Volver") } }
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
            Text(
                "Antes de abrir un ticket, revisa si la caída es del proveedor.",
                style = MaterialTheme.typography.bodyMedium
            )

            if (isLoading) {
                CircularProgressIndicator()
            }

            items.forEach { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IndicatorDot(item.indicator, label = statusText(item.indicator))
                    }
                }
            }

            Button(onClick = onRefresh, enabled = !isLoading) { Text("Actualizar") }
            Text(
                "Datos de las páginas de estado públicas de cada proveedor",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// Texto en español según el color
private fun statusText(indicator: Indicator): String = when (indicator) {
    Indicator.GREEN -> "Operativo"
    Indicator.YELLOW -> "Falla parcial"
    Indicator.RED -> "Caída del servicio"
    Indicator.GRAY -> "Sin información"
}