package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.evpanalsuitemobile.di.AppModule
import com.duoc.evpanalsuitemobile.repository.ProviderStatus
import com.duoc.evpanalsuitemobile.repository.ProviderStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProviderStatusUiState(
    val isLoading: Boolean = false,
    val items: List<ProviderStatus> = emptyList()
)

class ProviderStatusViewModel(
    private val repository: ProviderStatusRepository = AppModule.providerStatusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProviderStatusUiState())
    val uiState: StateFlow<ProviderStatusUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = repository.loadAll()
            _uiState.update { it.copy(isLoading = false, items = result) }
        }
    }
}
