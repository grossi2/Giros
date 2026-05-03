package com.example.giros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.giros.ui.theme.GirosTheme

@Composable
fun HomeScreen(
    onCreateWheelClick: () -> Unit,
    onViewWheelsClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Giros",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Crea ruedas personalizadas, guardalas y genera rankings aleatorios sin repetir opciones.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Primera versión",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(text = "• Crear y editar ruedas")
                Text(text = "• Guardar ruedas en el dispositivo")
                Text(text = "• Ejecutar sorteos con ranking sin repetición")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCreateWheelClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Nueva rueda")
            }

            Button(
                onClick = onViewWheelsClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Mis ruedas")
            }
        }

        Button(
            onClick = onAboutClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Acerca de")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    GirosTheme {
        HomeScreen(
            onCreateWheelClick = {},
            onViewWheelsClick = {},
            onAboutClick = {}
        )
    }
}
