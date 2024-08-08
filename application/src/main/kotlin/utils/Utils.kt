package utils

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize

/**
 * Checks if the current [IntSize] is larger or equal to the given [other] [IntSize] in both width and height.
 *
 * @param other The [IntSize] to compare with the current one.
 *
 * @return `true` if the current [IntSize] is larger or equal to the given [other] [IntSize] in both dimensions;
 *         `false` otherwise.
 */
fun IntSize.isLargeOnParamsThen(other: IntSize): Boolean {
	return width >= other.width && height >= other.height
}

/**
 * Creates a new [IntSize] instance from a string representation in the format "widthxheight".
 *
 * @param string A string representation of the size in the format "widthxheight".
 *
 * @return A new [IntSize] instance with the parsed width and height from the given string.
 *
 * @throws IllegalArgumentException If the given string does not contain exactly one 'x' character,
 *                                  or if the parsed width or height is not a valid integer.
 */
fun IntSize.Companion.valueOf(string: String): IntSize {
	val dimensions = string.split(" x ")
	return IntSize(dimensions[0].toInt(), dimensions[1].toInt())
}

/**
 * Converts the current [DpSize] instance to an [IntSize] instance.
 *
 * This function extracts the integer values of the width and height from the current [DpSize] instance
 * and returns a new [IntSize] instance with these values.
 *
 * @return A new [IntSize] instance with the width and height values from the current [DpSize] instance.
 */
fun DpSize.toIntSize(): IntSize {
	return IntSize(
		width.value.toInt(),
		height.value.toInt()
	)
}
