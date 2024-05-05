package graphs_lab.algs.utils

import graphs_lab.core.edges.Edge
import graphs_lab.core.edges.WeightedEdge

/**
 * Retrieves the weight of the specified edge.
 *
 * @param edge The edge for which the weight needs to be retrieved
 * @param defaultWeight The default weight to return if the edge is not a WeightedEdge
 * @return The weight of the edge if it is a WeightedEdge, otherwise the defaultWeight
 */
fun <I, E : Edge<I>> getEdgeWeight(edge: E, defaultWeight: Double = 1.0): Double {
	if (edge::class == WeightedEdge::class) {
		edge as WeightedEdge<*>
		return edge.weight
	}
	return defaultWeight
}
