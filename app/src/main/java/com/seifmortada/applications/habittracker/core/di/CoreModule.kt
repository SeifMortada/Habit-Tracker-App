package com.seifmortada.applications.habittracker.core.di

import android.content.Context
import androidx.room.Room
import com.seifmortada.applications.habittracker.core.data.local.HabitDao
import com.seifmortada.applications.habittracker.core.data.local.HabitDatabase
import com.seifmortada.applications.habittracker.core.data.repository.HabitRepositoryImpl
import com.seifmortada.applications.habittracker.core.domain.repository.HabitRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Singleton
    @Provides
    fun provideHabitDatabase(@ApplicationContext context: Context): HabitDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = HabitDatabase::class.java,
            name = "habit_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideHabitDao(habitDatabase: HabitDatabase) = habitDatabase.habitDao

    @Singleton
    @Provides
    fun provideHabitRepository(habitDao: HabitDao): HabitRepository = HabitRepositoryImpl(habitDao)
}