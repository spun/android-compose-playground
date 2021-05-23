package com.spundev.composebasics.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.insets.statusBarsHeight
import com.spundev.composebasics.R
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.ui.components.collectionCarousel.CollectionsCarousel
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    plants: Flow<PagingData<Plant>>,
    onPlantClick: (Plant) -> Unit,
    setFavorite: (Long, Boolean) -> Unit,
) {
    // Note: We Want the search bar and the collections to scroll with the plant list.
    // To do this, we are going to pass all these Composable as a parameter to the list and
    // set them as a "header" of the PlantList.

    // List of plants
    PlantList(
        plants = plants,
        header = {
            ListHeader(uiState, onPlantClick)
        },
        onPlantSelected = onPlantClick,
        onPlantFavorite = { plant: Plant, isFavorite: Boolean ->
            setFavorite(plant.id, isFavorite)
        },
        // We cannot set the padding here since the carousel needs to have 0 horizontal padding
        // modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )

    /*
    if (uiState.loading) {
        //...
    } else {
        //...
    }
    */
}


@Composable
fun ListHeader(uiState: HomeUiState, onPlantClick: (Plant) -> Unit) {

    Column {
        // Avoid the transparent status bar
        Spacer(Modifier.statusBarsHeight().fillMaxWidth())

        // Search box
        SearchPlants(modifier = Modifier.padding(horizontal = 16.dp))

        // Collections header
        Text(
            text = "Browse collections",
            style = MaterialTheme.typography.h1,
            modifier = Modifier.paddingFromBaseline(top = 32.dp).padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Collections carousel
        CollectionsCarousel(
            carouselState = uiState.carouselState,
            onPlantClick = onPlantClick
        )

        // Plants list header
        Row(modifier = Modifier.paddingFromBaseline(top = 40.dp).padding(horizontal = 16.dp)) {
            Text(
                text = "Design your home garden",
                style = MaterialTheme.typography.h1,
                modifier = Modifier.weight(1f)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_filter_list_24),
                contentDescription = null,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPlants(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text(text = "Search", style = MaterialTheme.typography.body1) },
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        },
        singleLine = true,
        modifier = modifier.fillMaxWidth()
    )
}


@Composable
fun PlantList(
    plants: Flow<PagingData<Plant>>,
    header: @Composable () -> Unit,
    onPlantSelected: (Plant) -> Unit,
    onPlantFavorite: (Plant, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    // Collect paginated plants
    val pagedPlantItems = plants.collectAsLazyPagingItems()

    LazyColumn(modifier) {

        item {
            header()
        }

        itemsIndexed(pagedPlantItems) { _, plant ->
            if (plant != null) {
                PlantItem(
                    plant,
                    onPlantSelected = onPlantSelected,
                    onPlantFavorite = { isFavorite -> onPlantFavorite(plant, isFavorite) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )
            } else {
                PlantPlaceholder()
            }
        }

        if (pagedPlantItems.loadState.append == LoadState.Loading) {
            item { LoadingIndicator() }
        }
    }
}

@Composable
fun PlantItem(
    plant: Plant = Plant(),
    onPlantSelected: (Plant) -> Unit = {},
    onPlantFavorite: (Boolean) -> Unit = {},
    modifier: Modifier
) {
    val favoriteIcon = if (plant.isFavorite) {
        Icons.Filled.Favorite
    } else {
        Icons.Filled.FavoriteBorder
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(64.dp).clickable { onPlantSelected(plant) }
    ) {
        // Plant image
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.height(64.dp).width(64.dp)
        ) {
            Image(
                painter = rememberCoilPainter(request = plant.imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(start = 8.dp)) {
            Row(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                // Plant name and description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plant.name,
                        style = MaterialTheme.typography.h2,
                        modifier = Modifier.paddingFromBaseline(top = 24.dp)
                    )
                    Text(
                        text = plant.description,
                        style = MaterialTheme.typography.body1,
                    )
                }
                // Favorite button
                IconButton(onClick = { onPlantFavorite(!plant.isFavorite) }) {
                    Icon(
                        imageVector = favoriteIcon,
                        contentDescription = null // decorative element
                    )
                }
            }
            // Divider line
            Divider(thickness = 2.dp)
        }
    }
}

@Composable
fun PlantPlaceholder() {
    Text(text = "Placeholder text for the PlantItem !!")
}

@Composable
fun LoadingIndicator() {
    Text(text = "Placeholder for the Loading Indicator")
}
