package androidx.compose.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.filemanager.screen.BrowserScreen
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.test.ui.theme.ComposeRepositoryTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.setruth.yangdialog.YangDialog
import com.setruth.yangdialog.YangDialogDefaults

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeRepositoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    Column {
        var dialogShow by remember { mutableStateOf(false) }
        var isFile by remember { mutableStateOf(false) }
        var path by remember { mutableStateOf("") }
        YangDialog(
            title = "文件选择器",
            isShow = dialogShow,
            onCancel = { dialogShow = false },
            onDismissRequest = { dialogShow = false },
            bottomConfig = YangDialogDefaults.bottomConfig(
                confirmTip = stringResource(id = android.R.string.ok),
                cancelTip = stringResource(id = android.R.string.cancel)
            )
        ) {
            BrowserScreen(isFile, onCloseChange = {
                dialogShow = false
            }) {
                dialogShow = false
                path = it.absolutePath
            }
        }
        Button(onClick = {
            isFile = true
            dialogShow = !dialogShow
        }) {
            Text(text = "文件选择")
        }
        Button(onClick = {
            isFile = false
            dialogShow = !dialogShow
        }) {
            Text(text = "文件夹选择")
        }
        Text(text = path)
    }
}