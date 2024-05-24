package viewmodels.graphs

import graphs_lab.core.edges.WeightedEdge
import models.VertexID

class EdgeViewModel(
	val edge: WeightedEdge<VertexID>,
	val source: VertexViewModel,
	val target: VertexViewModel,
	isUnweighted: Boolean
) {
	val label: String = if (isUnweighted) {
		""
	} else if (edge.weight.rem(1) == 0.0) {
		edge.weight.toInt().toString()
	} else {
		edge.weight.toString()
	}

	override fun toString(): String {
		return "EdgeViewModel(source=$source, target=$target)"
	}
}
