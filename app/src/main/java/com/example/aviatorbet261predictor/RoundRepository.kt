
package com.example.aviatorbet261predictor

import kotlinx.coroutines.flow.Flow

class RoundRepository(
    private val roundDao: RoundDao
) {

    fun getRoundsBySite(site: String): Flow<List<RoundEntity>> {
        return roundDao.getRoundsBySite(site)
    }

    fun getAllRounds(): Flow<List<RoundEntity>> {
        return roundDao.getAllRounds()
    }

    suspend fun insertRound(
        site: String,
        multiplier: Double
    ) {
        val round = RoundEntity(
            site = site,
            multiplier = multiplier
        )

        roundDao.insertRound(round)
    }

    suspend fun deleteRoundsBySite(site: String) {
        roundDao.deleteRoundsBySite(site)
    }

    suspend fun deleteAllRounds() {
        roundDao.deleteAllRounds()
    }

    suspend fun countRounds(site: String): Int {
        return roundDao.countRounds(site)
    }
}
