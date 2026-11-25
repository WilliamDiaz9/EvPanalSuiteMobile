package com.duoc.evpanalsuitemobile.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

// API externa: estado publico de servicios de proveedores
interface ProviderStatusApiService {

    // La direccion completa llega como parametro
    @GET
    suspend fun getStatus(@Url url: String): StatusResponse
}
