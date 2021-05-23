package com.spundev.composebasics.ui.components.accordion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spundev.composebasics.R
import com.spundev.composebasics.ui.theme.ComposeBasicsTheme

@Composable
fun Accordion(
    title: String = "Title",
    content: @Composable () -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }

    val openCloseIcon = if (isOpen) {
        R.drawable.ic_expand_less_24
    } else {
        R.drawable.ic_expand_more_24
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { isOpen = !isOpen }
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(vertical = 16.dp).weight(1f)
            )

            Icon(
                painter = painterResource(id = openCloseIcon),
                contentDescription = null,
            )
        }
        if (isOpen) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccordion() {
    ComposeBasicsTheme {
        Accordion(title = "Click to show more") {
            Text(text = "This is the Accordion content.")
        }
    }
}