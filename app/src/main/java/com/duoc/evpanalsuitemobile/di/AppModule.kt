package com.duoc.evpanalsuitemobile.di

import android.content.Context
import com.duoc.evpanalsuitemobile.data.local.DataStoreSessionStorage
import com.duoc.evpanalsuitemobile.data.local.SessionStorage
import com.duoc.evpanalsuitemobile.data.remote.RetrofitClient
import com.duoc.evpanalsuitemobile.repository.AuthRepository
import com.duoc.evpanalsuitemobile.repository.ProviderStatusRepository
import com.duoc.evpanalsuitemobile.repository.TicketRepository

// Crea una sola vez los repositorios que comparten los ViewModels
object AppModule {

    // Almacenamiento local (DataStore). Se crea al abrir la app.
    lateinit var sessionStorage: SessionStorage
        private set

    fun init(context: Context) {
        sessionStorage = DataStoreSessionStorage(context.applicationContext)
    }

    val authRepository: AuthRepository by lazy { AuthRepository(RetrofitClient.usersApi) }

    val ticketRepository: TicketRepository by lazy { TicketRepository(RetrofitClient.ticketsApi) }

    val providerStatusRepository: ProviderStatusRepository by lazy { ProviderStatusRepository(RetrofitClient.providerStatusApi) }
}
