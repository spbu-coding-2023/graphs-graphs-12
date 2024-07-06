package viewmodels.graphs

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import models.VertexID
import themes.colorVerticesStart
import themes.radiusVerticesStart

/**
 * A ViewModel class representing a vertex in a graph.
 *
 * @property id the unique identifier of the vertex
 * @param x the initial x-coordinate of the vertex
 * @property xPos the current x-coordinate of the vertex
 * @param y the initial y-coordinate of the vertex
 * @property yPos the current y-coordinate of the vertex
 * @param color the initial color of the vertex
 * @property color the current color of the vertex
 * @param radius the initial radius of the vertex
 * @property radius the current radius of the vertex
 * @param degree the initial degree of the vertex
 * @property degree the current degree of the vertex
 * @param visibility the initial visibility of the vertex
 * @property visibility the current visibility of the vertex
 */
class VertexViewModel(
	val id: VertexID,
	x: Dp = 0.dp,
	y: Dp = 0.dp,
	color: Color = colorVerticesStart,
	radius: Dp = radiusVerticesStart,
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

	/**
	 * Function to handle dragging of the vertex.
	 *
	 * This function updates the x and y positions of the vertex based on the drag offset.
	 *
	 * @param offset The offset of the drag event. The offset contains the change in position
	 * 					since the last drag event. The x and y properties of the offset represent
	 * 					the change in x and y coordinates, respectively
	 *
	 * @see Offset
	 */
	fun onDrag(offset: Offset) {
		xPos += offset.x.dp
		yPos += offset.y.dp
	}

	override fun toString(): String {
		return "VertexViewModel(id = $id)"
	}
}
