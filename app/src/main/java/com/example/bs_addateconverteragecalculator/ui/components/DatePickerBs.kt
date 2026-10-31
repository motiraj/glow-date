package com.example.bs_addateconverteragecalculator.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.BsCalendarData
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.utils.LocalIsNepali

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBs(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDay: Int,
    onDateSelected: (Int, Int, Int) -> Unit
) {
    val isNepali = LocalIsNepali.current
    val years = remember { (2000..2100).toList() }
    val months = remember { (1..12).toList() }
    val maxDays = BsCalendarData.getDaysInMonth(selectedYear, selectedMonth)
    val days = remember(selectedYear, selectedMonth) { (1..maxDays).toList() }

    var yearExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }
    var dayExpanded by remember { mutableStateOf(false) }

    val yearInteraction = remember { MutableInteractionSource() }
    val monthInteraction = remember { MutableInteractionSource() }
    val dayInteraction = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Year Dropdown
        ExposedDropdownMenuBox(
            expanded = yearExpanded,
            onExpandedChange = { yearExpanded = !yearExpanded },
            modifier = Modifier.weight(1.15f)
        ) {
            val valueText = NepaliNumerals.formatDigits(selectedYear, isNepali)
            BasicTextField(
                value = valueText,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                interactionSource = yearInteraction,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = valueText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = yearInteraction,
                        label = { Text("वर्ष", style = MaterialTheme.typography.labelSmall) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        contentPadding = PaddingValues(start = 10.dp, top = 8.dp, end = 2.dp, bottom = 8.dp)
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = yearExpanded,
                onDismissRequest = { yearExpanded = false }
            ) {
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(NepaliNumerals.formatDigits(year, isNepali), fontWeight = FontWeight.Bold) },
                        onClick = {
                            yearExpanded = false
                            val newMaxDays = BsCalendarData.getDaysInMonth(year, selectedMonth)
                            val adjustedDay = if (selectedDay > newMaxDays) newMaxDays else selectedDay
                            onDateSelected(year, selectedMonth, adjustedDay)
                        }
                    )
                }
            }
        }

        // Month Dropdown
        ExposedDropdownMenuBox(
            expanded = monthExpanded,
            onExpandedChange = { monthExpanded = !monthExpanded },
            modifier = Modifier.weight(1.65f)
        ) {
            val valueText = NepaliMonths.getMonthName(selectedMonth, isNepali)
            BasicTextField(
                value = valueText,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                interactionSource = monthInteraction,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = valueText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = monthInteraction,
                        label = { Text("महिना", style = MaterialTheme.typography.labelSmall) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        contentPadding = PaddingValues(start = 10.dp, top = 8.dp, end = 2.dp, bottom = 8.dp)
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = monthExpanded,
                onDismissRequest = { monthExpanded = false }
            ) {
                months.forEach { month ->
                    DropdownMenuItem(
                        text = { Text(NepaliMonths.getMonthName(month, isNepali), fontWeight = FontWeight.Bold) },
                        onClick = {
                            monthExpanded = false
                            val newMaxDays = BsCalendarData.getDaysInMonth(selectedYear, month)
                            val adjustedDay = if (selectedDay > newMaxDays) newMaxDays else selectedDay
                            onDateSelected(selectedYear, month, adjustedDay)
                        }
                    )
                }
            }
        }

        // Day Dropdown
        ExposedDropdownMenuBox(
            expanded = dayExpanded,
            onExpandedChange = { dayExpanded = !dayExpanded },
            modifier = Modifier.weight(0.9f)
        ) {
            val valueText = NepaliNumerals.formatDigits(selectedDay, isNepali)
            BasicTextField(
                value = valueText,
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true),
                interactionSource = dayInteraction,
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = valueText,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = VisualTransformation.None,
                        interactionSource = dayInteraction,
                        label = { Text("गते", style = MaterialTheme.typography.labelSmall) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dayExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        contentPadding = PaddingValues(start = 10.dp, top = 8.dp, end = 2.dp, bottom = 8.dp)
                    )
                }
            )
            ExposedDropdownMenu(
                expanded = dayExpanded,
                onDismissRequest = { dayExpanded = false }
            ) {
                days.forEach { day ->
                    DropdownMenuItem(
                        text = { Text(NepaliNumerals.formatDigits(day, isNepali), fontWeight = FontWeight.Bold) },
                        onClick = {
                            dayExpanded = false
                            onDateSelected(selectedYear, selectedMonth, day)
                        }
                    )
                }
            }
        }
    }
}


