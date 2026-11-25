package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duoc.evpanalsuitemobile.data.local.SessionStorage
import com.duoc.evpanalsuitemobile.di.AppModule
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.repository.AuthRepository
import com.duoc.evpanalsuitemobile.repository.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(
    authRepository: AuthRepository = AppModule.authRepository,
    private val sessionStorage: SessionStorage = AppModule.sessionStorage
) : ViewModel() {

    // Usuario inicial: el ADMIN (mismo id que en el servidor)
    private val _currentUser = MutableStateFlow(
        SampleData.users.first { it.role == Role.ADMIN }
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Lista de usuarios que viene del microservicio
    val users: StateFlow<List<User>> = authRepository.users

    // Si el usuario ya eligio uno a mano, no pisamos su eleccion
    private var userChosen = false

    init {
        restoreUser()
    }

    // Cuando llegan los usuarios del servidor, vuelve a elegir el ultimo guardado
    private fun restoreUser() {
        viewModelScope.launch {
            val saved = combine(sessionStorage.userId, users) { id, list ->
                list.firstOrNull { it.id == id }
            }.filterNotNull().first()
            if (!userChosen) {
                _currentUser.value = saved
            }
        }
    }

    fun selectUser(user: User) {
        userChosen = true
        _currentUser.value = user
        viewModelScope.launch { sessionStorage.saveUserId(user.id) }
    }
}
