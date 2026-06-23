package net.coblos.moodly.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.coblos.moodly.data.local.db.MoodDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoodDatabase =
        Room.databaseBuilder(context, MoodDatabase::class.java, "moodly.db").build()

    @Provides
    fun provideMoodDao(db: MoodDatabase) = db.moodDao()
}
