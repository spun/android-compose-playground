package com.spundev.composebasics.ui.plantDetails

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.ui.components.accordion.Accordion
import com.spundev.composebasics.ui.components.collectionCarousel.CollectionsCarousel

@Composable
fun PlantDetailsScreen(
    uiState: PlantDetailsUiState,
    plant: Plant,
    setFavorite: (Long, Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {

        HeroImage(
            imageUrl = plant.imageUrl,
            price = 45,
            isFavorite = plant.isFavorite,
            setFavorite = { isFavorite ->
                setFavorite(plant.id, isFavorite)
            }
        )

        // Name, description, "Add to cart" button and extra info accordions
        PlantInfo(name = plant.name, modifier = Modifier.padding(16.dp))

        // Collections carousel
        Text(
            text = "Browse similar plants",
            style = MaterialTheme.typography.h2,
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
        )
        CollectionsCarousel(
            carouselState = uiState.carouselState,
            onPlantClick = { }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun HeroImage(imageUrl: String, price: Int, isFavorite: Boolean, setFavorite: (Boolean) -> Unit) {

    val favoriteIcon = if (isFavorite) {
        Icons.Filled.Favorite
    } else {
        Icons.Filled.FavoriteBorder
    }

    Box {
        Image(
            painter = rememberCoilPainter(request = imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.height(216.dp).fillMaxWidth()
        )

        Row(modifier = Modifier.align(Alignment.BottomStart)) {
            // Price tag
            Surface(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.clip(RoundedCornerShape(0.dp, 32.dp, 0.dp, 0.dp))
            ) {
                Text(
                    "$$price",
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        start = 16.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Share button
            Surface(
                color = MaterialTheme.colors.background,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.align(Alignment.CenterVertically),
                elevation = 1.dp
            ) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Favorite button
            Surface(
                color = MaterialTheme.colors.background,
                shape = MaterialTheme.shapes.small,
                elevation = 1.dp,
                modifier = Modifier.align(Alignment.CenterVertically)
                    .clickable { setFavorite(!isFavorite) },
            ) {
                Icon(
                    imageVector = favoriteIcon,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp)
                        .size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun PlantInfo(name: String, modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Column(modifier = modifier) {
        Text(text = name, style = MaterialTheme.typography.h1)
        Text(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque felis lectus, euismod id accumsan nec, viverra non velit. Nulla laoreet hendrerit elit eget tempor. Nullam commodo nibh augue, id pretium urna porttitor nec. Suspendisse ornare lorem id dignissim vulputate. Sed lobortis metus eu mi gravida, id mattis risus lobortis.",
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = {
                // Placeholder Toast
                Toast.makeText(context, "No action", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(vertical = 16.dp).height(48.dp).fillMaxWidth()
        ) {
            Text(text = "Add to cart", style = MaterialTheme.typography.button)
        }
        Divider(thickness = 2.dp, modifier = Modifier.padding(top = 16.dp))
        Accordion(title = "Care instruction") {
            Text(
                text = "No care instructions available.", style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Divider(thickness = 2.dp)
        Accordion(title = "FAQ") {
            Text(
                text = "Empty.", style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        Divider(thickness = 2.dp)
    }
}
