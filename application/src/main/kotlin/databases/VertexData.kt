package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

data class VertexData(
	val x: Dp,
	val y: Dp,
	val radius: Dp,
	val color: Color,
	val degree: Int
)
