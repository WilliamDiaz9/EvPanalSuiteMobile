package com.duoc.evpanalsuitemobile.model

sealed class LoginResult {
    data class Success(val usuario: String) : LoginResult()
    data class Error(val mensaje: String) : LoginResult()
}
