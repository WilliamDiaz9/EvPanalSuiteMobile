package com.duoc.evpanalsuitemobile.data.remote

// Respuesta de las paginas de estado publicas (formato Statuspage)
data class StatusResponse(
    val status: StatusInfo
)

data class StatusInfo(
    // none, minor, major o critical
    val indicator: String,
    val description: String
)
