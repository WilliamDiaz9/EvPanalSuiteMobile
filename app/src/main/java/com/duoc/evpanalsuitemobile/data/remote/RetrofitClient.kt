package com.duoc.evpanalsuitemobile.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Crea los clientes Retrofit. 10.0.2.2 es tu PC visto desde el emulador.
object RetrofitClient {

    private const val USERS_URL = "http://10.0.2.2:8081/"
    private const val TICKETS_URL = "http://10.0.2.2:8082/"
    // API externa (internet). Retrofit pide una base; la direccion real llega con @Url
    private const val PROVIDERS_URL = "https://www.githubstatus.com/"

    private fun build(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val usersApi: UsersApiService by lazy {
        build(USERS_URL).create(UsersApiService::class.java)
    }

    val ticketsApi: TicketsApiService by lazy {
        build(TICKETS_URL).create(TicketsApiService::class.java)
    }

    val providerStatusApi: ProviderStatusApiService by lazy {
        build(PROVIDERS_URL).create(ProviderStatusApiService::class.java)
    }
}
