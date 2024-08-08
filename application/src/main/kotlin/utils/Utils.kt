package utils

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
