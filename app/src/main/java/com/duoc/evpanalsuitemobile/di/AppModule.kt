package com.duoc.evpanalsuitemobile.di

import com.duoc.evpanalsuitemobile.data.remote.RetrofitClient
import com.duoc.evpanalsuitemobile.repository.AuthRepository
import com.duoc.evpanalsuitemobile.repository.ProviderStatusRepository
import com.duoc.evpanalsuitemobile.repository.TicketRepository

// Crea una sola vez los repositorios que comparten los ViewModels
object AppModule {

    val authRepository: AuthRepository by lazy { AuthRepository(RetrofitClient.usersApi) }

    val ticketRepository: TicketRepository by lazy { TicketRepository(RetrofitClient.ticketsApi) }

    val providerStatusRepository: ProviderStatusRepository by lazy { ProviderStatusRepository(RetrofitClient.providerStatusApi) }
}
