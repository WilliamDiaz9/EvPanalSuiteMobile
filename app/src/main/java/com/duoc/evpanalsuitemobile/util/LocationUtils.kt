package com.duoc.evpanalsuitemobile.util

/** Coordenadas ficticias por sucursal: la georreferenciación del MVP es simulada. */
fun simulatedLocation(branch: String): String = when (branch) {
    "Tienda Centro" -> "-33.4489, -70.6693"
    "Tienda Sur" -> "-33.5200, -70.6600"
    "Farmacia Norte" -> "-33.3800, -70.6700"
    "Oficina central" -> "-33.4150, -70.6000"
    else -> "-33.4500, -70.6500"
}

