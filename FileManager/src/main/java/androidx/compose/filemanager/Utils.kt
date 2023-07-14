package androidx.compose.filemanager

import java.io.File
import java.text.DateFormat
import java.text.StringCharacterIterator
import java.util.Date

fun getFullDateTimeString(timestamp: Long): String {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
        .format(Date(timestamp))
}

fun getFileSizeShortString(sizeByte: Long): String {
    if (-1000 < sizeByte && sizeByte < 1000) {
        return "$sizeByte B"
    }
    val ci = StringCharacterIterator("kMGTPE")
    var bytes = sizeByte
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return "%.2f %cB".format(bytes / 1000.0, ci.current())
}

fun listFiles(path: String): List<File> {
    val parent = File(path)
    return parent.listFiles()/*?.filter { it.isDirectory }*/?.toList() ?: listOf()
}