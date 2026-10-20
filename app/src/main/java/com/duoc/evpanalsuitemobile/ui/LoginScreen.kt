package com.duoc.evpanalsuitemobile.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.duoc.evpanalsuitemobile.R
import com.duoc.evpanalsuitemobile.model.Role
import com.duoc.evpanalsuitemobile.model.User
import com.duoc.evpanalsuitemobile.model.label

@Composable
fun PanalMark(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.panalsuite_logo),
        contentDescription = "Logo PanalSuite",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun RoleAvatar(role: Role, modifier: Modifier = Modifier.size(48.dp)) {
    val roleColor = when (role) {
        Role.ADMIN -> Color(0xFF9A5B13)
        Role.SUPERVISOR -> Color(0xFF4267A8)
        Role.REQUESTER -> Color(0xFF278474)
        Role.RESOLVER -> Color(0xFF7256A3)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(roleColor)
    ) {
        Canvas(Modifier.fillMaxSize().padding(7.dp)) {
            val stroke = size.minDimension * 0.09f
            drawCircle(
                color = Color.White,
                radius = size.minDimension * 0.17f,
                center = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height * 0.30f),
                style = Stroke(width = stroke)
            )
            val shoulders = Path().apply {
                moveTo(size.width * 0.13f, size.height * 0.92f)
                cubicTo(size.width * 0.14f, size.height * 0.67f, size.width * 0.31f, size.height * 0.53f, size.width * 0.50f, size.height * 0.53f)
                cubicTo(size.width * 0.69f, size.height * 0.53f, size.width * 0.86f, size.height * 0.67f, size.width * 0.87f, size.height * 0.92f)
            }
            drawPath(
                path = shoulders,
                color = Color.White,
                style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

@Composable
fun UserPickerDialog(
    users: List<User>,
    selectedUser: User,
    onDismiss: () -> Unit,
    onConfirm: (User) -> Unit
) {
    var pendingUser by remember(selectedUser) { mutableStateOf(selectedUser) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Seleccionar usuario") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                users.forEach { option ->
                    val isSelected = option.id == pendingUser.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { pendingUser = option },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 2.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RoleAvatar(option.role, Modifier.size(48.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(option.fullName, style = MaterialTheme.typography.titleSmall)
                                Text(option.role.label(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            RadioButton(selected = isSelected, onClick = { pendingUser = option })
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(pendingUser) }) { Text("Hecho") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

