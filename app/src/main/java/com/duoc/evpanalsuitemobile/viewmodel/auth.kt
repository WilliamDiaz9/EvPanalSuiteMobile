package com.duoc.evpanalsuitemobile.viewmodel

import com.duoc.evpanalsuitemobile.model.LoginResult
data class LoginUI (
    val usuario: String = "",
    val contraseña: String = "",
    val ErrorUsuario: String? = null,
    val ErrorContraseña: String? = null,
    val Resultado: LoginResult? = null
)

class authmodel : viewmodel(){
    private val Estado = mutableStateFlow(LoginUI())
    val UiEstado: StateFlow<LoginUI> = Estado.asStateFlow()
}