package models

import graphs_lab.core.graphs.WeightedGraph

/**
 * A class representing a weighted graph that is also considered as an unweighted graph.
 * This class extends the [WeightedGraph] class and overrides the [addEdge] method to set the weight as 1.0.
 *
 * @property id the unique identifier of the graph
 * @property isDirected indicates whether the graph is directed or undirected. Default is false (undirected)
 * @property isAutoAddVertex indicates whether to automatically add vertices when adding edges. Default is false
 */
class WeightedUnweightedGraph<I>(
	id: String,
	isDirected: Boolean = false,
	isAutoAddVertex: Boolean = false
) : WeightedGraph<I>(id, isDirected, isAutoAddVertex) {
	override fun addEdge(idSource: I, idTarget: I, weight: Double) {
		super.addEdge(idSource, idTarget, 1.0)
	}
}
