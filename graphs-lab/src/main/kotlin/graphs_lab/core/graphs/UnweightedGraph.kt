package graphs_lab.core.graphs

import graphs_lab.core.edges.Edge

/**
 * Represents an unweighted graph that extends the Graph class.
 *
 * @param I the type of the vertex identifiers
 * @property id the identifier or name of the graph
 * @property isDirected indicates whether the graph is directed or not
 * @property isAutoAddVertex specifies whether new vertices should be automatically added when adding edges
 */
class UnweightedGraph<I>(
	id: String,
	isDirected: Boolean = false,
	isAutoAddVertex: Boolean = false
) : Graph<I, Edge<I>>(id, isDirected, isAutoAddVertex) {

	/**
	 * Adds an unweighted edge between the vertices with the specified ids to the graph.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 */
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
