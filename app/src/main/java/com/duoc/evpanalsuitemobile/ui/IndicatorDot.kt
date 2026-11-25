package com.duoc.evpanalsuitemobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.duoc.evpanalsuitemobile.model.Indicator

// Color de cada indicador
fun Indicator.color(): Color = when (this) {
    Indicator.RED -> Color(0xFFD32F2F)
    Indicator.YELLOW -> Color(0xFFF9A825)
    Indicator.GREEN -> Color(0xFF2E7D32)
    Indicator.GRAY -> Color(0xFF9E9E9E)
}

// Circulo de color con un texto opcional al lado
@Composable
fun IndicatorDot(
    indicator: Indicator,
    label: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(indicator.color(), CircleShape)
        )
        if (label != null) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}
