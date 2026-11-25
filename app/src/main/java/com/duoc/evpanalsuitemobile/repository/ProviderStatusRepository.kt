package com.duoc.evpanalsuitemobile.repository

import com.duoc.evpanalsuitemobile.data.remote.ProviderStatusApiService
import com.duoc.evpanalsuitemobile.model.Indicator
import kotlinx.coroutines.CancellationException

data class Provider(val name: String, val url: String)

data class ProviderStatus(
    val name: String,
    val indicator: Indicator,
    val description: String
)

// Convierte el estado que entrega la API en un color
fun indicatorFromStatus(value: String): Indicator = when (value) {
    "none" -> Indicator.GREEN
    "minor" -> Indicator.YELLOW
    "major", "critical" -> Indicator.RED
    else -> Indicator.GRAY
}

// Consulta el estado de cada proveedor
class ProviderStatusRepository(private val api: ProviderStatusApiService) {

    val providers = listOf(
        Provider("GitHub", "https://www.githubstatus.com/api/v2/status.json"),
        Provider("Twilio", "https://status.twilio.com/api/v2/status.json"),
        Provider("New Relic", "https://status.newrelic.com/api/v2/status.json")
    )

    // Si un proveedor falla, los demas igual se muestran
    suspend fun loadAll(): List<ProviderStatus> = providers.map { provider ->
        try {
            val status = api.getStatus(provider.url).status
            ProviderStatus(provider.name, indicatorFromStatus(status.indicator), status.description)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            ProviderStatus(provider.name, Indicator.GRAY, "No se pudo consultar")
        }
    }
}
