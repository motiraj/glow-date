package com.example.bs_addateconverteragecalculator.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.bs_addateconverteragecalculator.data.RashifalData
import com.example.bs_addateconverteragecalculator.data.ZodiacSign

@Composable
fun RashifalIconGrid(
    signs: List<ZodiacSign>,
    selectedSignId: String,
    onSignSelected: (ZodiacSign) -> Unit,
    lang: String,
    modifier: Modifier = Modifier,
    columnsCount: Int = 4
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chunk into rows of columnsCount (default 4 columns = 3 compact rows for 12 signs)
        signs.chunked(columnsCount).forEach { rowSigns ->
            val containsSelected = rowSigns.any { it.id == selectedSignId }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(if (containsSelected) 1f else 0f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowSigns.forEach { sign ->
                    RashifalIconCard(
                        sign = sign,
                        isSelected = sign.id == selectedSignId,
                        onClick = { onSignSelected(sign) },
                        lang = lang,
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill the remaining spots if the list size isn't a multiple of columnsCount
                repeat(columnsCount - rowSigns.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun RashifalIconCard(
    sign: ZodiacSign,
    isSelected: Boolean,
    onClick: () -> Unit,
    lang: String,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1.0f,
        animationSpec = tween(durationMillis = 180),
        label = "scale_anim"
    )

    val cardColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val symbolColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    val signName = if (lang == "en") sign.nameEnglish else sign.nameNepali

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = cardColor,
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .zIndex(if (isSelected) 1f else 0f)
            .scale(scale)
            .aspectRatio(0.95f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 6.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = sign.symbol,
                    fontSize = 24.sp,
                    color = symbolColor
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = signName,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RashifalIconGridPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF0D1323)) // Deep Navy Canvas
            .padding(16.dp)
    ) {
        RashifalIconGrid(
            signs = RashifalData.signs,
            selectedSignId = "aries",
            onSignSelected = {},
            lang = "ne"
        )
    }
}
