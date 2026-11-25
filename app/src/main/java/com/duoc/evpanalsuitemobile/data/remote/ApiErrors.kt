package com.duoc.evpanalsuitemobile.data.remote

import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.IOException
import retrofit2.HttpException

// Convierte un error tecnico en un texto simple para mostrar al usuario
fun Throwable.toUserMessage(): String = when (this) {
    is HttpException -> httpErrorMessage(this)
    is IOException -> "No se pudo conectar con el servidor. Revisa que los microservicios estén encendidos."
    else -> message ?: "Ocurrió un error inesperado"
}

// Lee el mensaje que manda el microservicio en el cuerpo del error
private fun httpErrorMessage(error: HttpException): String {
    val body = error.response()?.errorBody()?.string().orEmpty()
    return try {
        val json = Gson().fromJson(body, JsonObject::class.java)
        if (json.has("message")) {
            json.get("message").asString
        } else {
            // Errores de validacion: vienen como campo -> mensaje
            json.entrySet().joinToString(". ") { it.value.asString }
        }
    } catch (e: Exception) {
        "Error del servidor (código ${error.code()})"
    }
}
