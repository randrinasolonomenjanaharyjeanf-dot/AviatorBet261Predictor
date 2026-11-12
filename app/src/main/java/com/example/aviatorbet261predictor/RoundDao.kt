
package com.example.aviatorbet261predictor

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRound(round: RoundEntity)

    @Query(
        "SELECT * FROM round_results " +
        "WHERE site = :site " +
        "ORDER BY timestamp DESC"
    )
    fun getRoundsBySite(site: String): Flow<List<RoundEntity>>

    @Query("SELECT * FROM round_results ORDER BY timestamp DESC")
    fun getAllRounds(): Flow<List<RoundEntity>>

    @Query("DELETE FROM round_results WHERE site = :site")
    suspend fun deleteRoundsBySite(site: String)

    @Query("DELETE FROM round_results")
    suspend fun deleteAllRounds()

    @Query("SELECT COUNT(*) FROM round_results WHERE site = :site")
    suspend fun countRounds(site: String): Int
}
