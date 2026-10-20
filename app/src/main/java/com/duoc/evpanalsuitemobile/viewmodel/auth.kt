package com.duoc.evpanalsuitemobile.viewmodel

import androidx.lifecycle.ViewModel
import com.duoc.evpanalsuitemobile.model.LoginResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUI (
    val usuario: String = "",
    val contraseña: String = "",
    val ErrorUsuario: String? = null,
    val ErrorContraseña: String? = null,
    val Resultado: LoginResult? = null
)

class authmodel : ViewModel(){
    private val Estado = MutableStateFlow(LoginUI())
    val UiEstado: StateFlow<LoginUI> = Estado.asStateFlow()

    fun onUsuarioChange(valor: String) {
        Estado.update { it.copy(usuario = valor, ErrorUsuario = null, Resultado = null) }
    }

    fun onContraseñaChange(valor: String) {
        Estado.update { it.copy(contraseña = valor, ErrorContraseña = null, Resultado = null) }
    }

    fun iniciarSesion() {
        val actual = Estado.value
        val usuario = actual.usuario.trim()

        val errorUsuario = if (usuario.isEmpty()) "Ingresa tu usuario" else null
        val errorContraseña = when {
            actual.contraseña.isEmpty() -> "Ingresa tu contraseña"
            actual.contraseña.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }

        if (errorUsuario != null || errorContraseña != null) {
            Estado.update {
                it.copy(
                    ErrorUsuario = errorUsuario,
                    ErrorContraseña = errorContraseña,
                    Resultado = LoginResult.Error("Revisa los datos ingresados")
                )
            }
            return
        }

        Estado.update {
            it.copy(
                ErrorUsuario = null,
                ErrorContraseña = null,
                Resultado = LoginResult.Success(usuario)
            )
        }
    }
}
