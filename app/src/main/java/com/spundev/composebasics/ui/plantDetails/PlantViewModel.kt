package com.spundev.composebasics.ui.plantDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.repositories.PlantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlantViewModel @Inject constructor(
    plantsRepository: PlantsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlantDetailsUiState(loading = true))
    val uiState: StateFlow<PlantDetailsUiState> = _uiState

    val plantDetails: Flow<Plant> = plantsRepository.getPlantDetails(
        savedStateHandle.get<Int>("id")
    )

    init {
        viewModelScope.launch {
            val collections = plantsRepository.getCollections()
            _uiState.value = PlantDetailsUiState(plantCollections = collections)
        }
    }

    val setFavorite: (Long, Boolean) -> Unit = { plantId, isFavorite ->
        viewModelScope.launch {
            plantsRepository.setFavorite(plantId, isFavorite)
        }
    }
}