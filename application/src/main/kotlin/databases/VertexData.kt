package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * A data class to hold vertex data during graph loading.
 *
 * @property x The x-coordinate of the vertex.
 * @property y The y-coordinate of the vertex.
 * @property radius The radius of the vertex.
 * @property color The color of the vertex.
 * @property degree The degree of the vertex.
 */
data class VertexData(
	val x: Dp,
	val y: Dp,
	val radius: Dp,
	val color: Color,
	val degree: Int
)
