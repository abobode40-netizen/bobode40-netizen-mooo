package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.AthkarDataSource
import com.example.data.datasource.QuranDataSource
import com.example.data.datasource.ThimarDataSource
import com.example.data.model.AppTab
import com.example.ui.theme.*

@Composable
fun SearchScreen(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onOpenSurah: (Int) -> Unit,
    onOpenAthkarCategory: (String) -> Unit,
    onNavigateTab: (AppTab) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("الكل") }
    val filters = listOf("الكل", "المصحف", "الأذكار", "الثمار")

    // Filter results
    val cleanQuery = searchQuery.trim()

    val matchedSurahs = remember(cleanQuery) {
        if (cleanQuery.isEmpty()) emptyList()
        else QuranDataSource.SURAHS_LIST.filter {
            it.name.contains(cleanQuery) || it.englishName.contains(cleanQuery, ignoreCase = true)
        }
    }

    val matchedAthkar = remember(cleanQuery) {
        if (cleanQuery.isEmpty()) emptyList()
        else AthkarDataSource.ATHKAR_CATEGORIES.flatMap { cat ->
            cat.items.filter { it.text.contains(cleanQuery) || it.fadl?.contains(cleanQuery) == true }
                .map { Pair(cat, it) }
        }
    }

    val matchedThimar = remember(cleanQuery) {
        if (cleanQuery.isEmpty()) emptyList()
        else ThimarDataSource.THIMAR_QUOTES.filter {
            it.quote.contains(cleanQuery) || it.author.contains(cleanQuery) || it.category.contains(cleanQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("search_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            placeholder = { Text("ابحث في المصحف، الأذكار، أو أقوال العلماء...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "مسح")
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_text_input")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filter chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filters) { f ->
                FilterChip(
                    selected = f == selectedFilter,
                    onClick = { selectedFilter = f },
                    label = { Text(f, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = EmeraldContainer,
                        selectedLabelColor = EmeraldPrimaryDark
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (cleanQuery.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 90.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "اكتب كلمة للبحث الفوري في محتوى التطبيق",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Surahs
                if ((selectedFilter == "الكل" || selectedFilter == "المصحف") && matchedSurahs.isNotEmpty()) {
                    item {
                        Text(
                            text = "السور القرانية (${matchedSurahs.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                    }
                    items(matchedSurahs) { surah ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOpenSurah(surah.number)
                                    onNavigateTab(AppTab.QURAN)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "سورة ${surah.name}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "${surah.numberOfAyahs} آية • صفحة ${surah.startPage}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Athkar
                if ((selectedFilter == "الكل" || selectedFilter == "الأذكار") && matchedAthkar.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "الأذكار والأدعية (${matchedAthkar.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                    }
                    items(matchedAthkar) { (cat, thikr) ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onOpenAthkarCategory(cat.id)
                                    onNavigateTab(AppTab.ATHKAR)
                                }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = cat.title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = EmeraldPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = thikr.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // Thimar
                if ((selectedFilter == "الكل" || selectedFilter == "الثمار") && matchedThimar.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "درر السلف والثمار (${matchedThimar.size})",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                    }
                    items(matchedThimar) { thimarah ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateTab(AppTab.THIMAR) }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = thimarah.author,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GoldAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "«${thimarah.quote}»",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                if (matchedSurahs.isEmpty() && matchedAthkar.isEmpty() && matchedThimar.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "لم يتم العثور على نتائج تطابق: $cleanQuery",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
