package com.duoc.evpanalsuitemobile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.evpanalsuitemobile.model.LoginResult
import com.duoc.evpanalsuitemobile.ui.theme.PanalSuiteTheme
import com.duoc.evpanalsuitemobile.viewmodel.LoginUI
import com.duoc.evpanalsuitemobile.viewmodel.authmodel

/** Pantalla de login conectada al [authmodel]. */
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit = {},
    authViewModel: authmodel = viewModel()
) {
    val estado by authViewModel.UiEstado.collectAsStateWithLifecycle()

    LoginContent(
        estado = estado,
        onUsuarioChange = authViewModel::onUsuarioChange,
        onContraseñaChange = authViewModel::onContraseñaChange,
        onIniciarSesion = authViewModel::iniciarSesion
    )

    val resultado = estado.Resultado
    if (resultado is LoginResult.Success) {
        LaunchedEffect(resultado) {
            onLoginSuccess(resultado.usuario)
        }
    }
}

@Composable
fun LoginContent(
    estado: LoginUI,
    onUsuarioChange: (String) -> Unit,
    onContraseñaChange: (String) -> Unit,
    onIniciarSesion: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PanalSuite",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Gestión móvil de flujos de trabajo",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Iniciar sesión",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = estado.usuario,
            onValueChange = onUsuarioChange,
            label = { Text("Usuario") },
            singleLine = true,
            isError = estado.ErrorUsuario != null,
            supportingText = { estado.ErrorUsuario?.let { Text(it) } },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = estado.contraseña,
            onValueChange = onContraseñaChange,
            label = { Text("Contraseña") },
            singleLine = true,
            isError = estado.ErrorContraseña != null,
            supportingText = { estado.ErrorContraseña?.let { Text(it) } },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onIniciarSesion()
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        val resultado = estado.Resultado
        if (resultado is LoginResult.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = resultado.mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onIniciarSesion()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Ingresar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PanalSuiteTheme {
        LoginContent(
            estado = LoginUI(),
            onUsuarioChange = {},
            onContraseñaChange = {},
            onIniciarSesion = {}
        )
    }
}
