package com.duoc.evpanalsuitemobile.model

sealed class LoginResult {
    data object Authenticating : LoginResult()
    data class Success(val user: User) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

