package com.duoc.evpanalsuitemobile.repository

import com.duoc.evpanalsuitemobile.model.LoginResult
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.User
import kotlinx.coroutines.delay

object AuthRepository {

    /** Simula la verificación contra un servidor: demora de red y resultado. */
    suspend fun authenticate(username: String, password: String): LoginResult {
        delay(1000L)
        val user = SampleData.users.firstOrNull {
            it.username == username && it.password == password
        }
        return if (user != null) {
            LoginResult.Success(user)
        } else {
            LoginResult.Error("Credenciales incorrectas")
        }
    }

    fun resolvers(): List<User> = SampleData.users.filter { it.role == Role.RESOLVER }

    fun userName(id: Int?): String =
        SampleData.users.firstOrNull { it.id == id }?.fullName ?: "Sin asignar"
}

