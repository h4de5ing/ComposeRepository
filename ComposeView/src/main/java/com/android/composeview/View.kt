package com.android.composeview

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .padding(top = 0.dp, start = 0.dp, bottom = 5.dp, end = 0.dp)
            .wrapContentHeight()
    ) { content() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackMenu(
    menuIcon: Boolean = true,
    isBack: Boolean = true,
    backClick: () -> Unit = {},
    title: String = "",
    actions: () -> Unit = {}
) {
    TopAppBar(title = { Text(text = title) }, navigationIcon = {
        if (menuIcon)
            IconButton(onClick = backClick, enabled = isBack) {
                Icon(
                    imageVector = if (isBack) Icons.Filled.ArrowBack else Icons.Filled.Menu,
                    contentDescription = "Navigation icon",
                )
            }
    }, actions = {
        IconButton(onClick = actions) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Navigation icon",
            )
        }
    })
}

