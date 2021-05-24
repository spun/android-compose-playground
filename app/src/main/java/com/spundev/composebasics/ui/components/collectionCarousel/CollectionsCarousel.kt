package com.spundev.composebasics.ui.components.collectionCarousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.model.PlantCollection

@Composable
fun CollectionsCarousel(
    carouselState: CollectionsCarouselState,
    onPlantClick: (Plant) -> Unit
) {
    val selectedIndex = carouselState.selectedIndex

    // We use selectedIndex instead of isExpanded to avoid the null check in the else block
    if (selectedIndex == null) {
        CollapsedCarousel(
            collections = carouselState.collections,
            onCollectionClick = { collectionIndex ->
                carouselState.onCollectionClick(collectionIndex)
            }
        )
    } else {
        val selectedCollection = carouselState.collections[selectedIndex]
        ExpandedCarousel(
            selectedCollection = selectedCollection,
            onCarouselClose = { carouselState.onCollectionClick(selectedIndex) },
            onPlantClick = { /*onPlantClick*/ /* Do not use with fake data */ }
        )
    }
}

@Composable
fun CollapsedCarousel(
    collections: List<PlantCollection>,
    onCollectionClick: (Int) -> Unit
) {
    LazyRow {
        // initial space
        item {
            Spacer(modifier = Modifier.width(16.dp))
        }

        itemsIndexed(collections) { index, plantCollection ->

            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.height(96.dp).width(136.dp)
            ) {
                Box(modifier = Modifier.clickable { onCollectionClick(index) }) {
                    Image(
                        painter = rememberCoilPainter(request = plantCollection.imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient on top of image
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0.6f to Color.Transparent,
                                    1.0f to Color.Black
                                )
                            )
                    )

                    Text(
                        text = plantCollection.name,
                        style = MaterialTheme.typography.h2,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        // end space
        item {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun ExpandedCarousel(
    selectedCollection: PlantCollection,
    onCarouselClose: () -> Unit = {},
    onPlantClick: (Plant) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {

        Column {
            // Selected collection image
            Box(modifier = Modifier.height(96.dp).clickable { onCarouselClose() }) {
                Image(
                    painter = rememberCoilPainter(request = selectedCollection.imageUrl),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp, 4.dp, 0.dp, 0.dp))
                )

                // Gradient on top of image
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.6f to Color.Transparent,
                                1.0f to Color.Black
                            )
                        )
                )

                Text(
                    text = selectedCollection.name,
                    style = MaterialTheme.typography.h2,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Plants
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.width(8.dp))

                selectedCollection.plants.forEach { plant ->
                    Column(
                        modifier = Modifier.weight(1f).clickable { onPlantClick(plant) }
                    ) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.height(80.dp)
                        ) {
                            Image(
                                painter = rememberCoilPainter(request = plant.imageUrl),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                            )
                        }
                        Text(text = plant.name, style = MaterialTheme.typography.h2)
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
