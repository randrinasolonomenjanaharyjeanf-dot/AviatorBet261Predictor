
package com.example.aviatorbet261predictor

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoundViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RoundRepository(
        AviatorDatabase.getDatabase(application).roundDao()
    )

    private val _rounds =
        MutableStateFlow<List<RoundEntity>>(emptyList())

    val rounds: StateFlow<List<RoundEntity>> =
        _rounds.asStateFlow()

    private var observeJob: Job? = null

    fun loadRounds(site: String) {
        observeJob?.cancel()

        observeJob = viewModelScope.launch {
            repository.getRoundsBySite(site.uppercase())
                .collect { results ->
                    _rounds.value = results
                }
        }
    }

    fun addRound(site: String, multiplier: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertRound(
                site.uppercase(),
                multiplier
            )
        }
    }

    fun clearHistory(site: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRoundsBySite(site.uppercase())
        }
    }
}
