package com.example.ui.viewmodels

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.JannatApp
import com.example.data.datasource.AthkarDataSource
import com.example.data.datasource.AyahInsightsDataSource
import com.example.data.datasource.PrayersDataSource
import com.example.data.datasource.QuranDataSource
import com.example.data.datasource.ThimarDataSource
import com.example.data.model.AppTab
import com.example.data.model.AppSettings
import com.example.data.model.AudioLessonItem
import com.example.data.model.AyahData
import com.example.data.model.AyahFullInsight
import com.example.data.model.Bookmark
import com.example.data.model.DayTrackerData
import com.example.data.model.PrayerStatus
import com.example.data.model.ReciterInfo
import com.example.data.model.SurahMeta
import com.example.data.model.ThikrItem
import com.example.data.model.ThimarahItem
import com.example.ui.audio.AudioPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as JannatApp).repository
    val audioPlayer = AudioPlayerManager(application)

    // Navigation & Tab State
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    // Settings
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    // Daily Tracker
    val dayTracker: StateFlow<DayTrackerData> = repository.getTodayTrackerFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DayTrackerData(repository.getTodayDateString()))

    // Bookmarks
    val bookmarks: StateFlow<List<Bookmark>> = repository.allBookmarks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Quran State
    private val _currentSurah = MutableStateFlow<SurahMeta>(QuranDataSource.SURAHS_LIST.first())
    val currentSurah: StateFlow<SurahMeta> = _currentSurah.asStateFlow()

    private val _currentSurahAyahs = MutableStateFlow<List<AyahData>>(QuranDataSource.getAyahsForSurah(1))
    val currentSurahAyahs: StateFlow<List<AyahData>> = _currentSurahAyahs.asStateFlow()

    private val _lastReadPage = MutableStateFlow(1)
    val lastReadPage: StateFlow<Int> = _lastReadPage.asStateFlow()

    private val _selectedAyahForDetails = MutableStateFlow<AyahData?>(null)
    val selectedAyahForDetails: StateFlow<AyahData?> = _selectedAyahForDetails.asStateFlow()

    private val _currentAyahInsight = MutableStateFlow<AyahFullInsight?>(null)
    val currentAyahInsight: StateFlow<AyahFullInsight?> = _currentAyahInsight.asStateFlow()

    // Athkar State
    private val _selectedAthkarCategoryId = MutableStateFlow("morning")
    val selectedAthkarCategoryId: StateFlow<String> = _selectedAthkarCategoryId.asStateFlow()

    private val _athkarCounters = MutableStateFlow<Map<String, Int>>(emptyMap())
    val athkarCounters: StateFlow<Map<String, Int>> = _athkarCounters.asStateFlow()

    // Sebha State
    private val _sebhaCount = MutableStateFlow(0)
    val sebhaCount: StateFlow<Int> = _sebhaCount.asStateFlow()

    private val _sebhaTotalCycles = MutableStateFlow(0)
    val sebhaTotalCycles: StateFlow<Int> = _sebhaTotalCycles.asStateFlow()

    private val _sebhaTarget = MutableStateFlow(33)
    val sebhaTarget: StateFlow<Int> = _sebhaTarget.asStateFlow()

    private val _sebhaDhikrText = MutableStateFlow("سُبْحَانَ اللَّهِ")
    val sebhaDhikrText: StateFlow<String> = _sebhaDhikrText.asStateFlow()

    private val _isSebhaDialogOpen = MutableStateFlow(false)
    val isSebhaDialogOpen: StateFlow<Boolean> = _isSebhaDialogOpen.asStateFlow()

    // Voice Practice State
    private val _voicePracticeAyah = MutableStateFlow<AyahData?>(null)
    val voicePracticeAyah: StateFlow<AyahData?> = _voicePracticeAyah.asStateFlow()

    // Scholar Audio Lesson Modal
    private val _selectedLessonForPlayback = MutableStateFlow<AudioLessonItem?>(null)
    val selectedLessonForPlayback: StateFlow<AudioLessonItem?> = _selectedLessonForPlayback.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        viewModelScope.launch {
            _settings.value = repository.loadSettings()
            _lastReadPage.value = repository.getLastReadPage()
        }
    }

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun updateSettings(newSettings: AppSettings) {
        _settings.value = newSettings
        viewModelScope.launch {
            repository.saveSettings(newSettings)
        }
    }

    fun selectSurah(surah: SurahMeta) {
        _currentSurah.value = surah
        _currentSurahAyahs.value = QuranDataSource.getAyahsForSurah(surah.number)
        _lastReadPage.value = surah.startPage
        viewModelScope.launch {
            repository.saveLastReadPage(surah.startPage)
        }
    }

    fun selectSurahByNumber(surahNumber: Int) {
        val surah = QuranDataSource.SURAHS_LIST.find { it.number == surahNumber }
        if (surah != null) {
            selectSurah(surah)
        }
    }

    fun openSurahByPage(page: Int) {
        val surah = QuranDataSource.SURAHS_LIST.findLast { it.startPage <= page } ?: QuranDataSource.SURAHS_LIST.first()
        selectSurah(surah)
        _lastReadPage.value = page
        _currentTab.value = AppTab.QURAN
        viewModelScope.launch {
            repository.saveLastReadPage(page)
        }
    }

    fun openAyahDetails(ayah: AyahData) {
        _selectedAyahForDetails.value = ayah
        _currentAyahInsight.value = AyahInsightsDataSource.getInsight(ayah.surahNumber, ayah.numberInSurah)
    }

    fun closeAyahDetails() {
        _selectedAyahForDetails.value = null
        _currentAyahInsight.value = null
    }

    fun openVoicePractice(ayah: AyahData) {
        _voicePracticeAyah.value = ayah
    }

    fun closeVoicePractice() {
        _voicePracticeAyah.value = null
    }

    fun playSurahRecitation(surahNumber: Int, surahName: String) {
        val reciter = QuranDataSource.RECITERS_LIST.find { it.id == _settings.value.selectedReciterId }
            ?: QuranDataSource.RECITERS_LIST.first()
        val url = QuranDataSource.getSurahAudioUrl(reciter.id, surahNumber)
        audioPlayer.play(
            url = url,
            title = "سورة $surahName",
            subtitle = "القارئ ${reciter.name}"
        )
    }

    fun playAudioLesson(lesson: AudioLessonItem) {
        _selectedLessonForPlayback.value = lesson
        audioPlayer.play(
            url = lesson.audioUrl,
            title = lesson.title,
            subtitle = lesson.scholar
        )
    }

    fun playAyahRecitation(ayah: AyahData, surahName: String) {
        val url = ayah.audioUrl ?: "https://everyayah.com/data/Alafasy_128kbps/${String.format("%03d%03d", ayah.surahNumber, ayah.numberInSurah)}.mp3"
        audioPlayer.play(
            url = url,
            title = "$surahName - آية ${ayah.numberInSurah}",
            subtitle = "مشاري العفاسي"
        )
    }

    fun bookmarkPage(page: Int, surahName: String) {
        viewModelScope.launch {
            val bm = Bookmark(
                id = "bm_page_${System.currentTimeMillis()}",
                type = "page",
                title = "سورة $surahName (صفحة $page)",
                subtitle = "المصحف الشريف",
                targetId = page.toString()
            )
            repository.addBookmark(bm)
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    // Athkar Methods
    fun selectAthkarCategory(categoryId: String) {
        _selectedAthkarCategoryId.value = categoryId
    }

    fun incrementThikrCount(thikr: ThikrItem) {
        vibrateLight()
        val current = _athkarCounters.value[thikr.id] ?: 0
        val next = (current + 1).coerceAtMost(thikr.repeatCount)
        val updatedMap = _athkarCounters.value.toMutableMap()
        updatedMap[thikr.id] = next
        _athkarCounters.value = updatedMap

        if (next == thikr.repeatCount && current < thikr.repeatCount) {
            // Count completed
            val tracker = dayTracker.value
            val newTracker = tracker.copy(
                athkarCompletedCount = tracker.athkarCompletedCount + 1,
                habits = tracker.habits + ("morning_athkar" to true)
            )
            viewModelScope.launch {
                repository.saveDayTracker(newTracker)
            }
        }
    }

    fun resetThikrCount(thikrId: String) {
        val updatedMap = _athkarCounters.value.toMutableMap()
        updatedMap[thikrId] = 0
        _athkarCounters.value = updatedMap
    }

    // Sebha Methods
    fun openSebha(dhikr: String = "سُبْحَانَ اللَّهِ", target: Int = 33) {
        _sebhaDhikrText.value = dhikr
        _sebhaTarget.value = target
        _isSebhaDialogOpen.value = true
    }

    fun closeSebha() {
        _isSebhaDialogOpen.value = false
    }

    fun incrementSebha() {
        vibrateLight()
        val count = _sebhaCount.value + 1
        val target = _sebhaTarget.value
        if (target > 0 && count >= target) {
            _sebhaCount.value = 0
            _sebhaTotalCycles.value = _sebhaTotalCycles.value + 1
            vibrateSuccess()
        } else {
            _sebhaCount.value = count
        }
    }

    fun resetSebha() {
        _sebhaCount.value = 0
        _sebhaTotalCycles.value = 0
    }

    fun setSebhaDhikr(dhikr: String, target: Int = 33) {
        _sebhaDhikrText.value = dhikr
        _sebhaTarget.value = target
        _sebhaCount.value = 0
    }

    // Tracker Updates
    fun updatePrayerStatus(prayerKey: String, status: PrayerStatus) {
        val current = dayTracker.value
        val updated = when (prayerKey) {
            "fajr" -> current.copy(fajr = status)
            "dhuhr" -> current.copy(dhuhr = status)
            "asr" -> current.copy(asr = status)
            "maghrib" -> current.copy(maghrib = status)
            "isha" -> current.copy(isha = status)
            else -> current
        }
        viewModelScope.launch {
            repository.saveDayTracker(updated)
        }
    }

    fun toggleHabit(habitId: String) {
        val current = dayTracker.value
        val isChecked = current.habits[habitId] ?: false
        val newHabits = current.habits + (habitId to !isChecked)
        val updated = current.copy(habits = newHabits)
        viewModelScope.launch {
            repository.saveDayTracker(updated)
        }
    }

    fun incrementQuranPagesRead() {
        val current = dayTracker.value
        val updated = current.copy(quranPagesRead = current.quranPagesRead + 1)
        viewModelScope.launch {
            repository.saveDayTracker(updated)
        }
    }

    fun decrementQuranPagesRead() {
        val current = dayTracker.value
        if (current.quranPagesRead > 0) {
            val updated = current.copy(quranPagesRead = current.quranPagesRead - 1)
            viewModelScope.launch {
                repository.saveDayTracker(updated)
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun vibrateLight() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createOneShot(25, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (e: Exception) {
            // Ignore if vibration unavailable
        }
    }

    private fun vibrateSuccess() {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50), -1))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 50, 100, 50), -1))
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
