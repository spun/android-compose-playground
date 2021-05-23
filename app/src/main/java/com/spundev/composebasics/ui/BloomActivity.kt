package com.spundev.composebasics.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spundev.composebasics.model.Plant
import com.spundev.composebasics.model.ScreenDestination
import com.spundev.composebasics.ui.home.HomeScreen
import com.spundev.composebasics.ui.home.HomeViewModel
import com.spundev.composebasics.ui.plantDetails.PlantDetailsScreen
import com.spundev.composebasics.ui.plantDetails.PlantViewModel
import com.spundev.composebasics.ui.theme.ComposeBasicsTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class BloomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Transparent system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Note: In the video they use Scaffold here in the setContent directly.
            // The use of the theme is missing from the video and I need to check what's the best
            // way of organizing the navigation code. For now I'm using a BloomApp composable that
            // receives the nav controller for the bottom navigation bar.

            val navController = rememberNavController()

            BloomApp(navController = navController) {
                NavHost(navController, startDestination = "home") {
                    composable(route = "home") {
                        val homeViewModel: HomeViewModel = hiltViewModel()
                        val uiState by homeViewModel.uiState.collectAsState()
                        val plantList = homeViewModel.pagedPlants
                        val setFavorite = homeViewModel.setFavorite

                        HomeScreen(uiState, plantList, onPlantClick = { plant ->
                            navController.navigate("plant/${plant.id}")
                        }, setFavorite = setFavorite)
                    }
                    composable(
                        route = "plant/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) {
                        val plantViewModel: PlantViewModel = hiltViewModel()
                        val uiState by plantViewModel.uiState.collectAsState()
                        val plant by plantViewModel.plantDetails.collectAsState(/* TODO: Remove initial state */
                            Plant("name", "description", false, "")
                        )
                        val setFavorite = plantViewModel.setFavorite

                        PlantDetailsScreen(uiState, plant, setFavorite = setFavorite)
                    }
                    composable(route = "favorites") {
                        FakeScreen("favorites")
                    }
                    composable(route = "profile") {
                        FakeScreen("profile")
                    }
                    composable(route = "cart") {
                        FakeScreen("cart")
                    }
                }
            }
        }
    }
}

@Composable
fun BloomApp(
    navController: NavHostController,
    content: @Composable () -> Unit
) {
    ComposeBasicsTheme {
        // Remember a SystemUiController
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = MaterialTheme.colors.isLight
        val primaryColor = MaterialTheme.colors.primary

        SideEffect {
            systemUiController.setStatusBarColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )

            systemUiController.setNavigationBarColor(
                color = primaryColor,
                darkIcons = useDarkIcons
            )
        }

        ProvideWindowInsets {
            Scaffold(
                bottomBar = {
                    BottomBar(
                        navController = navController,
                        modifier = Modifier.navigationBarsPadding()
                    )
                },
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    content()
                }
            }
        }
    }
}


@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {

    // Destinations
    val items = listOf(
        ScreenDestination("home", "Home", Icons.Filled.Home),
        ScreenDestination("favorites", "Favorites", Icons.Filled.FavoriteBorder),
        ScreenDestination("profile", "Profile", Icons.Filled.AccountCircle),
        ScreenDestination("cart", "Cart", Icons.Filled.ShoppingCart),
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary, // Instead of primarySurface
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, null) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        // restoreState = true
                    }
                }
            )
        }
    }
}

/**
 * A fake screen composable to show when the user navigates to a not implemented destination using
 * the bottom navigation bar.
 */
@Composable
fun FakeScreen(screenLabel: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Nothing here. This is just a placeholder to test the bottom nav.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(24.dp)
        )
        Text(
            text = "You are in ${screenLabel.toUpperCase(Locale.getDefault())}",
            style = MaterialTheme.typography.h1,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}