package graphs_lab.core.graphs

import graphs_lab.core.edges.WeightedEdge

/**
 * Represents a weighted graph that extends the Graph class.
 *
 * @param I the type of the vertex identifiers
 * @param W the type of the weight associated with the edges (must be a subtype of Number)
 * @property id the identifier or name of the graph
 * @property isDirected indicates whether the graph is directed or not
 * @property isAutoAddVertex specifies whether new vertices should be automatically added when adding edges
 */
class WeightedGraph<I>(
	id: String,
	isDirected: Boolean = false,
	isAutoAddVertex: Boolean = false
) : Graph<I, WeightedEdge<I>>(id, isDirected, isAutoAddVertex) {

	/**
	 * Adds a weighted edge between the vertices with the specified ids and the specified weight to the graph.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 * @param weight the weight of the edge
	 */
	fun addEdge(idSource: I, idTarget: I, weight: Double) = addEdge(
		WeightedEdge(idSource, idTarget, weight)
	)

	protected override fun edgeToString(edge: WeightedEdge<I>): String {
		return "{${edge.idSource}, ${edge.idTarget}, ${edge.weight}}"
	}

	protected override fun reverseEdge(edge: WeightedEdge<I>): WeightedEdge<I> {
		return WeightedEdge(edge.idTarget, edge.idSource, edge.weight)
	}
}
