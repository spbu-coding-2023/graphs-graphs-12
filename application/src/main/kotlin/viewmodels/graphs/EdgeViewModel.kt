package viewmodels.graphs

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import graphs_lab.core.edges.WeightedEdge
import models.VertexID

class EdgeViewModel(
	val edge: WeightedEdge<VertexID>,
	val source: VertexViewModel,
	val target: VertexViewModel,
	isUnweighted: Boolean,
	color: Color = Color.Black, // Color(56, 90, 83)
	size: Float = 1f // todo(Float?)
) {
	private val _color = mutableStateOf(color)
	var color: Color
		get() = _color.value
		set(value) {
			_color.value = value
		}
	private val _size = mutableFloatStateOf(size)
	var size: Float
		get() = _size.floatValue
		set(value) {
			_size.floatValue = value
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
