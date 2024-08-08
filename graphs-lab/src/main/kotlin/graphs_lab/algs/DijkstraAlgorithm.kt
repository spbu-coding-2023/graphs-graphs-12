package graphs_lab.algs

import graphs_lab.algs.utils.checkAndGetFirst
import graphs_lab.algs.utils.checkAndGetSecond
import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph

/**
 * The Dijkstra algorithm.
 *
 * Computes the shortest paths from source vertex to all vertices in a weighted graph.
 * The Dijkstra algorithm does not support negative edge weights and in such cases an exception is thrown.
 * The shortest path is a path with a minimal sum of the all weights of edges on the path.
 *
 * @references https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 *
 * @param I the type of the vertex identifiers
 * @property graph the input graph
 */
class DijkstraAlgorithm<I>(val graph: WeightedGraph<I>) {
	/**
	 * [table] the map that associates the identifier of a vertex with a pair that consist of
	 * the identifier of a vertex, which came from beginning with the given identifier, and
	 * a weight equals the sum of the all weights of edges on the path from the given identifier.
	 * A path is a set of the identifiers of a vertex that get if add the identifiers first elements of pairs
	 * to this set beginning with the given identifier.
	 */
	private val table = mutableMapOf<I, Pair<I?, Double>>()

	/**
	 * [statusTable] the status of [table] equals a pair that consist of the given identifier,
	 * from which come out, and a boolean variable that shows whether the algorithm was
	 * processed without exceptions.
	 */
	private var statusTable: Pair<I, Boolean>? = null

	/**
	 * Returns the shortest path from a source vertex to a target vertex.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 * @return `path` equals a list of the identifier of a vertex in the order of movement or
	 * null if `path` is an empty list
	 * @throws IllegalArgumentException if a weight is negative number or [idSource] or [idTarget]
	 * is not contained in the graph
	 * @throws IllegalStateException if there is an appeal to a non-existent identifier
	 */
	fun getPath(idSource: I, idTarget: I): List<I>? {
		val path = mutableListOf<I>()

		if (statusTable == null || checkAndGetFirst(statusTable) != idSource) {
			getTable(idSource)
		}
		check(checkAndGetSecond(statusTable)) { "All paths do not build." }

		val pairTemp = table[idTarget] ?: throw IllegalArgumentException("The target vertex is not contained.")
		if (pairTemp.first == null) return null

		// Assembles the path in the reversed order of movement
		var idCurrent = idTarget
		path.add(idCurrent)
		while (idCurrent != idSource) {
			idCurrent = checkAndGetFirst(table[idCurrent])
			path.add(idCurrent)
		}

		return path.asReversed()
	}

	/**
	 * Returns a weight of the shortest path from a source vertex to a target vertex.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 * @return a weight equals the sum of the all weights of edges on the path from the given identifier
	 * @throws IllegalArgumentException if a weight is negative number or [idSource] or [idTarget]
	 * is not contained in the graph
	 * @throws IllegalStateException if there is an appeal to a non-existent identifier
	 */
	fun getWeightPath(idSource: I, idTarget: I): Double {
		if (statusTable == null || checkAndGetFirst(statusTable) != idSource) {
			getTable(idSource)
		}
		check(checkAndGetSecond(statusTable)) { "All paths do not build." }

		val pairTemp = table[idTarget] ?: throw IllegalArgumentException("The target vertex is not contained.")
		if (pairTemp.first == null) return Double.POSITIVE_INFINITY

		return checkAndGetSecond(table[idTarget])
	}

	/**
	 * @param idSource the identifier of the source vertex
	 * @return [table]
	 * @throws IllegalArgumentException if a weight is negative number or [idSource] is not contained in the graph
	 * @throws IllegalStateException if there is an appeal to a non-existent identifier
	 */
	fun getTable(idSource: I): Map<I, Pair<I?, Double>> {
		require(graph.idVertices.contains(idSource)) { "The source vertex is not contained." }
		statusTable = Pair(idSource, false)

		val setVerticesVisited = mutableSetOf<I>() // a vertex is considered visited if all its edges have been explored

		// Initializes `table` and `statusVertices` on the first step
		graph.idVertices.forEach {
			table[it] = Pair(null, Double.POSITIVE_INFINITY)
		}
		table[idSource] = Pair(null, 0.0)
		setVerticesVisited.add(idSource)

		if (graph.vertexEdges(idSource).isEmpty()) {
			statusTable = Pair(idSource, true)
			return table
		}

		val listEdges = mutableListOf<WeightedEdge<I>>()
		var edgeNext: WeightedEdge<I>? = null

		// Searches the edge with the minimal weight from the source vertex
		graph.vertexEdges(idSource).forEach {
			require(it.weight >= 0) { "Negative number does not support." }

			table[it.idTarget] = Pair(idSource, it.weight)
			listEdges.add(it)

			val edgeTemp = edgeNext
			if (edgeTemp == null || it.weight < edgeTemp.weight) {
				edgeNext = it
			}
		}

		// Finds the shortest paths to all vertices from the source vertex
		while (setVerticesVisited.size != graph.size) {
			// Finds the shortest paths to the target vertex from the source vertex
			while (true) {
				val edge = edgeNext ?: break
				listEdges.remove(edge)
				edgeNext = null

				setVerticesVisited.add(edge.idTarget)
				val weightPathIdCurrent = checkAndGetSecond(table[edge.idTarget])

				// Searches the edge with the minimal weight from the current vertex
				graph.vertexEdges(edge.idTarget).forEach {
					require(it.weight >= 0) { "Negative number does not support." }

					if (weightPathIdCurrent + it.weight < checkAndGetSecond(table[it.idTarget])) {
						table[it.idTarget] = Pair(edge.idTarget, weightPathIdCurrent + it.weight)
					}

					if (!setVerticesVisited.contains(it.idTarget)) {
						listEdges.add(it)

						val edgeTemp = edgeNext
						if (edgeTemp == null || it.weight < edgeTemp.weight) {
							edgeNext = it
						}
					}
				}
			}

			if (listEdges.isEmpty()) break
			edgeNext = listEdges.removeFirst()
		}

		statusTable = Pair(idSource, true)
		return table
	}
}
