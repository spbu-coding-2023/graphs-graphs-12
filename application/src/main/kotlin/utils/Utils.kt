package utils

import androidx.compose.ui.unit.IntSize

fun IntSize.isLargeOnParamsThen(other: IntSize): Boolean {
	return width >= other.width && height >= other.height
}
