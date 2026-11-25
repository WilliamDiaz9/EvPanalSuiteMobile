package com.duoc.evpanalsuitemobile.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.duoc.evpanalsuitemobile.model.canCreateTicket
import com.duoc.evpanalsuitemobile.model.canViewDashboard
import com.duoc.evpanalsuitemobile.model.visibleTickets
import com.duoc.evpanalsuitemobile.ui.AccessDeniedScreen
import com.duoc.evpanalsuitemobile.ui.CreateTicketScreen
import com.duoc.evpanalsuitemobile.ui.ProviderStatusScreen
import com.duoc.evpanalsuitemobile.ui.SplashScreen
import com.duoc.evpanalsuitemobile.ui.SupervisorScreen
import com.duoc.evpanalsuitemobile.ui.TicketDetailScreen
import com.duoc.evpanalsuitemobile.ui.TicketListScreen
import com.duoc.evpanalsuitemobile.viewmodel.AuthViewModel
import com.duoc.evpanalsuitemobile.viewmodel.CreateTicketViewModel
import com.duoc.evpanalsuitemobile.viewmodel.TicketViewModel
import kotlinx.coroutines.delay

@Composable
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    ticketViewModel: TicketViewModel,
    createTicketViewModel: CreateTicketViewModel
) {
    val user by authViewModel.currentUser.collectAsState()
    val users by authViewModel.users.collectAsState()
    val tickets by ticketViewModel.tickets.collectAsState()
    val resolvers by ticketViewModel.resolvers.collectAsState()
    val message by ticketViewModel.message.collectAsState()

    // Muestra los errores del servidor en un aviso abajo
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            ticketViewModel.clearMessage()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen()
            LaunchedEffect(Unit) {
                delay(1800)
                navController.navigate(Routes.TICKETS) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable(Routes.TICKETS) {
            // Cada vez que se abre la lista, pide los datos al servidor
            LaunchedEffect(Unit) { ticketViewModel.refresh() }
            TicketListScreen(
                user = user,
                users = users,
                tickets = user.visibleTickets(tickets),
                userName = { id -> users.firstOrNull { it.id == id }?.fullName ?: "Sin asignar" },
                onTicketClick = { ticketId -> navController.navigate(Routes.ticketDetail(ticketId)) },
                onCreateClick = { navController.navigate(Routes.CREATE_TICKET) },
                onDashboardClick = { navController.navigate(Routes.SUPERVISOR) },
                onProvidersClick = { navController.navigate(Routes.PROVIDERS) },
                onUserSelected = { authViewModel.selectUser(it) }
            )
        }

        composable(
            route = Routes.TICKET_DETAIL,
            arguments = listOf(navArgument("ticketId") { type = NavType.IntType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getInt("ticketId") ?: 0
            TicketDetailScreen(
                ticketId = ticketId,
                user = user,
                ticketViewModel = ticketViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PROVIDERS) {
            ProviderStatusScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.CREATE_TICKET) {
            if (user.role.canCreateTicket()) {
                CreateTicketScreen(
                    user = user,
                    viewModel = createTicketViewModel,
                    onBack = {
                        createTicketViewModel.reset()
                        navController.popBackStack()
                    },
                    onCreated = { newId ->
                        navController.navigate(Routes.ticketDetail(newId)) {
                            popUpTo(Routes.TICKETS)
                        }
                    }
                )
            } else {
                AccessDeniedScreen(onBack = { navController.popBackStack() })
            }
        }

        composable(Routes.SUPERVISOR) {
            if (user.role.canViewDashboard()) {
                SupervisorScreen(
                    tickets = tickets,
                    resolvers = resolvers,
                    onBack = { navController.popBackStack() }
                )
            } else {
                AccessDeniedScreen(onBack = { navController.popBackStack() })
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.align(Alignment.BottomCenter)
    )
    }
}

