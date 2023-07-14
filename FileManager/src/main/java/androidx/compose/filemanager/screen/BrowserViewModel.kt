package androidx.compose.filemanager.screen

import android.os.Environment
import android.util.Log
import androidx.compose.filemanager.listFiles
import androidx.compose.filemanager.model.SortOrder
import androidx.compose.filemanager.model.SortType
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File


class BrowserViewModel : ViewModel() {
    private val storageVolumes: List<File> =
        listOf(Environment.getExternalStorageDirectory().absoluteFile)

    var sortType by mutableStateOf(SortType.Name)
        private set

    var sortOrder by mutableStateOf(SortOrder.Ascending)
        private set

    private val _currentPathSegments = mutableListOf<String>()
    val currentPathSegment: List<String> = _currentPathSegments

    var currentPath by mutableStateOf("")
        private set

    var canGoBack by mutableStateOf(false)
        private set

    var files: List<File> by mutableStateOf(listOf())
        private set

    init {
        setFileList(storageVolumes)
        if (storageVolumes.size == 1) {
            goToDirectory(files[0])
        }
    }

    private fun setFileList(files: List<File>) {
        this.files = when (sortType) {
            SortType.Name -> when (sortOrder) {
                SortOrder.Ascending -> files.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.name })
                SortOrder.Descending -> files.sortedWith(compareByDescending<File> { it.isDirectory }.thenByDescending { it.name })
            }

            SortType.LastModified -> when (sortOrder) {
                SortOrder.Ascending -> files.sortedWith(compareByDescending<File> { it.isDirectory }.thenBy { it.lastModified() })
                SortOrder.Descending -> files.sortedWith(compareByDescending<File> { it.isDirectory }.thenByDescending { it.lastModified() })
            }

            SortType.Size -> when (sortOrder) {
                SortOrder.Ascending -> files.sortedWith(compareByDescending<File> { it.isDirectory }
                    .thenComparing { f1, f2 ->
                        if (f1.isDirectory && f2.isDirectory)
                            return@thenComparing f1.name.compareTo(f2.name) // Sort by name asc instead of size for directories
                        else return@thenComparing (f1.length() - f2.length()).toInt()
                    })

                SortOrder.Descending -> files.sortedWith(compareByDescending<File> { it.isDirectory }
                    .thenComparing { f1, f2 ->
                        if (f1.isDirectory && f2.isDirectory)
                            return@thenComparing f1.name.compareTo(f2.name) // Sort by name asc instead of size for directories
                        else return@thenComparing (f2.length() - f1.length()).toInt()
                    })
            }
        }
    }

    fun goToDirectory(directory: File) {
        viewModelScope.launch {
            canGoBack = if (_currentPathSegments.isEmpty()) {
                _currentPathSegments.add(directory.path)
                storageVolumes.size != 1
            } else {
                _currentPathSegments.add(directory.name)
                true
            }
            currentPath = _currentPathSegments.joinToString("/")
            Log.d("", "goToDirectory: $currentPath")
            setFileList(listFiles(currentPath))
        }
    }

    fun goBack() {
        viewModelScope.launch {
            if (_currentPathSegments.isEmpty()) return@launch
            if (storageVolumes.size == 1 && _currentPathSegments.size == 1) return@launch
            _currentPathSegments.removeLast()
            currentPath = _currentPathSegments.joinToString("/")
            canGoBack = if (_currentPathSegments.isEmpty()) {
                setFileList(storageVolumes)
                false
            } else {
                setFileList(listFiles(currentPath))
                !(storageVolumes.size == 1 && _currentPathSegments.size == 1)
            }
        }
    }
}