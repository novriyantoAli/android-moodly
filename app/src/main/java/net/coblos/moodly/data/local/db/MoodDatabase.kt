package net.coblos.moodly.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MoodEntryEntity::class], version = 1, exportSchema = false)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
}
