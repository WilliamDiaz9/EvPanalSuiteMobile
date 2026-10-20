package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.evpanalsuitemobile.model.Priority
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateTicketUiState(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val branch: String = "",
    val priority: Priority = Priority.MEDIUM,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val categoryError: String? = null,
    val branchError: String? = null
)

class CreateTicketViewModel : ViewModel() {

    val categories = listOf("Infraestructura", "Inventario", "Hardware", "Software", "Otro")
    val branches = listOf("Tienda Centro", "Tienda Sur", "Farmacia Norte", "Oficina central")

    private val _uiState = MutableStateFlow(CreateTicketUiState())
    val uiState: StateFlow<CreateTicketUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) {
        _uiState.update { it.copy(title = value, titleError = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value, descriptionError = null) }
    }

    fun onCategoryChange(value: String) {
        _uiState.update { it.copy(category = value, categoryError = null) }
    }

    fun onBranchChange(value: String) {
        _uiState.update { it.copy(branch = value, branchError = null) }
    }

    fun onPriorityChange(value: Priority) {
        _uiState.update { it.copy(priority = value) }
    }

    fun submit(requester: User, onCreated: (Int) -> Unit) {
        val state = _uiState.value

        val titleError = if (state.title.trim().length < 5) {
            "El título debe tener al menos 5 caracteres"
        } else null
        val descriptionError = if (state.description.trim().length < 10) {
            "La descripción debe tener al menos 10 caracteres"
        } else null
        val categoryError = if (state.category.isEmpty()) "Elige una categoría" else null
        val branchError = if (state.branch.isEmpty()) "Elige una sucursal" else null

        if (titleError != null || descriptionError != null ||
            categoryError != null || branchError != null
        ) {
            _uiState.update {
                it.copy(
                    titleError = titleError,
                    descriptionError = descriptionError,
                    categoryError = categoryError,
                    branchError = branchError
                )
            }
            return
        }

        val newId = TicketRepository.create(
            title = state.title.trim(),
            description = state.description.trim(),
            category = state.category,
            priority = state.priority,
            branch = state.branch,
            requester = requester
        )
        _uiState.value = CreateTicketUiState()
        onCreated(newId)
    }

    fun reset() {
        _uiState.value = CreateTicketUiState()
    }
}

