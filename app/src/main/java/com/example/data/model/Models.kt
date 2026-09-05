package com.example.data.model

import kotlinx.serialization.Serializable

enum class AppTab {
    HOME, QURAN, ATHKAR, TRACKER, THIMAR, SEARCH, SETTINGS
}

@Serializable
data class SurahMeta(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val revelationType: String, // "Meccan" | "Medinan"
    val numberOfAyahs: Int,
    val startPage: Int,
    val juz: Int
)

@Serializable
data class AyahData(
    val numberInSurah: Int,
    val numberInQuran: Int,
    val text: String,
    val juz: Int,
    val page: Int,
    val hizbQuarter: Int,
    val surahNumber: Int,
    val audioUrl: String? = null,
    val tafseer: String? = null
)

@Serializable
data class ReciterInfo(
    val id: String,
    val name: String,
    val subname: String,
    val category: String,
    val serverUrl: String,
    val hasAyahAudio: Boolean = true
)

@Serializable
data class RelatedQuranVerse(
    val surahName: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val verseText: String,
    val explanation: String,
    val audioUrl: String = ""
)

@Serializable
data class ThikrItem(
    val id: String,
    val text: String,
    val count: Int,
    val repeatCount: Int,
    val currentCount: Int = 0,
    val fadl: String? = null,
    val source: String? = null,
    val audioHint: String? = null,
    val relatedVerse: RelatedQuranVerse? = null
)

@Serializable
data class AthkarCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val iconName: String,
    val colorHex: String,
    val items: List<ThikrItem>
)

@Serializable
data class ThimarahItem(
    val id: String,
    val quote: String,
    val author: String,
    val category: String,
    val source: String? = null,
    val reflectionPrompt: String? = null,
    val era: String? = null,
    val relatedVerse: RelatedQuranVerse? = null
)

@Serializable
data class ScholarProfile(
    val id: String,
    val name: String,
    val shortName: String,
    val title: String,
    val era: String,
    val description: String
)

@Serializable
data class AudioLessonItem(
    val id: String,
    val title: String,
    val scholar: String,
    val category: String,
    val duration: String,
    val durationSeconds: Int,
    val summary: String,
    val audioUrl: String,
    val keyTakeaway: String,
    val tags: List<String> = emptyList(),
    val relatedVerse: RelatedQuranVerse? = null
)

enum class PrayerStatus {
    MISSED, ALONE, CONGREGATION
}

@Serializable
data class DayTrackerData(
    val date: String,
    val fajr: PrayerStatus = PrayerStatus.ALONE,
    val dhuhr: PrayerStatus = PrayerStatus.ALONE,
    val asr: PrayerStatus = PrayerStatus.ALONE,
    val maghrib: PrayerStatus = PrayerStatus.ALONE,
    val isha: PrayerStatus = PrayerStatus.ALONE,
    val habits: Map<String, Boolean> = emptyMap(),
    val quranPagesRead: Int = 0,
    val quranTargetPages: Int = 4,
    val athkarCompletedCount: Int = 0,
    val athkarTargetCount: Int = 2,
    val treeGrowthPercentage: Int = 0
)

@Serializable
data class OneMinuteDeed(
    val id: String,
    val title: String,
    val targetCount: Int,
    val categoryName: String,
    val rewardFadl: String
)

@Serializable
data class Bookmark(
    val id: String,
    val type: String, // "page", "surah", "ayah", "thikr", "thimarah"
    val title: String,
    val subtitle: String,
    val targetId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Serializable
data class AppSettings(
    val isDarkMode: Boolean = false,
    val enableEyeComfortMode: Boolean = false,
    val selectedReciterId: String = "alafasy",
    val quranFontSize: Int = 24,
    val enableAutoScroll: Boolean = false,
    val enableAudioChimes: Boolean = true,
    val dailyMorningReminder: Boolean = true,
    val dailyEveningReminder: Boolean = true
)

@Serializable
data class AyahFullInsight(
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val text: String,
    val ibnKathirTafseer: String,
    val wordMeanings: List<AyahWordMeaning> = emptyList(),
    val asbabNuzul: String? = null,
    val tadabburPoints: List<String> = emptyList(),
    val audioUrl: String? = null
)

@Serializable
data class AyahWordMeaning(
    val word: String,
    val meaning: String
)
