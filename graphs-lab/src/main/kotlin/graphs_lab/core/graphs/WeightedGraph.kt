package graphs_lab.core.graphs

import graphs_lab.core.edges.WeightedEdge


class WeightedGraph<I, W : Number>(
	id: String,
	isDirected: Boolean = false,
	isAutoAddVertex: Boolean = false
) : Graph<I, WeightedEdge<I, W>>(id, isDirected, isAutoAddVertex) {

	fun addEdge(idSource: I, idTarget: I, weight: W) = addEdge(
		WeightedEdge(idSource, idTarget, weight)
	)

	protected override fun edgeToString(edge: WeightedEdge<I, W>): String {
		return "{${edge.idSource}, ${edge.idTarget}, ${edge.weight}}"
	}

	protected override fun reverseEdge(edge: WeightedEdge<I, W>): WeightedEdge<I, W> {
		return WeightedEdge(edge.idTarget, edge.idSource, edge.weight)
	}

}
