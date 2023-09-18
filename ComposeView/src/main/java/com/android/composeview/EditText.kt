package com.android.composeview

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EditText(value: String, modifier: Modifier = Modifier, onValueChange: ((String) -> Unit)) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .border(
                width = 0.5.dp, color = Color.Black, shape = RoundedCornerShape(1.dp)
            )
            .padding(3.dp),
    )
}