@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.panchanga

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.ui.theme.NotoSerifDevanagari
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import org.json.JSONObject

data class PatroMonthData(
    val monthNumber: Int,
    val name: String,
    val days: Int,
    val events: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatroViewerScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var patroTitle by remember { mutableStateOf("नेपाल पञ्चाङ्ग २०८३ (Official Patro)") }
    var patroDesc by remember { mutableStateOf("संवत् २०८३ को आधिकारिक पात्रो तथा पर्व विवरण") }
    var monthsList by remember { mutableStateOf<List<PatroMonthData>>(emptyList()) }
    var selectedMonth by remember { mutableStateOf<PatroMonthData?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val inputStream = context.assets.open("panchanga_2083_meta.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObj = JSONObject(jsonString)
            patroTitle = jsonObj.optString("title", patroTitle)
            patroDesc = jsonObj.optString("description", patroDesc)
            val jsonArray = jsonObj.getJSONArray("months")
            val list = mutableListOf<PatroMonthData>()
            for (i in 0 until jsonArray.length()) {
                val mObj = jsonArray.getJSONObject(i)
                val mNum = mObj.getInt("month")
                val mName = mObj.getString("name")
                val mDays = mObj.getInt("days")
                val evArray = mObj.getJSONArray("events")
                val evList = mutableListOf<String>()
                for (j in 0 until evArray.length()) {
                    evList.add(evArray.getString(j))
                }
                list.add(PatroMonthData(mNum, mName, mDays, evList))
            }
            monthsList = list
            if (list.isNotEmpty()) {
                selectedMonth = list[0]
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "📖 $patroTitle",
                        fontFamily = NotoSerifDevanagari,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingVals ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingVals)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card with PDF export/view action
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF Patro",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = patroTitle,
                                style = MaterialTheme.typography.titleMedium.copy(fontFamily = NotoSerifDevanagari),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = patroDesc,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            try {
                                val pdfFile = File(context.getExternalFilesDir(null), "Panchanga_2083_Patro.pdf")
                                if (!pdfFile.exists()) {
                                    context.assets.open("panchanga_2083_meta.json").use { input ->
                                        pdfFile.outputStream().use { output ->
                                            input.copyTo(output)
                                        }
                                    }
                                }
                                val uri: Uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    pdfFile
                                )
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, "application/pdf")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                }
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                errorMessage = "कुनै पनि PDF रिडर एप फेला परेन। कृपया प्ले स्टोरबाट PDF Reader डाउनलोड गर्नुहोस्।"
                            } catch (e: Exception) {
                                errorMessage = "PDF खोल्न सकिएन: ${e.localizedMessage}"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "📥 बाह्य PDF Patro फाइल खोल्नुहोस्",
                            fontFamily = NotoSerifDevanagari,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    errorMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = msg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Month selection chips
            PrimaryScrollableTabRow(
                selectedTabIndex = monthsList.indexOf(selectedMonth).coerceAtLeast(0),
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                monthsList.forEach { m ->
                    Tab(
                        selected = selectedMonth == m,
                        onClick = { selectedMonth = m },
                        text = {
                            Text(
                                text = "${m.name} (${NepaliNumerals.toNepaliDigits(m.monthNumber)})",
                                fontFamily = NotoSerifDevanagari,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                }
            }

            // Selected Month Details & Festivals
            selectedMonth?.let { month ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "महिना: ${month.name}",
                                style = MaterialTheme.typography.titleLarge.copy(fontFamily = NotoSerifDevanagari),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "कुल दिन: ${NepaliNumerals.toNepaliDigits(month.days)}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "📜 यस महिनाका प्रमुख चाडपर्व तथा मुहूर्तहरू:",
                            style = MaterialTheme.typography.titleSmall.copy(fontFamily = NotoSerifDevanagari),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(month.events) { event ->
                                Surface(
                                    shape = MaterialTheme.shapes.medium,
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Event,
                                            contentDescription = "Event",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = event,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = NotoSerifDevanagari),
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
    }
}
