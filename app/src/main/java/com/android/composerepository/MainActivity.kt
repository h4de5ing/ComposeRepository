package com.android.composerepository

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.composerepository.ui.theme.ComposeRepositoryTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRepositoryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    val coroutineScope = rememberCoroutineScope()
    var name = remember { mutableStateOf("张三") }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(1000)
            name.value = "测试"
        }
    }
    text(name)
}

@Composable
fun text(value: MutableState<String>) {
    Text(text = "Hello ${value.value}!")
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeRepositoryTheme {
        Greeting()
    }
}