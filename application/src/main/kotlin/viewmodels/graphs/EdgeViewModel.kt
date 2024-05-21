package viewmodels.graphs

import graphs_lab.core.edges.WeightedEdge
import models.VertexID

class EdgeViewModel(
	val edge: WeightedEdge<VertexID>,
	val source: VertexViewModel,
	val target: VertexViewModel,
	isUnweigted: Boolean
) {
	val label: String = if (isUnweigted) "" else edge.weight.toString()

	override fun toString(): String {
		return "EdgeViewModel(source=$source, target=$target)"
	}
}
