@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.panchanga

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.AdhikMaasEngine
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MalmasKnowledgeSheet(
    selectedAdDate: LocalDate,
    onDismiss: () -> Unit
) {
    val lang = com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage.current
    val statusResult = AdhikMaasEngine.checkAdhikMaas(selectedAdDate)

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
            // Sheet Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = "Malmas Guide",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lang == "en") "Malmas / Adhik Maas Guide" else "मलमास (अधिकमास / पुरुषोत्तम मास) मार्गदर्शन",
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
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // ACTIVE OR UPCOMING MALMAS BANNER
                item {
                    val info = statusResult.currentOrUpcomingInfo
                    if (info != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (statusResult.isCurrentDateInMalmas) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (statusResult.isCurrentDateInMalmas) "⚠️ हाल मलमास (अधिकमास) सक्रिय छ" else "आगामी मलमास (अधिकमास / अनला): वि.सं. ${NepaliNumerals.toNepaliDigits(info.bsYear)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (statusResult.isCurrentDateInMalmas) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "अवधि: ${info.bsStartText} देखि ${info.bsEndText} सम्म (${info.adStartDate} to ${info.adEndDate})",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (statusResult.isCurrentDateInMalmas) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (lang == "en") info.descriptionEnglish else info.descriptionNepali,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }

                // 1. ASTRONOMICAL LOGIC (मलमास / अधिकमास खगोलीय गणितीय नियम)
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (lang == "en") "1. Astronomical Calculation Logic" else "१. मलमास (अधिकमास) कसरी र किन हुन्छ?",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Text(
                                text = if (lang == "en") {
                                    "• Solar Year = 365.2422 days | Lunar Year = 354.367 days.\n" +
                                    "• The 10.875-day annual difference accumulates to 1 full lunar month (~30 days) every 32.5 months (~2.8 to 3 years).\n" +
                                    "• Sun Sankranti Absence Rule: In Vedic astronomy, a lunar month (Aunsi to Aunsi) with NO Sun Sankranti (the Sun stays in 1 Zodiac sign throughout) is declared as Adhik Maas (Malmas / Anala)."
                                } else {
                                    "• सूर्यमान वर्ष (३६५.२४२२ दिन) र चन्द्रमान वर्ष (३५४.३६७ दिन) बिच प्रतिवर्ष करिब १०.८७५ दिनको अन्तर हुन्छ।\n" +
                                    "• हरेक ३२ महिना १६ घडी (करिब २.८ देखि ३ वर्ष) मा यो अन्तर ३० दिन (१ पूरा महिना) बन्न पुग्छ।\n" +
                                    "• सूर्य संक्रान्ति विहीन महिना नियम: एक औँसी देखि अर्को औँसीसम्मको चन्द्र महिनाभित्र कुनै पनि सूर्य संक्रान्ति (सूर्यको राशि परिवर्तन) नपरेमा सो महिनालाई 'अधिकमास' (मलमास/पुरुषोत्तम मास/अनला) मानिन्छ।"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                // 2. RELIGIOUS RITUALS & DO'S AND DON'TS (धार्मिक मान्यता र नियमहरू)
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = if (lang == "en") "2. Religious Do's and Don'ts (पुरुषोत्तम नियम)" else "२. धार्मिक मान्यता, शुभ कार्य र वर्जित कार्यहरू",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = if (lang == "en") {
                                    "• Lord Purushottam Grace: Adhik Maas is blessed by Lord Vishnu (Purushottam). Spiritual practices carry multi-fold merits.\n" +
                                    "• Recommended: Shrimad Bhagavata Path, Vishnu Sahasranama, Punya Snan, and Kheer/Malpua donation.\n" +
                                    "• Restricted (वर्जित कार्यहरू): Kamya rituals like Marriage (विवाह), Bratabandha (व्रतबन्ध), Griha Pravesh (गृहप्रवेश), and new business openings are avoided."
                                } else {
                                    "• भगवान् पुरुषोत्तमको विशेष कृपा: मलमास स्वामीविहीन मानिने भएकाले भगवान् विष्णु (पुरुषोत्तम) ले यसको स्वामित्व ग्रहण गर्नुभएको धार्मिक मान्यता छ।\n" +
                                    "• करणीय कार्यहरू: नित्य पूजाआजा, श्रीमद्भागवत महापुराण पाठ, विष्णु सहस्रनाम, नानी-पुण्य स्नान, काँसाको पात्रमा मालपुवा/खीर दान।\n" +
                                    "• वर्जित कार्यहरू (काम्य कर्म): विवाह, व्रतबन्ध, गृहप्रवेश, नयाँ व्यापार आरम्भ, देवाली पूजा र नयाँ मूर्ति प्राणप्रतिष्ठा गर्न मनाही हुन्छ।"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }

                // 3. CALIBRATED ADHIK MAAS TABLE (विक्रम संवत् मलमास तालिका)
                item {
                    Text(
                        text = if (lang == "en") "3. Adhik Maas Calendar Table (BS 2070–2090)" else "३. विक्रम संवत् मलमास (अधिकमास) तालिका",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                items(AdhikMaasEngine.ADHIK_MAAS_TABLE, key = { it.bsYear }) { period ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "वि.सं. ${NepaliNumerals.toNepaliDigits(period.bsYear)} (${period.bsMonthName} अधिकमास)",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${period.bsStartText} देखि ${period.bsEndText} सम्म (${period.adStartDate} to ${period.adEndDate})",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
