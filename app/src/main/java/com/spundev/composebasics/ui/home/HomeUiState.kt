package com.spundev.composebasics.ui.home

import com.spundev.composebasics.model.PlantCollection
import com.spundev.composebasics.ui.components.collectionCarousel.CollectionsCarouselState

data class HomeUiState(
    val plantCollections: List<PlantCollection> = emptyList(),
    val loading: Boolean = false,
    val refreshError: Boolean = false,

    // Note: Definition is missing from the video, but they use this to populate CollectionsCarousel
    val carouselState: CollectionsCarouselState = CollectionsCarouselState(plantCollections)
)