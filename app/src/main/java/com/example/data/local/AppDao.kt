package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteBookmarkById(id: String)

    @Query("SELECT * FROM day_tracker WHERE date = :date LIMIT 1")
    fun getDayTracker(date: String): Flow<DayTrackerEntity?>

    @Query("SELECT * FROM day_tracker WHERE date = :date LIMIT 1")
    suspend fun getDayTrackerDirect(date: String): DayTrackerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDayTracker(tracker: DayTrackerEntity)

    @Query("SELECT * FROM settings_kv WHERE `key` = :key LIMIT 1")
    suspend fun getSetting(key: String): SettingKeyValue?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(item: SettingKeyValue)
}
