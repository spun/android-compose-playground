package com.spundev.composebasics.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.repositories.PlantsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantsRepository: PlantsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    val pagedPlants: Flow<PagingData<Plant>> = plantsRepository.plants.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            val collections = plantsRepository.getCollections()
            _uiState.value = HomeUiState(plantCollections = collections)
        }
    }

    val setFavorite: (Long, Boolean) -> Unit = { plantId, isFavorite ->
        viewModelScope.launch {
            plantsRepository.setFavorite(plantId, isFavorite)
        }
    }
}
