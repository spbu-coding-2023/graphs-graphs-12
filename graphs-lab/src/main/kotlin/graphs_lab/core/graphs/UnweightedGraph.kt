package graphs_lab.core.graphs

import graphs_lab.core.edges.Edge


class UnweightedGraph<I>(
	id: String,
	isDirected: Boolean = false,
	isAutoAddVertex: Boolean = false
) : Graph<I, Edge<I>>(id, isDirected, isAutoAddVertex) {

	fun addEdge(idSource: I, idTarget: I) = addEdge(
		Edge(idSource, idTarget)
	)

	protected override fun edgeToString(edge: Edge<I>): String {
		return "{${edge.idSource}, ${edge.idTarget}}"
	}

	protected override fun reverseEdge(edge: Edge<I>): Edge<I> {
		return Edge(edge.idTarget, edge.idSource)
	}

}
