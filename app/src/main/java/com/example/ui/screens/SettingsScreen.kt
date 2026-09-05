package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranDataSource
import com.example.data.model.AppSettings
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdateSettings: (AppSettings) -> Unit
) {
    var isReciterPickerOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("settings_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quran Reading Preferences
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "إعدادات المصحف والتلاوة",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Selected Reciter Setting
                    val currentReciter = QuranDataSource.RECITERS_LIST.find { it.id == settings.selectedReciterId }
                        ?: QuranDataSource.RECITERS_LIST.first()

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isReciterPickerOpen = !isReciterPickerOpen }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "القارئ المفضل للتلاوات",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = currentReciter.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldPrimary
                            )
                        }
                        Icon(
                            imageVector = if (isReciterPickerOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = EmeraldPrimary
                        )
                    }

                    if (isReciterPickerOpen) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                QuranDataSource.RECITERS_LIST.forEach { reciter ->
                                    val isSelected = reciter.id == settings.selectedReciterId
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onUpdateSettings(settings.copy(selectedReciterId = reciter.id))
                                                isReciterPickerOpen = false
                                            }
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = reciter.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = EmeraldPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Font Size Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "حجم خط الآيات القرآنية",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${settings.quranFontSize.toInt()} sp",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimary
                            )
                        }

                        Slider(
                            value = settings.quranFontSize,
                            onValueChange = { onUpdateSettings(settings.copy(quranFontSize = it)) },
                            valueRange = 18f..36f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = EmeraldPrimary,
                                activeTrackColor = EmeraldPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Eye comfort switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "وضع القراءة المريحة (Warm Sepia)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "خلفية دافئة مريحة للعين أثناء التلاوة الطويلة",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.enableEyeComfortMode,
                            onCheckedChange = { onUpdateSettings(settings.copy(enableEyeComfortMode = it)) }
                        )
                    }
                }
            }
        }

        // App General Preferences
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "التنبيهات والتفضيلات العامة",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Vibration Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "الاهتزاز التفاعلي مع التسبيح",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "نبضة لمسية خفيفة عند كل تسبيحة وتكرار",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.enableVibrationFeedback,
                            onCheckedChange = { onUpdateSettings(settings.copy(enableVibrationFeedback = it)) }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Morning Athkar Reminder
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "تذكير أذكار الصباح",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "تنبيه يومي مبارك بعد صلاة الفجر",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.morningAthkarReminder,
                            onCheckedChange = { onUpdateSettings(settings.copy(morningAthkarReminder = it)) }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Evening Athkar Reminder
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "تذكير أذكار المساء",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "تنبيه يومي عند إقبال المساء وصلاة العصر",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.eveningAthkarReminder,
                            onCheckedChange = { onUpdateSettings(settings.copy(eveningAthkarReminder = it)) }
                        )
                    }
                }
            }
        }

        // About Application Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "عن تطبيق جنّة",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "تطبيق جنّة - رفيق المسلم اليومي لتلاوة القرآن الكريم وتدبر معانيه، وحفظ الأذكار، ومتابعة شجرة الطاعات والصلوات، والاستفادة من درر وثمار كبار العلماء المحققين.",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = EmeraldContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "الإصدار 1.0.0 • صُمم بإحسان لخدمة كتاب الله وسنة رسوله ﷺ",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimaryDark,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }
}
