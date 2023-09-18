package com.android.composeview

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
private fun <T> SpinnerView(
    modifier: Modifier = Modifier,
    dropDownModifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T, Int) -> Unit,
    selectedItemFactory: @Composable (Modifier, T) -> Unit,
    dropdownItemFactory: @Composable (T, Int) -> Unit,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        selectedItemFactory(Modifier.clickable { expanded = true }, selectedItem)
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = dropDownModifier
        ) {
            items.forEachIndexed { index, element ->
                DropdownMenuItem(text = {
                    dropdownItemFactory(element, index)
                }, onClick = {
                    onItemSelected(items[index], index)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun Spinner(
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String, Int) -> Unit
) {
    SpinnerView(
        modifier = Modifier
            .wrapContentSize()
            .border(
                width = 0.5.dp,
                color = Color.Black,
                shape = RoundedCornerShape(1.dp)
            ),
        dropDownModifier = Modifier.wrapContentSize(),
        items = items,
        selectedItem = selectedItem,
        onItemSelected = onItemSelected,
        selectedItemFactory = { modifier, item ->
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .padding(8.dp)
                    .wrapContentSize()
            ) {
                Text(item, modifier = Modifier.wrapContentWidth())
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "drop down arrow"
                )
            }
        },
        dropdownItemFactory = { item, _ ->
            Text(text = item)
        }
    )
}