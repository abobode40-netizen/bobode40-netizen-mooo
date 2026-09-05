package com.example.data.repository

import android.content.Context
import com.example.data.datasource.AthkarDataSource
import com.example.data.datasource.PrayersDataSource
import com.example.data.datasource.QuranDataSource
import com.example.data.datasource.ThimarDataSource
import com.example.data.local.AppDao
import com.example.data.local.BookmarkEntity
import com.example.data.local.DayTrackerEntity
import com.example.data.local.SettingKeyValue
import com.example.data.model.AppSettings
import com.example.data.model.Bookmark
import com.example.data.model.DayTrackerData
import com.example.data.model.PrayerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository(
    private val appDao: AppDao,
    private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    val allBookmarks: Flow<List<Bookmark>> = appDao.getAllBookmarks().map { list ->
        list.map {
            Bookmark(
                id = it.id,
                type = it.type,
                title = it.title,
                subtitle = it.subtitle,
                targetId = it.targetId,
                createdAt = it.createdAt
            )
        }
    }

    suspend fun addBookmark(bookmark: Bookmark) = withContext(Dispatchers.IO) {
        appDao.insertBookmark(
            BookmarkEntity(
                id = bookmark.id,
                type = bookmark.type,
                title = bookmark.title,
                subtitle = bookmark.subtitle,
                targetId = bookmark.targetId,
                createdAt = bookmark.createdAt
            )
        )
    }

    suspend fun removeBookmark(id: String) = withContext(Dispatchers.IO) {
        appDao.deleteBookmarkById(id)
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    fun getTodayTrackerFlow(date: String = getTodayDateString()): Flow<DayTrackerData> {
        return appDao.getDayTracker(date).map { entity ->
            if (entity == null) {
                DayTrackerData(date = date)
            } else {
                val habitsMap: Map<String, Boolean> = try {
                    json.decodeFromString(entity.habitsJson)
                } catch (e: Exception) {
                    emptyMap()
                }
                DayTrackerData(
                    date = entity.date,
                    fajr = runCatching { PrayerStatus.valueOf(entity.fajr) }.getOrDefault(PrayerStatus.ALONE),
                    dhuhr = runCatching { PrayerStatus.valueOf(entity.dhuhr) }.getOrDefault(PrayerStatus.ALONE),
                    asr = runCatching { PrayerStatus.valueOf(entity.asr) }.getOrDefault(PrayerStatus.ALONE),
                    maghrib = runCatching { PrayerStatus.valueOf(entity.maghrib) }.getOrDefault(PrayerStatus.ALONE),
                    isha = runCatching { PrayerStatus.valueOf(entity.isha) }.getOrDefault(PrayerStatus.ALONE),
                    habits = habitsMap,
                    quranPagesRead = entity.quranPagesRead,
                    quranTargetPages = entity.quranTargetPages,
                    athkarCompletedCount = entity.athkarCompletedCount,
                    athkarTargetCount = entity.athkarTargetCount,
                    treeGrowthPercentage = entity.treeGrowthPercentage
                )
            }
        }
    }

    suspend fun saveDayTracker(tracker: DayTrackerData) = withContext(Dispatchers.IO) {
        val growth = calculateGrowthPercentage(tracker)
        val updated = tracker.copy(treeGrowthPercentage = growth)
        val entity = DayTrackerEntity(
            date = updated.date,
            fajr = updated.fajr.name,
            dhuhr = updated.dhuhr.name,
            asr = updated.asr.name,
            maghrib = updated.maghrib.name,
            isha = updated.isha.name,
            habitsJson = json.encodeToString(updated.habits),
            quranPagesRead = updated.quranPagesRead,
            quranTargetPages = updated.quranTargetPages,
            athkarCompletedCount = updated.athkarCompletedCount,
            athkarTargetCount = updated.athkarTargetCount,
            treeGrowthPercentage = growth
        )
        appDao.insertOrUpdateDayTracker(entity)
    }

    private fun calculateGrowthPercentage(tracker: DayTrackerData): Int {
        var score = 0
        var totalPossible = 0

        // 5 Prayers (each worth 10 points for congregation, 7 for alone, 0 for missed)
        val prayerList = listOf(tracker.fajr, tracker.dhuhr, tracker.asr, tracker.maghrib, tracker.isha)
        prayerList.forEach { p ->
            totalPossible += 10
            when (p) {
                PrayerStatus.CONGREGATION -> score += 10
                PrayerStatus.ALONE -> score += 7
                PrayerStatus.MISSED -> score += 0
            }
        }

        // Quran pages (up to 20 points)
        totalPossible += 20
        val quranRatio = (tracker.quranPagesRead.toFloat() / tracker.quranTargetPages.coerceAtLeast(1)).coerceIn(0f, 1f)
        score += (quranRatio * 20).toInt()

        // Athkar count (up to 15 points)
        totalPossible += 15
        val athkarRatio = (tracker.athkarCompletedCount.toFloat() / tracker.athkarTargetCount.coerceAtLeast(1)).coerceIn(0f, 1f)
        score += (athkarRatio * 15).toInt()

        // Habits (each checked habit worth 3 points)
        val checkedHabits = tracker.habits.values.count { it }
        val habitsCount = PrayersDataSource.DAILY_HABITS_DEFINITIONS.size
        totalPossible += habitsCount * 3
        score += checkedHabits * 3

        return ((score.toFloat() / totalPossible.coerceAtLeast(1)) * 100).toInt().coerceIn(0, 100)
    }

    suspend fun loadSettings(): AppSettings = withContext(Dispatchers.IO) {
        val settingKv = appDao.getSetting("app_settings")
        if (settingKv != null) {
            try {
                json.decodeFromString<AppSettings>(settingKv.value)
            } catch (e: Exception) {
                AppSettings()
            }
        } else {
            AppSettings()
        }
    }

    suspend fun saveSettings(settings: AppSettings) = withContext(Dispatchers.IO) {
        val jsonStr = json.encodeToString(settings)
        appDao.setSetting(SettingKeyValue("app_settings", jsonStr))
    }

    suspend fun getLastReadPage(): Int = withContext(Dispatchers.IO) {
        val kv = appDao.getSetting("last_read_page")
        kv?.value?.toIntOrNull() ?: 1
    }

    suspend fun saveLastReadPage(page: Int) = withContext(Dispatchers.IO) {
        appDao.setSetting(SettingKeyValue("last_read_page", page.toString()))
    }
}
