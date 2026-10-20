package com.duoc.evpanalsuitemobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.duoc.evpanalsuitemobile.model.canCreateTicket
import com.duoc.evpanalsuitemobile.model.canViewDashboard
import com.duoc.evpanalsuitemobile.model.visibleTickets
import com.duoc.evpanalsuitemobile.repository.SampleData
import com.duoc.evpanalsuitemobile.ui.AccessDeniedScreen
import com.duoc.evpanalsuitemobile.ui.CreateTicketScreen
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
    val tickets by ticketViewModel.tickets.collectAsState()

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
            TicketListScreen(
                user = user,
                users = SampleData.users,
                tickets = user.visibleTickets(tickets),
                userName = { id -> ticketViewModel.userName(id) },
                onTicketClick = { ticketId -> navController.navigate(Routes.ticketDetail(ticketId)) },
                onCreateClick = { navController.navigate(Routes.CREATE_TICKET) },
                onDashboardClick = { navController.navigate(Routes.SUPERVISOR) },
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
                    resolvers = ticketViewModel.resolvers,
                    onBack = { navController.popBackStack() }
                )
            } else {
                AccessDeniedScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

