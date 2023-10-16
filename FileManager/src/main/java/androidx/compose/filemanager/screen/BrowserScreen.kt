package androidx.compose.filemanager.screen

import android.text.TextUtils
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.filemanager.R
import androidx.compose.filemanager.component.EmptyDirectory
import androidx.compose.filemanager.component.FileItem
import androidx.compose.filemanager.component.SimpleTopAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File

@Composable
fun BrowserScreen(
    isFile: Boolean = false,
    viewModel: BrowserViewModel = viewModel(),
    onCloseChange: () -> Unit = {},
    onCheckedChange: (File) -> Unit = {},
) {
    BackHandler(viewModel.canGoBack) { viewModel.goBack() }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
    ) {
        SimpleTopAppBar(title = if (viewModel.currentPathSegment.isEmpty()) stringResource(R.string.app_name) else viewModel.currentPathSegment.last(),
            navigationButton = {
                if (viewModel.canGoBack) {
                    IconButton(onClick = { viewModel.goBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface,
                        )
                    }
                }
            },
            menuButton = {
                IconButton(onClick = onCloseChange) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface,
                    )
                }
            })
        Spacer(modifier = Modifier.height(10.dp))
        FileList(
            isFile = isFile,
            files = viewModel.files,
            onFileClick = { if (it.isDirectory) viewModel.goToDirectory(it) },
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun FileList(
    isFile: Boolean = false,
    files: List<File>,
    onFileClick: (File) -> Unit,
    onCheckedChange: (File) -> Unit = {}
) {
    val context = LocalContext.current
    if (files.isEmpty()) {
        EmptyDirectory(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
        )
    } else {
        var checkedIndex by remember { mutableIntStateOf(-1) }
        var fileName by remember { mutableStateOf("") }
        var filePath by remember { mutableStateOf("") }
        LazyColumn(modifier = Modifier.background(MaterialTheme.colors.surface)) {
            item {
                if (!isFile) OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(id = R.string.file_name)) },
                    value = fileName,
                    singleLine = true,
                    onValueChange = {
                        fileName = it
                        //下面这一个代码块的作用是，解决当选择了文件夹目录后，更改文件名不生效问题。
                        if (filePath != "") {
                            //此行的作用是，当已经选择了文件夹目录，却又把文件名给删除了，此时应该返回一个空路径
                            if (fileName == "") onCheckedChange(File(""))
                            //此行的作用是，当已经选择了文件夹目录，改变文件名后，应当返回最新的path
                            else onCheckedChange(File("${filePath}${File.separator}${fileName}"))
                        }
                    })
            }
            //此行作用是解决当选择文件夹时，在上一级目录已经勾选了，点入下一级目录后同一个index位置的文件夹也会自动勾选的问题。
            checkedIndex = -1
            //此行的作用是 当只写了文件名没有选择目录，应当返回一个空路径
            onCheckedChange(File(""))
            filePath = ""
            itemsIndexed(files, key = { _, item -> item.name }) { index, item ->
                FileItem(
                    isFile = isFile, file = item, onClick = onFileClick, checkedIndex == index
                ) {
                    if (isFile) {
                        checkedIndex = index
                        onCheckedChange(it)
                    } else {
                        if (TextUtils.isEmpty(fileName)) {
                            Toast.makeText(context, R.string.file_name_tips, Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            checkedIndex = index
                            filePath = it.toString()
                            onCheckedChange(File("${filePath}${File.separator}${fileName}"))
                        }
                    }
                }
                Divider(thickness = 0.2.dp, color = MaterialTheme.colors.onSurface)
            }
        }
    }
}