package com.example.giros.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.giros.model.Wheel
import com.example.giros.model.WheelOption
import com.example.giros.ui.theme.GirosTheme

@Composable
fun WheelListScreen(
    wheels: List<Wheel>,
    onBackClick: () -> Unit,
    onCreateWheelClick: () -> Unit,
    onOpenWheelClick: (String) -> Unit,
    onEditWheelClick: (String) -> Unit,
    onDeleteWheelClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Mis ruedas",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (wheels.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Todavia no guardaste ruedas.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Crea una rueda para empezar a probar el flujo completo."
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(wheels, key = { it.id }) { wheel ->
                    WheelCard(
                        wheel = wheel,
                        onOpenClick = { onOpenWheelClick(wheel.id) },
                        onEditClick = { onEditWheelClick(wheel.id) },
                        onDeleteClick = { onDeleteWheelClick(wheel.id) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver")
            }

            Button(
                onClick = onCreateWheelClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Nueva rueda")
            }
        }
    }
}

@Composable
private fun WheelCard(
    wheel: Wheel,
    onOpenClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = wheel.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${wheel.options.size} opciones",
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onOpenClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Usar")
                }

                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Borrar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WheelListScreenPreview() {
    GirosTheme {
        WheelListScreen(
            wheels = listOf(
                Wheel(
                    id = "1",
                    name = "Cena",
                    options = listOf(
                        WheelOption("a", "Pizza"),
                        WheelOption("b", "Sushi")
                    )
                )
            ),
            onBackClick = {},
            onCreateWheelClick = {},
            onOpenWheelClick = {},
            onEditWheelClick = {},
            onDeleteWheelClick = {}
        )
    }
}
