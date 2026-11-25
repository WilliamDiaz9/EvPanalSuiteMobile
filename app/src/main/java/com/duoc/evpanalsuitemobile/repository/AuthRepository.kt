package com.duoc.evpanalsuitemobile.repository

import com.duoc.evpanalsuitemobile.data.remote.LoginRequest
import com.duoc.evpanalsuitemobile.data.remote.UsersApiService
import com.duoc.evpanalsuitemobile.data.remote.toUserMessage
import com.duoc.evpanalsuitemobile.model.LoginResult
import com.duoc.evpanalsuitemobile.model.User
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Habla con el microservicio de usuarios
class AuthRepository(private val api: UsersApiService) {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    // Trae la lista de usuarios desde el servidor
    suspend fun loadUsers() {
        _users.value = api.getUsers()
    }

    // Login contra el servidor (todavia no hay pantalla de login)
    suspend fun authenticate(username: String, password: String): LoginResult =
        try {
            LoginResult.Success(api.login(LoginRequest(username, password)))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LoginResult.Error(e.toUserMessage())
        }
}
