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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.giros.model.WheelOption
import com.example.giros.ui.theme.GirosTheme
import com.example.giros.viewmodel.EditWheelUiState

@Composable
fun EditWheelScreen(
    uiState: EditWheelUiState,
    onBackClick: () -> Unit,
    onWheelNameChange: (String) -> Unit,
    onOptionDraftChange: (String) -> Unit,
    onAddOptionClick: () -> Unit,
    onRemoveOptionClick: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditing = uiState.editingWheelId != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isEditing) "Editar rueda" else "Nueva rueda",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = if (isEditing) {
                "Actualizá el nombre y las opciones de esta rueda."
            } else {
                "Definí el nombre de la rueda y cargá al menos dos opciones para empezar."
            },
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = uiState.wheelName,
            onValueChange = onWheelNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nombre de la rueda") },
            singleLine = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.optionDraft,
                onValueChange = onOptionDraftChange,
                modifier = Modifier.weight(1f),
                label = { Text("Nueva opción") },
                singleLine = true
            )

            Button(
                onClick = onAddOptionClick,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Agregar")
            }
        }

        if (uiState.validationMessage != null) {
            Text(
                text = uiState.validationMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Opciones cargadas (${uiState.options.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (uiState.options.isEmpty()) {
                    Text(
                        text = "Todavía no agregaste opciones.",
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(uiState.options, key = { it.id }) { option ->
                            OptionRow(
                                option = option,
                                onRemoveClick = { onRemoveOptionClick(option.id) }
                            )
                        }
                    }
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
                Text("Cancelar")
            }

            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isEditing) "Actualizar" else "Guardar")
            }
        }
    }
}

@Composable
private fun OptionRow(
    option: WheelOption,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = option.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onRemoveClick) {
            Text("Quitar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditWheelScreenPreview() {
    GirosTheme {
        EditWheelScreen(
            uiState = EditWheelUiState(
                editingWheelId = "wheel-1",
                wheelName = "Cena del finde",
                optionDraft = "",
                options = listOf(
                    WheelOption(id = "1", name = "Pizza"),
                    WheelOption(id = "2", name = "Empanadas")
                )
            ),
            onBackClick = {},
            onWheelNameChange = {},
            onOptionDraftChange = {},
            onAddOptionClick = {},
            onRemoveOptionClick = {},
            onSaveClick = {}
        )
    }
}
