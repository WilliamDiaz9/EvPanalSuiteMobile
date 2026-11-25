package com.duoc.evpanalsuitemobile.model

// Color de un indicador de estado (verde, amarillo, rojo o gris)
enum class Indicator(val label: String) {
    RED("Crítico"),
    YELLOW("En curso"),
    GREEN("Listo"),
    GRAY("Pendiente")
}
