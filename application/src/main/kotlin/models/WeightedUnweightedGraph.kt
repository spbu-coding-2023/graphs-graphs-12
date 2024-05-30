package models

import graphs_lab.core.graphs.WeightedGraph

class WeightedUnweightedGraph<I>(
	id: String,
	isDirected: Boolean = false,
	isAutoAddVertex: Boolean = false
) : WeightedGraph<I>(id, isDirected, isAutoAddVertex) {

	override fun addEdge(idSource: I, idTarget: I, weight: Double) {
		super.addEdge(idSource, idTarget, 1.0)
	}
}
