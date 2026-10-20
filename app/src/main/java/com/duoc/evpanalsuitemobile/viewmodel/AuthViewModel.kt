package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.repository.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow(
        SampleData.users.first { it.role == Role.ADMIN }
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    fun selectUser(user: User) {
        _currentUser.value = user
    }
}

