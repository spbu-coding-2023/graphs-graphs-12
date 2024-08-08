package utils.filedialogs

import java.io.File

/**
 * Filters files within a given directory based on a provided filter function.
 *
 * @param directory the directory to filter files from
 * @param filter a function that takes a [File] as input and returns a [Boolean]
 * The function should return `true` for files that should be included in the result, and `false` otherwise.
 *
 * @return a list of files that satisfy the provided filter function.
 * If the [directory] is not a valid directory or does not exist, an empty list is returned.
 */
fun filtrate(directory: File, filter: (File) -> Boolean): List<File> {
	if (!directory.isDirectory || !directory.exists()) return emptyList()
	return directory.listFiles()?.filter { filter(it) } ?: emptyList()
}
