@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.panchanga

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.TithiCalculator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TithiKnowledgeSheet(
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Tithi Info",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "पक्ष, तिथि र ५ वर्ग जानकारी",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. PAKSHA OVERVIEW
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "पक्षका आधारमा तिथि विभाजन",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            
                            Text(
                                text = "१. शुक्ल पक्ष / पूर्णिमा पक्ष (उज्यालो पक्ष / Bright Fortnight): औंसीको भोलिपल्टदेखि पूर्णिमासम्मको १५ दिनको समय। यसमा चन्द्रमा क्रमशः बढ्दै जान्छ। यस पक्षलाई धार्मिक तथा मांगलिक कार्यका लागि शुभ मानिन्छ।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 18.sp
                            )

                            HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                            Text(
                                text = "२. कृष्ण पक्ष / औंसी पक्ष (अँध्यारो पक्ष / Dark Fortnight): पूर्णिमाको भोलिपल्टदेखि औंसीसम्मको १५ दिनको समय। यसमा चन्द्रमा क्रमशः घट्दै जान्छ। यस पक्षमा पितृ कार्य (श्राद्ध, तर्पण आदि) बढी गरिन्छ।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // 2. TITHI 5 CATEGORIES (पाँच वर्ग)
                item {
                    Text(
                        text = "तिथिको वर्गीकरण (पाँच वर्ग)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val categories = listOf(
                        Triple("नन्दा (आनन्द दिने)", "प्रतिपदा (१), षष्ठी (६), एकादशी (११)", "शुभ कार्यका लागि राम्रो मानिन्छ।"),
                        Triple("भद्रा (कल्याण गर्ने)", "द्वितीया (२), सप्तमी (७), द्वादशी (१२)", "यात्रा र व्यापारका लागि राम्रो।"),
                        Triple("जया (विजय गराउने)", "तृतीया (३), अष्टमी (८), त्रयोदशी (१३)", "मुद्दा मामिला, प्रतिस्पर्धा र पराक्रमका काममा राम्रो।"),
                        Triple("रिक्ता (खाली हात हुने)", "चतुर्थी (४), नवमी (९), चतुर्दशी (१४)", "यस तिथिमा नयाँ वा मांगलिक कार्य सुरु गर्न निषेध जस्तै मानिन्छ।"),
                        Triple("पूर्णा (पूर्ण फल दिने)", "पञ्चमी (५), दशमी (१०), पूर्णिमा (१५) र औंसी (३०)", "गृहप्रवेश, विवाह तथा धार्मिक अनुष्ठानका लागि उत्तम।")
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categories.forEach { (cat, tithis, usage) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = cat,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = tithis,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = usage,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. 15 TITHIS DETAILED TABLE
                item {
                    Text(
                        text = "वर्षभरि आउने १५ तिथिहरू र तिनका विशेषता",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(TithiCalculator.tithiKnowledgeList) { tithi ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tithi.name,
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Surface(
                                    color = if (tithi.isShubhaForWork) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${tithi.category} वर्ग",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (tithi.isShubhaForWork) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "इष्ट देवता: ${tithi.deity} • वर्ग अर्थ: ${tithi.categoryMeaning}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = tithi.mainFeature,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
