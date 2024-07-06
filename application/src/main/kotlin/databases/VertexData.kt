package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * A data class to hold vertex data during graph loading.
 *
 * @property x the x-coordinate of the vertex
 * @property y the y-coordinate of the vertex
 * @property radius the radius of the vertex
 * @property color the color of the vertex
 * @property degree the degree of the vertex
 */
data class VertexData(
	val x: Dp,
	val y: Dp,
	val radius: Dp,
	val color: Color,
	val degree: Int
)
