package graphs_lab.algs.clustering

import graphs_lab.algs.utils.WeightedAdjacencyMatrix
import graphs_lab.algs.utils.SetPartition
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

/**
 * Graph modularity heuristic evaluator.
 * Needed to valid evaluate start modularity value of `graph` partition and update its value if partition update.
 * __Important__: if `graph` change evaluator may not be correct.
 *
 * @param I the type of the vertex ID
 * @param E the type of the edge
 * @param graph which need to create modularity evaluator
 */
class GraphModularityEvaluator<I, E : Edge<I>>(graph: Graph<I, E>) {
	private val adjacencyMatrix = WeightedAdjacencyMatrix(graph)
	private val module: Double = adjacencyMatrix.totalWeight / 2

	/**
	 * Evaluate modularity for graph partition.
	 *
	 * @param communities partition of graph vertices
	 * @return value of modularity
	 */
	fun evaluateModularity(communities: SetPartition<I>): Double {
		return evaluateModularity(communities.getPartition())
	}

	/**
	 * Evaluate modularity for graph partition.
	 *
	 * @param communities partition of graph vertices
	 * @return value of modularity
	 */
	fun evaluateModularity(communities: Set<Set<I>>): Double {
		var modularity = 0.0
		for (community in communities) {
			for (idSource in community) {
				val sourceDegree: Double = adjacencyMatrix.getVertexWeightedDegree(idSource)
				for (idTarget in community) {
					if (idSource == idTarget) continue
					val edgeWeight: Double = adjacencyMatrix.getEdgeWeight(idSource, idTarget)
					val targetDegree: Double = adjacencyMatrix.getVertexWeightedDegree(idTarget)
					modularity += (edgeWeight - sourceDegree * targetDegree / adjacencyMatrix.totalWeight)
				}
			}
		}
		return modularity / adjacencyMatrix.totalWeight
	}

	/**
	 * Evaluate modularity change on move [idVertex] from [communityFrom] to [communityTo].
	 *
	 * @param idVertex vertex to check modularity change
	 * @param communityFrom community from which need remove [idVertex]
	 * @param communityTo community to which need add [idVertex]
	 * @return value of modularity change or [Double.MIN_VALUE] if [idVertex] not in [communityFrom]
	 */
	fun evaluateModularityChange(idVertex: I, communityFrom: Set<I>, communityTo: Set<I>): Double {
		if (idVertex !in communityFrom) return Double.MIN_VALUE
		val nestingFrom: Double = vertexNestingInCommunity(idVertex, communityFrom)
		val nestingTo: Double = vertexNestingInCommunity(idVertex, communityTo)
		return nestingTo - nestingFrom
	}

	/**
	 * Vertex nesting in community modularity.
	 *
	 * @param idVertex which need to evaluate its nesting
	 * @param community to check nesting on modularity
	 * @return value of vertex nesting of community modularity
	 */
	private fun vertexNestingInCommunity(idVertex: I, community: Set<I>): Double {
		val vertexDegree: Double = adjacencyMatrix.getVertexWeightedDegree(idVertex)
		var summaryCommunityDegree = 0.0
		var summaryEdges = 0.0
		for (idCommunityVertex in community) {
			if (idCommunityVertex == idVertex) continue
			summaryCommunityDegree += adjacencyMatrix.getVertexWeightedDegree(idCommunityVertex)
			summaryEdges += adjacencyMatrix.getEdgeWeight(idVertex, idCommunityVertex)
			summaryEdges += adjacencyMatrix.getEdgeWeight(idCommunityVertex, idVertex)
		}
		return (summaryEdges - (vertexDegree / module) * summaryCommunityDegree) / adjacencyMatrix.totalWeight
	}
}
