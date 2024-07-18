package utils.filedialogs

import java.io.File

fun filtrate(directory: File, filter: (File) -> Boolean): List<File> {
	if (!directory.isDirectory || !directory.exists()) return emptyList()

	return directory.listFiles()?.filter { filter(it) } ?: emptyList()
}
