
package com.example.aviatorbet261predictor

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "round_results")
data class RoundEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val site: String,

    val multiplier: Double,

    val timestamp: Long = System.currentTimeMillis()
)
