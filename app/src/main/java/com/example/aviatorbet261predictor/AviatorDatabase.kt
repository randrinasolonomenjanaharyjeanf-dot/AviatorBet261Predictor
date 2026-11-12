
package com.example.aviatorbet261predictor

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RoundEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AviatorDatabase : RoomDatabase() {

    abstract fun roundDao(): RoundDao

    companion object {

        @Volatile
        private var INSTANCE: AviatorDatabase? = null

        fun getDatabase(context: Context): AviatorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AviatorDatabase::class.java,
                    "aviator_history_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
