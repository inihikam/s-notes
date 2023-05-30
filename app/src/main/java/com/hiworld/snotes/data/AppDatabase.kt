package com.hiworld.snotes.data

import android.content.Context
import androidx.room.*
import com.hiworld.snotes.model.Notes
import com.hiworld.snotes.model.NotesDAO

@Database(entities = [Notes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NotesDAO

    companion object {

        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

    }

}