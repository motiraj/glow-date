package com.example.bs_addateconverteragecalculator.ui.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ThemeSampleScreen() {
    BSADDateConverterAgeCalculatorTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Glow Date Theme",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Nepali Date Converter & Panchanga",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Card(
                    shape = Shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today: २०८३, असोज २०",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tithi: Navami",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Button(
                    onClick = { },
                    shape = Shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Calculate Age")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LocalSemanticColors.current.holiday, Shapes.small)
                    )
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LocalSemanticColors.current.fire, Shapes.small)
                    )
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LocalSemanticColors.current.earth, Shapes.small)
                    )
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LocalSemanticColors.current.water, Shapes.small)
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ThemeSampleScreenPreview() {
    ThemeSampleScreen()
}
