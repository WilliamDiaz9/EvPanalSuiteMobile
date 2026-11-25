package com.duoc.evpanalsuitemobile.data.remote

import com.duoc.evpanalsuitemobile.model.Ticket
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Microservicio de tickets (puerto 8082)
interface TicketsApiService {

    @GET("api/tickets")
    suspend fun getTickets(): List<Ticket>

    @POST("api/tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): Ticket

    @PATCH("api/tickets/{id}/status")
    suspend fun changeStatus(@Path("id") id: Int, @Body request: StatusRequest): Ticket

    @PATCH("api/tickets/{id}/assign")
    suspend fun assign(@Path("id") id: Int, @Body request: AssignRequest): Ticket

    @POST("api/tickets/{id}/comments")
    suspend fun addComment(@Path("id") id: Int, @Body request: CommentRequest): Ticket

    // Responde 204 sin cuerpo, por eso se usa Response<Unit>
    @DELETE("api/tickets/{id}")
    suspend fun deleteTicket(@Path("id") id: Int, @Query("userId") userId: Int): Response<Unit>

    @DELETE("api/tickets/{id}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("id") id: Int,
        @Path("commentId") commentId: Int,
        @Query("userId") userId: Int
    ): Ticket
}
