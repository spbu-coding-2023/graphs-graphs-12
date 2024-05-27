package viewmodels.graphs

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import models.VertexID
import views.radiusStart
import javax.swing.text.StyledEditorKit.BoldAction

val colorVertexStart = Color.Gray

class VertexViewModel(
	val id: VertexID,
	x: Dp = 0.dp,
	y: Dp = 0.dp,
	color: Color = colorVertexStart,
	radius: Dp = radiusStart,
	degree: Int = 0,
	visibility: Boolean = true
) {
	private val _xPos = mutableStateOf(x)
	var xPos: Dp
		get() = _xPos.value
		set(newPos) {
			_xPos.value = newPos
		}
	private val _yPos = mutableStateOf(y)
	var yPos: Dp
		get() = _yPos.value
		set(newPos) {
			_yPos.value = newPos
		}
	private val _color = mutableStateOf(color)
	var color: Color
		get() = _color.value
		set(value) {
			_color.value = value
		}
	private val _radius = mutableStateOf(radius)
	var radius: Dp
		get() = _radius.value
		set(value) {
			_radius.value = value
		}
	private val _degree = mutableIntStateOf(degree)
	var degree: Int
		get() = _degree.intValue
		set(value) {
			_degree.intValue = value
		}
	private val _visibility = mutableStateOf(visibility)
	var visibility: Boolean
		get() = _visibility.value
		set(value) {
			_visibility.value = value
		}
	val label: String = id.valueToString()

	fun onDrag(offset: Offset) {
		xPos += offset.x.dp
		yPos += offset.y.dp
	}

	override fun toString(): String {
		return "VertexViewModel(id = $id)"
	}
}
