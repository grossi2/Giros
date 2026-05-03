package com.example.giros.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.giros.model.WheelOption
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WheelCanvas(
    options: List<WheelOption>,
    rotationDegrees: Float,
    sizeFraction: Float = 0.72f,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth(sizeFraction)
            .wrapContentWidth()
            .aspectRatio(1f)
    ) {
        if (options.isEmpty()) return@Canvas

        val sweepAngle = 360f / options.size
        val diameter = size.minDimension
        val arcSize = Size(diameter, diameter)
        val topLeft = Offset(
            x = (size.width - diameter) / 2f,
            y = (size.height - diameter) / 2f
        )
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val labelRadius = diameter * 0.32f

        val textPaint = Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 34f
            isFakeBoldText = true
            isAntiAlias = true
        }

        rotate(rotationDegrees, pivot = Offset(centerX, centerY)) {
            options.forEachIndexed { index, option ->
                val startAngle = -90f + (index * sweepAngle)
                drawArc(
                    color = wheelColors[index % wheelColors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = topLeft,
                    size = arcSize
                )

                val angleInRadians = Math.toRadians((startAngle + (sweepAngle / 2f)).toDouble())
                val textX = centerX + (labelRadius * cos(angleInRadians)).toFloat()
                val textY = centerY + (labelRadius * sin(angleInRadians)).toFloat()

                drawContext.canvas.nativeCanvas.drawText(
                    option.name.take(10),
                    textX,
                    textY,
                    textPaint
                )
            }
        }

        drawCircle(
            color = Color.White,
            radius = diameter * 0.08f,
            center = Offset(centerX, centerY)
        )
        drawCircle(
            color = Color(0xFF1D3557),
            radius = diameter * 0.035f,
            center = Offset(centerX, centerY)
        )

        val pointerBaseY = topLeft.y - 6.dp.toPx()
        val pointerHalfWidth = 18.dp.toPx()
        val pointerHeight = 26.dp.toPx()
        val pointerPath = Path().apply {
            moveTo(centerX, pointerBaseY)
            lineTo(centerX - pointerHalfWidth, pointerBaseY - pointerHeight)
            lineTo(centerX + pointerHalfWidth, pointerBaseY - pointerHeight)
            close()
        }
        drawPath(
            path = pointerPath,
            color = Color(0xFF1D3557)
        )
    }
}

private val wheelColors = listOf(
    Color(0xFFE76F51),
    Color(0xFFF4A261),
    Color(0xFF2A9D8F),
    Color(0xFF457B9D),
    Color(0xFF8D99AE),
    Color(0xFFE9C46A)
)
