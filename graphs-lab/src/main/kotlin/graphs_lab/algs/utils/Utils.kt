package graphs_lab.algs.utils

import graphs_lab.core.edges.Edge
import graphs_lab.core.edges.WeightedEdge

fun <I, E : Edge<I>> edgeWeight(edge: E, defaultWeight: Double = 1.0): Double {
	if (edge::class == WeightedEdge::class) {
		edge as WeightedEdge<*>
		return edge.weight
	}
	return defaultWeight
}
