package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.PrayersDataSource
import com.example.data.model.DayTrackerData
import com.example.data.model.PrayerStatus
import com.example.ui.components.TreeOfDeedsGraphic
import com.example.ui.theme.*

@Composable
fun TrackerScreen(
    trackerData: DayTrackerData,
    onUpdatePrayerStatus: (String, PrayerStatus) -> Unit,
    onToggleHabit: (String) -> Unit,
    onIncrementQuranPages: () -> Unit,
    onDecrementQuranPages: () -> Unit,
    onOpenSebha: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("tracker_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Visual Tree of Good Deeds Card
        item {
            TreeOfDeedsGraphic(
                growthPercentage = trackerData.treeGrowthPercentage
            )
        }

        // 5 Daily Prayers Status
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("prayers_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "الصلوات الخمس المكتوبة",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val prayers = listOf(
                        Triple("fajr", "صلاة الفجر", trackerData.fajr),
                        Triple("dhuhr", "صلاة الظهر", trackerData.dhuhr),
                        Triple("asr", "صلاة العصر", trackerData.asr),
                        Triple("maghrib", "صلاة المغرب", trackerData.maghrib),
                        Triple("isha", "صلاة العشاء", trackerData.isha)
                    )

                    prayers.forEach { (key, name, status) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                // Congregation button
                                FilterChip(
                                    selected = status == PrayerStatus.CONGREGATION,
                                    onClick = { onUpdatePrayerStatus(key, PrayerStatus.CONGREGATION) },
                                    label = { Text("جماعة", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrayerCongregation,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.height(32.dp)
                                )

                                // Alone button
                                FilterChip(
                                    selected = status == PrayerStatus.ALONE,
                                    onClick = { onUpdatePrayerStatus(key, PrayerStatus.ALONE) },
                                    label = { Text("منفرداً", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrayerAlone,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.height(32.dp)
                                )

                                // Missed button
                                FilterChip(
                                    selected = status == PrayerStatus.MISSED,
                                    onClick = { onUpdatePrayerStatus(key, PrayerStatus.MISSED) },
                                    label = { Text("قضاء", fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = PrayerMissed,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.height(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Daily Quran Pages Counter Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "الورد القرآني اليومي",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                        Text(
                            text = "قرأت اليوم ${trackerData.quranPagesRead} من أصل ${trackerData.quranTargetPages} صفحات",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onDecrementQuranPages,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "نقصان", modifier = Modifier.size(18.dp))
                        }

                        Text(
                            text = "${trackerData.quranPagesRead}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        )

                        IconButton(
                            onClick = onIncrementQuranPages,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "زيادة", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Sunan & Daily Habits Checklist
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
                        text = "السنن والرواتب وأعمال اليوم",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PrayersDataSource.DAILY_HABITS_DEFINITIONS.forEach { habit ->
                        val isChecked = trackerData.habits[habit.id] ?: false
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleHabit(habit.id) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { onToggleHabit(habit.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = EmeraldPrimary,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = habit.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isChecked) EmeraldPrimaryDark else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
