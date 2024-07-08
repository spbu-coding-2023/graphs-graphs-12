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
abstract class FileDatabase<T>(private val fileSuffix: String) {
	/**
	 * Open [file] and convert its data to object of type [T].
	 *
	 * @param file to read
	 * @return object of type [T]
	 *
	 * @throws IOException if file cannot open or cannot read its data
	 */
	fun load(file: File): T {
		val realFile = if (file.name.endsWith(fileSuffix)) file else File(file.parent, "${file.name}$fileSuffix")
		val reader = BufferedReader(FileReader(realFile))
		val result = load(reader)
		reader.close()
		return result
	}

	/**
	 * Save object of type [T] to [file].
	 *
	 * @param file to write on it
	 * @param obj to convert it to file data
	 *
	 * @throws IOException if file cannot open or cannot write its data
	 */
	fun save(file: File, obj: T) {
		val realFile = if (file.name.endsWith(fileSuffix)) file else File(file.parent, "${file.name}$fileSuffix")
		val writer = BufferedWriter(FileWriter(realFile))
		save(writer, obj)
		writer.close()
	}

	/**
	 * Convert [reader] data to object of type [T].
	 *
	 * @param reader to load object
	 * @return object of type [T]
	 */
	protected abstract fun load(reader: BufferedReader): T

	/**
	 * Save object of type [T] to [writer].
	 *
	 * @param writer to write object data on its
	 * @param obj to convert it to write data
	 */
	protected abstract fun save(writer: BufferedWriter, obj: T)
}
