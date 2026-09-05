package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AppTab
import com.example.data.model.AyahData
import com.example.data.model.AyahFullInsight
import com.example.data.model.Bookmark
import com.example.ui.audio.AudioPlaybackState
import com.example.ui.theme.*

@Composable
fun JannatTopBar(
    title: String,
    subtitle: String? = null,
    onOpenSebha: () -> Unit,
    onOpenBookmarks: () -> Unit,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("jannat_top_bar")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showBackButton) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag("back_button")
                        ) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = Color.White)
                        }
                    }
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color.White
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = GoldLight
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onOpenBookmarks,
                        modifier = Modifier.testTag("open_bookmarks_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmark,
                            contentDescription = "العلامات المرجعية",
                            tint = Color.White
                        )
                    }
                    IconButton(
                        onClick = onOpenSebha,
                        modifier = Modifier.testTag("open_sebha_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Adjust,
                            contentDescription = "المسبحة الإلكترونية",
                            tint = GoldAccent
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JannatBottomNavBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        val navItems = listOf(
            Triple(AppTab.HOME, "الرئيسية", Icons.Default.Home),
            Triple(AppTab.QURAN, "المصحف", Icons.Default.AutoStories),
            Triple(AppTab.ATHKAR, "الأذكار", Icons.Default.Favorite),
            Triple(AppTab.TRACKER, "الشجرة", Icons.Default.Park),
            Triple(AppTab.THIMAR, "الثمار", Icons.Default.Lightbulb),
            Triple(AppTab.SEARCH, "البحث", Icons.Default.Search),
            Triple(AppTab.SETTINGS, "الإعدادات", Icons.Default.Settings)
        )

        navItems.forEach { (tab, label, icon) ->
            val isSelected = currentTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) EmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = EmeraldContainer
                ),
                modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
            )
        }
    }
}

