package com.duoc.evpanalsuitemobile

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.duoc.evpanalsuitemobile.di.AppModule
import com.duoc.evpanalsuitemobile.ui.navigation.AppNavHost
import com.duoc.evpanalsuitemobile.ui.theme.PanalSuiteTheme
import com.duoc.evpanalsuitemobile.util.createNotificationChannel
import com.duoc.evpanalsuitemobile.viewmodel.AuthViewModel
import com.duoc.evpanalsuitemobile.viewmodel.CreateTicketViewModel
import com.duoc.evpanalsuitemobile.viewmodel.TicketViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Prepara el almacenamiento local antes de crear los ViewModels
        AppModule.init(applicationContext)
        // Prepara las notificaciones y pide el permiso (Android 13 en adelante)
        createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
                .launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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

