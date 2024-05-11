package graphs_lab.algs

import graphs_lab.core.graphs.WeightedGraph

/**
 * The Bellman-Ford algorithm
 *
 * Computes shortest paths from source vertex to target vertex in a weighted graph.
 * The Bellman-Ford algorithm supports negative edge weights, but it does not support negative weight cycles.
 * If a negative weight cycle is detected, the algorithm will report it. This implies that negative edge weights
 * are not allowed in undirected graphs. In such cases the code will throw an exception.
 * Note that the algorithm will not report or find negative weight cycles which are not reachable from
 * the source vertex.
 *
 * @references https://ru.wikipedia.org/wiki/Алгоритм_Беллмана_—_Форда
 *
 * @param I the type of the vertex identifiers
 * @property graph the input graph
 */
class BellmanFordShortestPath<I : Any>(val graph: WeightedGraph<I>) {
	private var weightOfShortestPath: Double = Double.POSITIVE_INFINITY
	private var countOfVertices: Int = 0
	private val indexedVertices = mutableMapOf<I, Int>()
	private val reverseIndexedVertices = mutableMapOf<Int, I>() // for quick conversion from index to id
	private var matrix: Array<DoubleArray> = Array(countOfVertices) { DoubleArray(countOfVertices + 2) }
	private var pathMatrix: Array<IntArray> = Array(countOfVertices) { IntArray(countOfVertices + 2) }

	/**
	 * Get the weight of the shortest path from the source vertex to the target vertex.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 * @return shortest path weight
	 */
	fun getPathWeight(idSource: I, idTarget: I): Double {
		if (!graph.containsVertex(idSource) || !graph.containsVertex(idTarget)) {
			throw NoSuchElementException("Graph must contain the source and the target vertices.")
		}

		getPath(idSource, idTarget)
		return weightOfShortestPath
	}

	/**
	 * Get the shortest path from a source vertex to a target vertex.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 * @return list of vertices in the shortest path order
	 */
	fun getPath(idSource: I, idTarget: I): List<I> {
		if (!graph.containsVertex(idSource) || !graph.containsVertex(idTarget)) {
			throw NoSuchElementException("Graph must contain the source and the target vertices.")
		}

		countOfVertices = graph.size
		// Checks whether all paths from the idSource should be built
		if (matrix.isEmpty()) {
			vertexIndexing()
			getPaths(idSource, indexedVertices)
		} else if (matrix[indexedVertices[idSource]!!][0] == Double.POSITIVE_INFINITY) {
			vertexIndexing()
			getPaths(idSource, indexedVertices)
		}

		// Finding the length of the shortest path and the number of edges in this path
		weightOfShortestPath = Double.POSITIVE_INFINITY
		var numberOfEdgesInThePath = 0
		var tempIdTarget = indexedVertices[idTarget]!!
		for (i in 0 until countOfVertices) {
			if (matrix[tempIdTarget][i] < weightOfShortestPath) {
				weightOfShortestPath = matrix[tempIdTarget][i]
				numberOfEdgesInThePath = i
			}
		}

		require(weightOfShortestPath != Double.POSITIVE_INFINITY) {
			"There is no path between vertex $idSource and vertex $idTarget."
		}

		// Constructing a path in given indexes
		val resultInIndexes = IntArray(numberOfEdgesInThePath) { 0 }
		var tempNumberOfEdgesInThePath = numberOfEdgesInThePath
		while (tempNumberOfEdgesInThePath > 0) {
			resultInIndexes[tempNumberOfEdgesInThePath - 1] = tempIdTarget
			tempIdTarget = pathMatrix[tempIdTarget][tempNumberOfEdgesInThePath]
			tempNumberOfEdgesInThePath -= 1
		}

		// Converting the path in the indexes to the path in the ids
		val resultInIds = mutableListOf<I>()
		resultInIds.add(idSource)
		for (i in 0 until numberOfEdgesInThePath) {
			resultInIds.add(reverseIndexedVertices[resultInIndexes[i]]!!)
		}
		return (resultInIds)
	}

	/**
	 * Mapping an index to each vertex.
	 */
	private fun vertexIndexing() {
		var index = 0
		graph.idVertices.forEach { vertex ->
			indexedVertices[vertex] = index
			reverseIndexedVertices[index] = vertex
			index++
		}
	}

	/**
	 * Compute all shortest paths starting from a single source vertex.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param indexedVertices map in which each vertex is associated with an index
	 */
	private fun getPaths(idSource: I, indexedVertices: MutableMap<I, Int>) {
		// Construction of a matrix in which A_ij is the length of the shortest path from source to i,
		// containing no more than j edges.
		// Construction of a pathMatrix, in which A_ij contains the previous vertex to i in
		// one of these shortest paths.
		val start = indexedVertices[idSource]!!
		matrix = Array(countOfVertices) { DoubleArray(countOfVertices + 2) { Double.POSITIVE_INFINITY } }
		matrix[start][0] = 0.0
		pathMatrix = Array(countOfVertices) { IntArray(countOfVertices + 2) { 0 } }
		for (i in 1 until countOfVertices + 2) {
			graph.idVertices.forEach { vertex ->
				graph.vertexEdges(vertex).forEach { edge ->
					val origin = indexedVertices[edge.idSource]!!
					val destination = indexedVertices[edge.idTarget]!!
					val edgeWeight = edge.weight

					if (matrix[destination][i] > matrix[origin][i - 1] + edgeWeight) {
						matrix[destination][i] = matrix[origin][i - 1] + edgeWeight
						pathMatrix[destination][i] = origin
					}
				}
			}
		}

		// Check for negative cycles
		for (i in 0 until countOfVertices) {
			var shortestDistance = Double.POSITIVE_INFINITY
			for (j in 1 until countOfVertices + 1) {
				if (matrix[i][j] < shortestDistance) shortestDistance = matrix[i][j]
			}
			require(matrix[i][countOfVertices + 1] >= shortestDistance) {
				"Graph contains negative weight cycle."
			}
		}
	}
}
