package com.spundev.composebasics.ui.components.collectionCarousel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.model.PlantCollection


class CollectionsCarouselState(
    val collections: List<PlantCollection> = emptyList()
    // TODO: Revisit this
    // Note: collections was set as private in the video and I don't get why.
    // If the component receiving the CollectionsCarouselState was only in charge of showing the
    // collection content (the plants) I could understand how it would work, but in the video it's
    // implied that the CollectionsCarousel receiving the CollectionsCarouselState is the one
    // in charge of showing the collections and the plants when a collection is selected.
    // We could use the plantCollection from the Home and PlantDetails UiState but...we have it
    // here and in the video shows CollectionsCarousel receiving just the CollectionsCarouselState.
    // Also, prefetching a list of lists from the database to fill a component that the user might
    // not use at all seems...not ok. I guess that they wanted to explain why not everything has
    // to be tied to the viewModel and they didn't have a better example.
) {
    var selectedIndex: Int? by mutableStateOf(null)
        private set

    val isExpanded: Boolean
        get() = selectedIndex != null

    var plants by mutableStateOf(emptyList<Plant>())
        private set

    fun onCollectionClick(index: Int) {
        if (index >= collections.size || index < 0) return
        if (index == selectedIndex) {
            selectedIndex = null
        } else {
            plants = collections[index].plants
            selectedIndex = index
        }
    }
}