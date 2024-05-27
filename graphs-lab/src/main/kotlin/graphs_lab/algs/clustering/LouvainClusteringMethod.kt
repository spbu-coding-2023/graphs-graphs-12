package graphs_lab.algs.clustering

import graphs_lab.algs.utils.SetPartition
import graphs_lab.algs.utils.doubleEquality
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

/**
 * Louvain clustering method implementation.
 *
 * See more: [Wiki](https://en.wikipedia.org/wiki/Louvain_method).
 *
 * @param I the type of the vertex ID
 * @param E the type of the edge
 * @param graph which need to search clusters
 * @return pair of clustering partition and result modularity value
 */
fun <I, E : Edge<I>> louvainClusteringMethod(graph: Graph<I, E>): Pair<SetPartition<I>, Double> {
	val partition = SetPartition(graph.idVertices)
	val modularity: Double = louvainClusteringMethod(graph, partition)
	return Pair(partition, modularity)
}

/**
 * Louvain clustering method implementation.
 *
 * See more: [Wiki](https://en.wikipedia.org/wiki/Louvain_method).
 *
 * @param I the type of the vertex ID
 * @param E the type of the edge
 * @param graph which need to search clusters
 * @param partition start clustering partition for [graph]
 * @return result modularity value for [partition]
 */
fun <I, E : Edge<I>> louvainClusteringMethod(graph: Graph<I, E>, partition: SetPartition<I>): Double {
	val modularityEvaluator = GraphModularityEvaluator(graph)
	var modularity = modularityEvaluator.evaluateModularity(partition)
	val minModularityChange = 0.03
	while (true) {
		val oldModularity = modularity
		for (idVertex in graph.idVertices) {
			var maxModularityChange = Double.MIN_VALUE
			var newNesting = Double.MIN_VALUE
			var maxTarget = idVertex
			val vertexCommunity: Set<I> = partition.getElementSet(idVertex)
			val visitedCommunities = mutableSetOf<Set<I>>()
			for (edge in graph.vertexEdges(idVertex)) {
				if (partition.isConnected(idVertex, edge.idTarget)) continue
				val targetCommunity: Set<I> = partition.getElementSet(edge.idTarget)
				if (targetCommunity in visitedCommunities) continue
				val (modularityChange, nesting) = modularityEvaluator.evaluateModularityChange(
					idVertex,
					vertexCommunity,
					targetCommunity
				)
				if (modularityChange > maxModularityChange) {
					maxTarget = edge.idTarget
					maxModularityChange = modularityChange
					newNesting = nesting
				}
				visitedCommunities.add(targetCommunity)
			}
			if ((maxModularityChange > 0.0) && (maxTarget != idVertex)) {
				partition.connectElements(idVertex, maxTarget)
				modularity += maxModularityChange
				modularityEvaluator.updateNesting(idVertex, newNesting)
			}
		}
		if (oldModularity >= modularity || doubleEquality(oldModularity, modularity, minModularityChange)) break
	}
	return modularity
}
