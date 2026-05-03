package com.example.giros.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.giros.model.SpinResult
import com.example.giros.model.Wheel
import com.example.giros.model.WheelOption
import com.example.giros.ui.components.WheelCanvas
import com.example.giros.ui.theme.GirosTheme
import kotlinx.coroutines.launch

@Composable
fun SpinScreen(
    wheel: Wheel,
    remainingOptions: List<WheelOption>,
    ranking: List<SpinResult>,
    lastResult: SpinResult?,
    onBackClick: () -> Unit,
    onSpinRequest: () -> WheelOption?,
    onSpinFinished: (WheelOption) -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation = remember(wheel.id) { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 70) }
    var isSpinning by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    DisposableEffect(Unit) {
        onDispose {
            toneGenerator.release()
        }
    }

    LaunchedEffect(wheel.id, ranking.isEmpty(), remainingOptions.size) {
        if (ranking.isEmpty() && remainingOptions.size == wheel.options.size) {
            rotation.snapTo(0f)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = wheel.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Seguimiento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Opciones restantes",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${remainingOptions.size} de ${wheel.options.size}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Card(modifier = Modifier.weight(1f)) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Ultimo giro",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = lastResult?.selectedOption?.name ?: "Sin resultado",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                WheelCanvas(
                    options = if (remainingOptions.isEmpty()) wheel.options else remainingOptions,
                    rotationDegrees = rotation.value,
                    sizeFraction = 0.64f
                )
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Ranking parcial",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (ranking.isEmpty()) {
                        Text("Todavia no hay posiciones definidas.")
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ranking.forEach { result ->
                                Text("${result.position}. ${result.selectedOption.name}")
                            }
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Opciones en juego",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (remainingOptions.isEmpty()) {
                        Text("Ya no quedan opciones. Reinicia para volver a empezar.")
                    } else {
                        remainingOptions.forEach { option ->
                            Text("• ${option.name}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.02f))

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
                onClick = onResetClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Reiniciar")
            }
        }

        Button(
            onClick = {
                val candidate = onSpinRequest() ?: return@Button
                val optionIndex = remainingOptions.indexOfFirst { it.id == candidate.id }
                if (optionIndex == -1) return@Button

                val sweepAngle = 360f / remainingOptions.size
                val correctionAngle = (360f - ((optionIndex * sweepAngle) + (sweepAngle / 2f))) % 360f
                val targetRotation = rotation.value + (360f * 5f) + correctionAngle
                val settleRotation = targetRotation - 8f

                scope.launch {
                    isSpinning = true
                    rotation.animateTo(
                        targetValue = settleRotation,
                        animationSpec = tween(
                            durationMillis = 2000,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    rotation.animateTo(
                        targetValue = targetRotation,
                        animationSpec = tween(
                            durationMillis = 260,
                            easing = FastOutSlowInEasing
                        )
                    )
                    toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 180)
                    onSpinFinished(candidate)
                    isSpinning = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = remainingOptions.isNotEmpty() && !isSpinning
        ) {
            Text(
                when {
                    isSpinning -> "Girando..."
                    remainingOptions.isEmpty() -> "Ranking completo"
                    else -> "Girar"
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SpinScreenPreview() {
    val wheel = Wheel(
        id = "1",
        name = "Cena",
        options = listOf(
            WheelOption("a", "Pizza"),
            WheelOption("b", "Sushi"),
            WheelOption("c", "Hamburguesa")
        )
    )

    GirosTheme {
        SpinScreen(
            wheel = wheel,
            remainingOptions = listOf(
                WheelOption("a", "Pizza"),
                WheelOption("c", "Hamburguesa")
            ),
            ranking = listOf(
                SpinResult(position = 1, selectedOption = WheelOption("b", "Sushi"))
            ),
            lastResult = SpinResult(position = 1, selectedOption = WheelOption("b", "Sushi")),
            onBackClick = {},
            onSpinRequest = { null },
            onSpinFinished = {},
            onResetClick = {}
        )
    }
}
