package androidx.compose.filemanager.component

import androidx.compose.filemanager.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.filemanager.getFileSizeShortString
import androidx.compose.filemanager.getFullDateTimeString
import java.io.File


@Composable
fun FileItem(
    isFile: Boolean = false,
    file: File,
    onClick: (File) -> Unit,
    checked: Boolean = false,
    onCheckedChange: (File) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .height(80.dp)
            .padding(5.dp)
            .verticalScroll(rememberScrollState())
            .clickable { onClick(file) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = if (file.isDirectory) painterResource(R.drawable.ic_folder)
            else painterResource(R.drawable.ic_file),
            contentDescription = null,
            modifier = Modifier.size(50.dp),
            tint = MaterialTheme.colors.onSurface,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(7f)) {
            Text(
                text = file.name,
                fontSize = 16.sp,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = getFullDateTimeString(file.lastModified()),
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        if (file.isFile) {
            Text(
                text = getFileSizeShortString(file.length()),
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Light),
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        if (isFile) {
            if (file.isFile)
                Checkbox(checked = checked, onCheckedChange = {
                    onCheckedChange(file)
                })
        } else {
            if (file.isDirectory)
                Checkbox(checked = checked, onCheckedChange = {
                    onCheckedChange(file)
                })
        }
    }
}