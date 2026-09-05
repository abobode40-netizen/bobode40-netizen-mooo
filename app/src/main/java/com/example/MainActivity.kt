package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.AppTab
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.JannatTheme
import com.example.ui.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val currentTab by viewModel.currentTab.collectAsState()
            val settings by viewModel.settings.collectAsState()
            val dayTracker by viewModel.dayTracker.collectAsState()
            val bookmarks by viewModel.bookmarks.collectAsState()
            val currentSurah by viewModel.currentSurah.collectAsState()
            val currentAyahs by viewModel.currentSurahAyahs.collectAsState()
            val lastReadPage by viewModel.lastReadPage.collectAsState()
            val selectedAyahForDetails by viewModel.selectedAyahForDetails.collectAsState()
            val currentAyahInsight by viewModel.currentAyahInsight.collectAsState()
            val selectedAthkarCategoryId by viewModel.selectedAthkarCategoryId.collectAsState()
            val athkarCounters by viewModel.athkarCounters.collectAsState()
            val sebhaCount by viewModel.sebhaCount.collectAsState()
            val sebhaTotalCycles by viewModel.sebhaTotalCycles.collectAsState()
            val sebhaTarget by viewModel.sebhaTarget.collectAsState()
            val sebhaDhikrText by viewModel.sebhaDhikrText.collectAsState()
            val isSebhaDialogOpen by viewModel.isSebhaDialogOpen.collectAsState()
            val voicePracticeAyah by viewModel.voicePracticeAyah.collectAsState()
            val searchQuery by viewModel.searchQuery.collectAsState()
            val audioPlaybackState by viewModel.audioPlayer.playbackState.collectAsState()

            var isBookmarksDialogOpen by remember { mutableStateOf(false) }

            JannatTheme {
                Scaffold(
                    topBar = {
                        val title = when (currentTab) {
                            AppTab.HOME -> "جنّة"
                            AppTab.QURAN -> "المصحف الشريف"
                            AppTab.ATHKAR -> "حصن المسلم والأذكار"
                            AppTab.TRACKER -> "شجرة العبادات"
                            AppTab.THIMAR -> "ثمار المشايخ والدرر"
                            AppTab.SEARCH -> "البحث الشامل"
                            AppTab.SETTINGS -> "الإعدادات"
                        }
                        val subtitle = when (currentTab) {
                            AppTab.HOME -> "رفيقك القرآني والإيماني"
                            AppTab.QURAN -> "سورة ${currentSurah.name} • صفحة ${currentSurah.startPage}"
                            AppTab.ATHKAR -> "غراس الجنة والتحصين"
                            AppTab.TRACKER -> "متابعة الفرائض والسنن"
                            AppTab.THIMAR -> "من هدي السلف الصالح"
                            AppTab.SEARCH -> "ابحث في القرآن والأذكار"
                            AppTab.SETTINGS -> "تخصيص القراءة والتنبيهات"
                        }
                        JannatTopBar(
                            title = title,
                            subtitle = subtitle,
                            onOpenSebha = { viewModel.openSebha() },
                            onOpenBookmarks = { isBookmarksDialogOpen = true }
                        )
                    },
                    bottomBar = {
                        Column {
                            MiniAudioPlayer(
                                playbackState = audioPlaybackState,
                                onTogglePlayPause = { viewModel.audioPlayer.togglePlayPause() },
                                onClose = { viewModel.audioPlayer.stop() }
                            )
                            JannatBottomNavBar(
                                currentTab = currentTab,
                                onTabSelected = { viewModel.selectTab(it) }
                            )
                        }
                    },
                    contentWindowInsets = WindowInsets.systemBars,
                    modifier = Modifier.fillMaxSize().testTag("main_activity_scaffold")
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            AppTab.HOME -> HomeScreen(
                                trackerData = dayTracker,
                                lastReadPage = lastReadPage,
                                onNavigateTab = { viewModel.selectTab(it) },
                                onOpenQuranPage = { viewModel.openSurahByPage(it) },
                                onOpenAthkarCategory = { viewModel.selectAthkarCategory(it) },
                                onOpenSebhaWithDhikr = { text, target -> viewModel.openSebha(text, target) }
                            )
                            AppTab.QURAN -> QuranScreen(
                                currentSurah = currentSurah,
                                currentAyahs = currentAyahs,
                                lastReadPage = lastReadPage,
                                settings = settings,
                                onSelectSurah = { viewModel.selectSurah(it) },
                                onPlaySurah = { num, name -> viewModel.playSurahRecitation(num, name) },
                                onPlayAyah = { ayah, name -> viewModel.playAyahRecitation(ayah, name) },
                                onAyahDetails = { viewModel.openAyahDetails(it) },
                                onVoicePractice = { viewModel.openVoicePractice(it) },
                                onBookmarkPage = { page, name -> viewModel.bookmarkPage(page, name) },
                                onUpdateSettings = { viewModel.updateSettings(it) }
                            )
                            AppTab.ATHKAR -> AthkarScreen(
                                selectedCategoryId = selectedAthkarCategoryId,
                                athkarCounters = athkarCounters,
                                onSelectCategory = { viewModel.selectAthkarCategory(it) },
                                onIncrementThikr = { viewModel.incrementThikrCount(it) },
                                onResetThikr = { viewModel.resetThikrCount(it) },
                                onOpenSebha = { viewModel.openSebha() }
                            )
                            AppTab.TRACKER -> TrackerScreen(
                                trackerData = dayTracker,
                                onUpdatePrayerStatus = { key, status -> viewModel.updatePrayerStatus(key, status) },
                                onToggleHabit = { viewModel.toggleHabit(it) },
                                onIncrementQuranPages = { viewModel.incrementQuranPagesRead() },
                                onDecrementQuranPages = { viewModel.decrementQuranPagesRead() },
                                onOpenSebha = { viewModel.openSebha() }
                            )
                            AppTab.THIMAR -> ThimarScreen(
                                onPlayAudioLesson = { viewModel.playAudioLesson(it) },
                                onOpenQuranSurah = {
                                    viewModel.selectSurahByNumber(it)
                                    viewModel.selectTab(AppTab.QURAN)
                                }
                            )
                            AppTab.SEARCH -> SearchScreen(
                                searchQuery = searchQuery,
                                onQueryChange = { viewModel.setSearchQuery(it) },
                                onOpenSurah = { viewModel.selectSurahByNumber(it) },
                                onOpenAthkarCategory = { viewModel.selectAthkarCategory(it) },
                                onNavigateTab = { viewModel.selectTab(it) }
                            )
                            AppTab.SETTINGS -> SettingsScreen(
                                settings = settings,
                                onUpdateSettings = { viewModel.updateSettings(it) }
                            )
                        }
                    }
                }

                // Sebha Dialog
                SebhaDialog(
                    isOpen = isSebhaDialogOpen,
                    count = sebhaCount,
                    totalCycles = sebhaTotalCycles,
                    target = sebhaTarget,
                    dhikrText = sebhaDhikrText,
                    onIncrement = { viewModel.incrementSebha() },
                    onReset = { viewModel.resetSebha() },
                    onSelectDhikr = { dhikr, target -> viewModel.setSebhaDhikr(dhikr, target) },
                    onClose = { viewModel.closeSebha() }
                )

                // Ayah Tafseer Details Dialog
                TafseerAyahDialog(
                    insight = currentAyahInsight,
                    isOpen = selectedAyahForDetails != null,
                    onPlayAudio = {
                        selectedAyahForDetails?.let {
                            viewModel.playAyahRecitation(it, currentSurah.name)
                        }
                    },
                    onVoicePractice = {
                        val ayah = selectedAyahForDetails
                        viewModel.closeAyahDetails()
                        if (ayah != null) {
                            viewModel.openVoicePractice(ayah)
                        }
                    },
                    onClose = { viewModel.closeAyahDetails() }
                )

                // Voice Practice Dialog
                VoicePracticeDialog(
                    ayah = voicePracticeAyah,
                    isOpen = voicePracticeAyah != null,
                    onClose = { viewModel.closeVoicePractice() }
                )

                // Bookmarks Dialog
                BookmarksDialog(
                    isOpen = isBookmarksDialogOpen,
                    bookmarks = bookmarks,
                    onSelectBookmark = { bm ->
                        val page = bm.targetId.toIntOrNull() ?: 1
                        viewModel.openSurahByPage(page)
                    },
                    onDeleteBookmark = { viewModel.deleteBookmark(it) },
                    onClose = { isBookmarksDialogOpen = false }
                )
            }
        }
    }
}
