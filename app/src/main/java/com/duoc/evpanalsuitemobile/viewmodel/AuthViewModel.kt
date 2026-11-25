package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.evpanalsuitemobile.di.AppModule
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.repository.AuthRepository
import com.duoc.evpanalsuitemobile.repository.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    authRepository: AuthRepository = AppModule.authRepository
) : ViewModel() {

    // Usuario inicial: el ADMIN (mismo id que en el servidor)
    private val _currentUser = MutableStateFlow(
        SampleData.users.first { it.role == Role.ADMIN }
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Lista de usuarios que viene del microservicio
    val users: StateFlow<List<User>> = authRepository.users

    fun selectUser(user: User) {
        _currentUser.value = user
    }
}
