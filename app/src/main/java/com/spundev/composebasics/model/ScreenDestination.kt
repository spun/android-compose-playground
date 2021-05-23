package com.spundev.composebasics.model

import androidx.compose.ui.graphics.vector.ImageVector

data class ScreenDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    /* @StringRes val resourceId: Int*/
)