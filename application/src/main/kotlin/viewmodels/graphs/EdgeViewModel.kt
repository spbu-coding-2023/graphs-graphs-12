package viewmodels.graphs

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import graphs_lab.core.edges.WeightedEdge
import models.VertexID
import themes.colorEdgesStart
import themes.widthEdgesStart

class EdgeViewModel(
	val edge: WeightedEdge<VertexID>,
	val source: VertexViewModel,
	val target: VertexViewModel,
	isUnweighted: Boolean,
	color: Color = colorEdgesStart,
	width: Float = widthEdgesStart,
	visibility: Boolean = false
) {
	private val _color = mutableStateOf(color)
	var color: Color
		get() = _color.value
		set(value) {
			_color.value = value
		}
	private val _width = mutableFloatStateOf(width)
	var width: Float
		get() = _width.floatValue
		set(value) {
			_width.floatValue = value
		}
	private val _visibility = mutableStateOf(visibility)
	var visibility: Boolean
		get() = _visibility.value
		set(value) {
			_visibility.value = value
		}
	val label: String = if (isUnweighted) {
		""
	} else if (edge.weight.rem(1) == 0.0) {
		edge.weight.toInt().toString()
	} else {
		edge.weight.toString()
	}

	override fun toString(): String {
		return "EdgeViewModel(source = $source, target = $target)"
	}
}
