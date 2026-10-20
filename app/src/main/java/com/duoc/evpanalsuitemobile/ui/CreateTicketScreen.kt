package com.duoc.evpanalsuitemobile.ui

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.viewmodel.CreateTicketUiState
import com.duoc.evpanalsuitemobile.viewmodel.CreateTicketViewModel

// Esta función conoce al ViewModel
@Composable
fun CreateTicketScreen(
    user: User,
    viewModel: CreateTicketViewModel,
    onBack: () -> Unit,
    onCreated: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    CreateTicketContent(
        state = state,
        categories = viewModel.categories,
        branches = viewModel.branches,
        onTitleChange = { viewModel.onTitleChange(it) },
        onDescriptionChange = { viewModel.onDescriptionChange(it) },
        onCategoryChange = { viewModel.onCategoryChange(it) },
        onBranchChange = { viewModel.onBranchChange(it) },
        onPriorityChange = { viewModel.onPriorityChange(it) },
        onSubmit = { viewModel.submit(user, onCreated) },
        onBack = onBack
    )
}

// Esta función NO conoce al ViewModel: solo dibuja lo que recibe
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTicketContent(
    state: CreateTicketUiState,
    categories: List<String>,
    branches: List<String>,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onBranchChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo ticket") },
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
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Cuéntanos qué necesitas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("Completa los datos para que el equipo pueda ayudarte.", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            OutlinedTextField(
                value = state.title,
                onValueChange = onTitleChange,
                label = { Text("Título") },
                singleLine = true,
                isError = state.titleError != null,
                supportingText = { state.titleError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = onDescriptionChange,
                label = { Text("Descripción") },
                minLines = 3,
                isError = state.descriptionError != null,
                supportingText = { state.descriptionError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            ChoiceRow(
                options = categories,
                selected = state.category,
                onSelect = onCategoryChange
            )
            state.categoryError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Text("Sucursal", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            ChoiceRow(
                options = branches,
                selected = state.branch,
                onSelect = onBranchChange
            )
            state.branchError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Text("Prioridad", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            ChoiceRow(
                options = Priority.entries.map { it.label },
                selected = state.priority.label,
                onSelect = { label ->
                    onPriorityChange(Priority.entries.first { it.label == label })
                }
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear ticket")
            }
        }
    }
}

@Composable
fun ChoiceRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option) }
            )
        }
    }
}

