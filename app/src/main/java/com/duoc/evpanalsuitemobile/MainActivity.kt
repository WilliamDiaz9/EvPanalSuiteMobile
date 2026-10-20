package com.duoc.evpanalsuitemobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.duoc.evpanalsuitemobile.ui.navigation.AppNavHost
import com.duoc.evpanalsuitemobile.ui.theme.PanalSuiteTheme
import com.duoc.evpanalsuitemobile.viewmodel.AuthViewModel
import com.duoc.evpanalsuitemobile.viewmodel.CreateTicketViewModel
import com.duoc.evpanalsuitemobile.viewmodel.TicketViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PanalSuiteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    val ticketViewModel: TicketViewModel = viewModel()
                    val createTicketViewModel: CreateTicketViewModel = viewModel()

                    AppNavHost(
                        navController = navController,
                        authViewModel = authViewModel,
                        ticketViewModel = ticketViewModel,
                        createTicketViewModel = createTicketViewModel
                    )
                }
            }
        }
    }
}

