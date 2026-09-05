package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranDataSource
import com.example.data.model.AppSettings
import com.example.data.model.AyahData
import com.example.data.model.SurahMeta
import com.example.ui.theme.*

@Composable
fun QuranScreen(
    currentSurah: SurahMeta,
    currentAyahs: List<AyahData>,
    lastReadPage: Int,
    settings: AppSettings,
    onSelectSurah: (SurahMeta) -> Unit,
    onPlaySurah: (Int, String) -> Unit,
    onPlayAyah: (AyahData, String) -> Unit,
    onAyahDetails: (AyahData) -> Unit,
    onVoicePractice: (AyahData) -> Unit,
    onBookmarkPage: (Int, String) -> Unit,
    onUpdateSettings: (AppSettings) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isSurahPickerOpen by remember { mutableStateOf(false) }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranDataSource.SURAHS_LIST
        } else {
            QuranDataSource.SURAHS_LIST.filter {
                it.name.contains(searchQuery.trim()) ||
                it.englishName.contains(searchQuery.trim(), ignoreCase = true) ||
                it.number.toString() == searchQuery.trim()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("quran_screen")
    ) {
        // Quran Top Control Strip
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Surah selector dropdown trigger
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldContainer,
                        modifier = Modifier
                            .clickable { isSurahPickerOpen = !isSurahPickerOpen }
                            .testTag("surah_picker_trigger")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = EmeraldPrimaryDark,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "سورة ${currentSurah.name} (${currentSurah.numberOfAyahs} آية)",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldPrimaryDark
                            )
                            Icon(
                                imageVector = if (isSurahPickerOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = EmeraldPrimaryDark
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Play full Surah Audio
                        IconButton(
                            onClick = { onPlaySurah(currentSurah.number, currentSurah.name) },
                            modifier = Modifier.testTag("play_surah_audio_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayCircle,
                                contentDescription = "استماع للسورة",
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Bookmark current Surah & Page
                        IconButton(
                            onClick = { onBookmarkPage(currentSurah.startPage, currentSurah.name) },
                            modifier = Modifier.testTag("bookmark_surah_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdd,
                                contentDescription = "حفظ علامة",
                                tint = GoldAccent
                            )
                        }
                    }
                }

                // Quick Reciter info tag & Sepia toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val reciter = QuranDataSource.RECITERS_LIST.find { it.id == settings.selectedReciterId } ?: QuranDataSource.RECITERS_LIST.first()
                    Text(
                        text = "القارئ: ${reciter.name}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "وضع القراءة المريحة",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Switch(
                            checked = settings.enableEyeComfortMode,
                            onCheckedChange = { onUpdateSettings(settings.copy(enableEyeComfortMode = it)) },
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }
            }
        }

        // Surah Picker Sheet/List if open
        if (isSurahPickerOpen) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("ابحث برقم أو اسم السورة...", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(filteredSurahs) { surah ->
                            val isSelected = surah.number == currentSurah.number
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) EmeraldContainer else Color.Transparent,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectSurah(surah)
                                        isSurahPickerOpen = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${surah.number}.",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = EmeraldPrimary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "سورة ${surah.name}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    }
                                    Text(
                                        text = "${surah.numberOfAyahs} آية • صفحة ${surah.startPage}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mushaf Ayahs Reader
        val pageBgColor = if (settings.enableEyeComfortMode) SepiaBackground else MaterialTheme.colorScheme.background
        val cardBgColor = if (settings.enableEyeComfortMode) SepiaSurface else MaterialTheme.colorScheme.surface
        val textColor = if (settings.enableEyeComfortMode) SepiaText else MaterialTheme.colorScheme.onSurface

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(pageBgColor)
                .padding(horizontal = 12.dp)
                .testTag("quran_ayahs_list"),
            contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Surah Header Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "سُورَةُ ${currentSurah.name}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = EmeraldPrimary
                        )
                        Text(
                            text = "${if (currentSurah.revelationType == "Meccan") "مكية" else "مدنية"} • عدد آياتها ${currentSurah.numberOfAyahs} • الجزء ${currentSurah.juz}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (currentSurah.number != 9) { // Surat At-Tawbah doesn't have Basmalah
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                ),
                                color = GoldAccent
                            )
                        }
                    }
                }
            }

            // Ayahs list
            items(currentAyahs) { ayah ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAyahDetails(ayah) }
                        .testTag("ayah_card_${ayah.numberInSurah}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = EmeraldContainer,
                                modifier = Modifier.size(30.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${ayah.numberInSurah}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = EmeraldPrimaryDark
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { onPlayAyah(ayah, currentSurah.name) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.VolumeUp,
                                        contentDescription = "استماع",
                                        tint = EmeraldPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onVoicePractice(ayah) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Mic,
                                        contentDescription = "تسميع",
                                        tint = GoldAccent,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onAyahDetails(ayah) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = "التفسير",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quranic Text with rich spacing
                        Text(
                            text = ayah.text,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = settings.quranFontSize.sp,
                                lineHeight = (settings.quranFontSize * 1.6f).sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Right,
                            color = textColor,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
