package databases

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * File database.
 * Implementation of its open/close local file to read/write to its some data.
 * Examples of files: `.csv`, `.json` ant ect.
 *
 * @param T type of data to read/write
 */
abstract class FileDatabase<T> {
	/**
	 * Load [file] and convert its data to object of type [T].
	 *
	 * @param file to read
	 * @return object of type [T]
	 */
	fun load(file: File): T {
		val reader = BufferedReader(FileReader(file))
		val result = load(reader)
		reader.close()
		return result
	}

	fun save(file: File, obj: T) {
		val writer = BufferedWriter(FileWriter(file))
		save(writer, obj)
		writer.close()
	}

	protected abstract fun load(reader: BufferedReader): T
	protected abstract fun save(writer: BufferedWriter, obj: T)
}