@Composable
fun MiniAudioPlayer(
    playbackState: AudioPlaybackState,
    onTogglePlayPause: () -> Unit,
    onClose: () -> Unit
) {
    AnimatedVisibility(
        visible = playbackState.url.isNotEmpty(),
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("mini_audio_player")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                val progress = if (playbackState.durationMs > 0) {
                    playbackState.currentPositionMs.toFloat() / playbackState.durationMs
                } else 0f

                LinearProgressIndicator(
                    progress = { progress.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = GoldAccent,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = playbackState.title.ifEmpty { "تلاوة مباركة" },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = playbackState.subtitle,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (playbackState.isBuffering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 2.dp,
                                color = EmeraldPrimary
                            )
                        } else {
                            IconButton(
                                onClick = onTogglePlayPause,
                                modifier = Modifier.testTag("audio_toggle_button")
                            ) {
                                Icon(
                                    imageVector = if (playbackState.isPlaying) Icons.Default.PauseCircleFilled else Icons.Default.PlayCircleFilled,
                                    contentDescription = if (playbackState.isPlaying) "إيقاف مؤقت" else "تشغيل",
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.testTag("audio_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "إغلاق",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SebhaDialog(
    isOpen: Boolean,
    count: Int,
    totalCycles: Int,
    target: Int,
    dhikrText: String,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    onSelectDhikr: (String, Int) -> Unit,
    onClose: () -> Unit
) {
    if (!isOpen) return

    val presets = listOf(
        Pair("سُبْحَانَ اللَّهِ", 33),
        Pair("الْحَمْدُ لِلَّهِ", 33),
        Pair("اللَّهُ أَكْبَرُ", 33),
        Pair("أَسْتَغْفِرُ اللَّهَ", 100),
        Pair("لَا إِلَهَ إِلَّا اللَّهُ", 100),
        Pair("اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ", 100),
        Pair("لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", 100)
    )

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("sebha_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المسبحة الإلكترونية",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = dhikrText,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Big tactile circular button
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(EmeraldPrimaryLight, EmeraldPrimaryDark)
                            )
                        )
                        .border(4.dp, GoldAccent, CircleShape)
                        .clickable { onIncrement() }
                        .testTag("sebha_tap_circle"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$count",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 48.sp
                            ),
                            color = Color.White
                        )
                        Text(
                            text = if (target > 0) "/ $target" else "حر",
                            style = MaterialTheme.typography.bodySmall,
                            color = GoldLight
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "مجموع الدورات المكتملة: $totalCycles",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Presets
                Text(
                    text = "اختر الذكر:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.take(3).forEach { (presetText, presetTarget) ->
                        OutlinedButton(
                            onClick = { onSelectDhikr(presetText, presetTarget) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                        ) {
                            Text(presetText.split(" ").firstOrNull() ?: presetText, fontSize = 11.sp, maxLines = 1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onReset) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("تصفير العداد", color = MaterialTheme.colorScheme.error)
                    }
                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                    ) {
                        Text("تم", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun TreeOfDeedsGraphic(
    growthPercentage: Int,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("tree_of_deeds_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "شجرة العبادات اليومية",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )
                    Text(
                        text = "تنمو وتزهر بطاعاتك وصلواتك وأذكارك",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldContainer
                ) {
                    Text(
                        text = "$growthPercentage%",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimaryDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Drawing for dynamic flourishing Tree
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val trunkBaseX = width / 2
                    val trunkBaseY = height - 10f

                    // Soil ground arc
                    drawArc(
                        color = Color(0xFF8D6E63),
                        startAngle = 0f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(trunkBaseX - 70f, trunkBaseY - 10f),
                        size = Size(140f, 20f)
                    )

                    // Trunk
                    val trunkPath = Path().apply {
                        moveTo(trunkBaseX - 10f, trunkBaseY)
                        quadraticTo(trunkBaseX - 6f, height * 0.6f, trunkBaseX - 14f, height * 0.45f)
                        lineTo(trunkBaseX + 14f, height * 0.45f)
                        quadraticTo(trunkBaseX + 6f, height * 0.6f, trunkBaseX + 10f, trunkBaseY)
                        close()
                    }
                    drawPath(trunkPath, color = Color(0xFF5D4037))

                    // Foliage based on growth
                    val foliageRadius = 25f + (growthPercentage * 0.45f)
                    val leafColor = when {
                        growthPercentage < 25 -> Color(0xFF81C784)
                        growthPercentage < 60 -> Color(0xFF4CAF50)
                        growthPercentage < 85 -> Color(0xFF2E7D32)
                        else -> Color(0xFF1B5E20)
                    }

                    // Draw canopy circles
                    drawCircle(
                        color = leafColor.copy(alpha = 0.9f),
                        radius = foliageRadius,
                        center = Offset(trunkBaseX, height * 0.35f)
                    )
                    drawCircle(
                        color = leafColor.copy(alpha = 0.85f),
                        radius = foliageRadius * 0.8f,
                        center = Offset(trunkBaseX - 28f, height * 0.42f)
                    )
                    drawCircle(
                        color = leafColor.copy(alpha = 0.85f),
                        radius = foliageRadius * 0.8f,
                        center = Offset(trunkBaseX + 28f, height * 0.42f)
                    )

                    // Golden fruits / blossoms if growth is high
                    if (growthPercentage >= 40) {
                        val fruitCount = (growthPercentage / 15).coerceAtMost(7)
                        val fruitOffsets = listOf(
                            Offset(trunkBaseX - 15f, height * 0.32f),
                            Offset(trunkBaseX + 15f, height * 0.30f),
                            Offset(trunkBaseX, height * 0.22f),
                            Offset(trunkBaseX - 25f, height * 0.42f),
                            Offset(trunkBaseX + 25f, height * 0.40f),
                            Offset(trunkBaseX - 5f, height * 0.38f),
                            Offset(trunkBaseX + 8f, height * 0.25f)
                        )
                        for (i in 0 until fruitCount.coerceAtMost(fruitOffsets.size)) {
                            drawCircle(
                                color = GoldAccent,
                                radius = 5.5f,
                                center = fruitOffsets[i]
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { (growthPercentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = EmeraldPrimary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }
    }
}

@Composable
fun TafseerAyahDialog(
    insight: AyahFullInsight?,
    isOpen: Boolean,
    onPlayAudio: () -> Unit,
    onVoicePractice: () -> Unit,
    onClose: () -> Unit
) {
    if (!isOpen || insight == null) return

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("tafseer_dialog")
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "سورة ${insight.surahName} - آية ${insight.ayahNumber}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, contentDescription = "إغلاق")
                        }
                    }
                }

                item {
                    Surface(
                        color = CreamSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = insight.text,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                lineHeight = 28.sp
                            ),
                            textAlign = TextAlign.Center,
                            color = TextPrimaryLight,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onPlayAudio,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("استماع للآية", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onVoicePractice,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تسميع صوتي", fontSize = 12.sp)
                        }
                    }
                }

                item {
                    Text(
                        text = "تفسير الآية (ابن كثير والميسر):",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )
                    Text(
                        text = insight.ibnKathirTafseer,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (insight.wordMeanings.isNotEmpty()) {
                    item {
                        Text(
                            text = "معاني المفردات:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                    }
                    items(insight.wordMeanings) { wm ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = "• ${wm.word}: ",
                                fontWeight = FontWeight.Bold,
                                color = GoldAccent,
                                fontSize = 13.sp
                            )
                            Text(
                                text = wm.meaning,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (!insight.asbabNuzul.isNullOrEmpty()) {
                    item {
                        Text(
                            text = "سبب النزول:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                        Text(
                            text = insight.asbabNuzul,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (insight.tadabburPoints.isNotEmpty()) {
                    item {
                        Text(
                            text = "لطائف وتدبرات:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldPrimary
                        )
                    }
                    items(insight.tadabburPoints) { pt ->
                        Text(
                            text = "✦ $pt",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoicePracticeDialog(
    ayah: AyahData?,
    isOpen: Boolean,
    onClose: () -> Unit
) {
    if (!isOpen || ayah == null) return

    var isRecording by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("voice_practice_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "التسميع والتلاوة الصوتية",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = CreamSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ayah.text,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            lineHeight = 28.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = TextPrimaryLight,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) Color(0xFFEF4444) else EmeraldPrimary)
                        .clickable {
                            isRecording = !isRecording
                            if (isRecording) {
                                recognizedText = "جاري الاستماع لتلاوتك العطرة..."
                                feedbackMessage = ""
                            } else {
                                recognizedText = ayah.text
                                feedbackMessage = "ما شاء الله! تلاوة مباركة ومتقنة بنسبة ٩٨٪"
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                        contentDescription = "تسجيل",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isRecording) "اضغط لإيقاف التسميع والتحليل" else "اضغط على الميكروفون وابدأ التلاوة",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (recognizedText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = recognizedText,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimaryDark,
                        textAlign = TextAlign.Center
                    )
                }

                if (feedbackMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = EmeraldContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = feedbackMessage,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = OnEmeraldContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookmarksDialog(
    isOpen: Boolean,
    bookmarks: List<Bookmark>,
    onSelectBookmark: (Bookmark) -> Unit,
    onDeleteBookmark: (String) -> Unit,
    onClose: () -> Unit
) {
    if (!isOpen) return

    Dialog(onDismissRequest = onClose) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("bookmarks_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "العلامات المرجعية المحفوظة",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldPrimary
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "إغلاق")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (bookmarks.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لا توجد علامات مرجعية محفوظة بعد.\nاضغط على أيقونة الحفظ في المصحف لتثبيت مواضع قراءتك.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(bookmarks) { bm ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectBookmark(bm)
                                        onClose()
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = bm.title,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Text(
                                            text = bm.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(onClick = { onDeleteBookmark(bm.id) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = MaterialTheme.colorScheme.error
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
