package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val subtitle: String,
    val targetId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "day_tracker")
data class DayTrackerEntity(
    @PrimaryKey val date: String,
    val fajr: String,
    val dhuhr: String,
    val asr: String,
    val maghrib: String,
    val isha: String,
    val habitsJson: String,
    val quranPagesRead: Int,
    val quranTargetPages: Int,
    val athkarCompletedCount: Int,
    val athkarTargetCount: Int,
    val treeGrowthPercentage: Int
)

@Entity(tableName = "settings_kv")
data class SettingKeyValue(
    @PrimaryKey val key: String,
    val value: String
)
