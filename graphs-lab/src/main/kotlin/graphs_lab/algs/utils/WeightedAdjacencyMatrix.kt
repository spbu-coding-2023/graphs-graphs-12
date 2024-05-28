package graphs_lab.algs.utils

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

/**
 * Weighted adjacency matrix implementation.
 *
 * See more: [Wiki](https://en.wikipedia.org/wiki/Adjacency_matrix).
 *
 * @param I the type of the vertex ID
 * @param E the type of the edge
 * @param graph to implements its adjacency matrix
 * @property totalWeight summary of all weights of `graph`'s edges
 * @property size count of `graph` vertices
 */
class WeightedAdjacencyMatrix<I, E : Edge<I>>(graph: Graph<I, E>) {
	private val matrix = mutableMapOf<I, MutableMap<I, Double>>()
	private val degrees = mutableMapOf<I, Double>()
	val size: Int
		get() = matrix.size
	var totalWeight = 0.0
		private set

	init {
		updateMatrix(graph)
	}

	/**
	 * Get edge weight from graph.
	 *
	 * @param idSource id of edge's source vertex
	 * @param idTarget id of edge's target vertex
	 * @return `0.0` if edge not found else its weight, by result of [getEdgeWeight] method
	 */
	fun getEdgeWeightOrDefault(idSource: I, idTarget: I): Double {
		return getEdgeWeightOrDefault(idSource, idTarget, 0.0)
	}

	/**
	 * Get edge weight from graph or return [defaultWeight] if edge not exists.
	 *
	 * @param idSource id of edge's source vertex
	 * @param idTarget id of edge's target vertex
	 * @param defaultWeight value of not found edge which source is [idSource] and target is [idTarget]
	 * @return [defaultWeight] if edge not found else its weight, by result of [getEdgeWeight] method
	 */
	fun getEdgeWeightOrDefault(idSource: I, idTarget: I, defaultWeight: Double): Double {
		return matrix.getOrDefault(idSource, mutableMapOf()).getOrDefault(idTarget, defaultWeight)
	}

	/**
	 * Evaluate vertex weighted degree.
	 * Weighted degree it's a summary of all edge's weights where source is [idVertex].
	 *
	 * @param idVertex
	 * @return 0.0 if vertex not found in graph or has no edges which start from it
	 * 			else summary of all edge's weights where source is [idVertex].
	 */
	fun getVertexWeightedDegree(idVertex: I): Double {
		return degrees.getOrDefault(idVertex, 0.0)
	}

	/**
	 * Update matrix for new graph implementation.
	 *
	 * @param graph to implements its adjacency matrix
	 */
	fun updateMatrix(graph: Graph<I, E>) {
		matrix.clear()
		degrees.clear()
		totalWeight = 0.0
		for (idVertex in graph.idVertices) {
			for (edge in graph.vertexEdges(idVertex)) {
				val edgeWeight = getEdgeWeight(edge, 1.0)
				totalWeight += edgeWeight
				addEdge(edge.idSource, edge.idTarget, edgeWeight)
			}
		}
	}

	/**
	 * Add weight of edge which start from [idSource] and finish with [idTarget].
	 *
	 * @param idSource id of edge's source vertex
	 * @param idTarget id of edge's target vertex
	 * @param edgeWeight value of edge weight
	 */
	private fun addEdge(idSource: I, idTarget: I, edgeWeight: Double) {
		val sourceEdges = matrix.getOrDefault(idSource, mutableMapOf())
		sourceEdges[idTarget] = edgeWeight
		matrix.putIfAbsent(idSource, sourceEdges)

		val sourceDegree = degrees.getOrDefault(idSource, 0.0)
		degrees[idSource] = (sourceDegree + edgeWeight)
	}
}
