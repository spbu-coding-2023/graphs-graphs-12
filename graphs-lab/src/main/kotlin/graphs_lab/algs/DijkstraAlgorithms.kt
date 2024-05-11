package graphs_lab.algs

import graphs_lab.algs.utils.checkAndGetFirst
import graphs_lab.algs.utils.checkAndGetSecond
import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph

class DijkstraAlgorithm<I>(val graph: WeightedGraph<I>) {
	private val table = mutableMapOf<I, Pair<I?, Double>>()
	private var statusTable: Pair<I, Boolean>? = null

	fun getPath(idSource: I, idTarget: I): MutableList<I> {
		val path = mutableListOf<I>()

		if (statusTable == null || checkAndGetFirst(statusTable) != idSource) {
			getAllPaths(idSource)
		}
		check(checkAndGetSecond(statusTable)) { "All paths do not build." }
		if (table[idTarget]?.first == null) return path

		var idCurrent = idTarget
		path.add(idCurrent)
		while (idCurrent != idSource) {
			idCurrent = checkAndGetFirst(table[idCurrent])
			path.add(idCurrent)
		}

		return path.asReversed()
	}

	fun getWeightPath(idSource: I, idTarget: I): Double {
		if (statusTable == null || checkAndGetFirst(statusTable) != idSource) {
			getAllPaths(idSource)
		}
		check(checkAndGetSecond(statusTable)) { "All paths do not build." }
		if (table[idTarget]?.first == null) return Double.POSITIVE_INFINITY

		return checkAndGetSecond(table[idTarget])
	}

	fun getAllPaths(idSource: I): MutableMap<I, Pair<I?, Double>> {
		require(idSource in graph.idVertices) { "The source vertex is not contained." }
		statusTable = Pair(idSource, false)

		val statusVertices = mutableMapOf<I, Boolean>()

		graph.idVertices.forEach {
			table[it] = Pair(null, Double.POSITIVE_INFINITY)
			statusVertices[it] = false
		}
		table[idSource] = Pair(null, 0.0)
		statusVertices[idSource] = true
		var countVisited = 1

		if (graph.vertexEdges(idSource).isEmpty()) {
			statusTable = Pair(idSource, true)
			return table
		}

		val heapEdges = mutableListOf<WeightedEdge<I>>()
		var edgeNext: WeightedEdge<I>? = null

		graph.vertexEdges(idSource).forEach {
			require(it.weight > 0) { "Negative number does not support." }

			table[it.idTarget] = Pair(idSource, it.weight)
			heapEdges.add(it)

			val edgeTemp = edgeNext
			if (edgeTemp == null || it.weight < edgeTemp.weight) {
				edgeNext = it
			}
		}

		while (countVisited != graph.size) {
			while (true) {
				val edge = edgeNext ?: break
				heapEdges.remove(edge)
				edgeNext = null

				statusVertices[edge.idTarget] = true
				val weightPathIdTarget = checkAndGetSecond(table[edge.idTarget])

				graph.vertexEdges(edge.idTarget).forEach {
					require(it.weight > 0) { "Negative number does not support." }

					if (statusVertices[it.idTarget] == false) {
						if (checkAndGetSecond(table[it.idTarget]) > weightPathIdTarget + it.weight) {
							table[it.idTarget] = Pair(edge.idTarget, weightPathIdTarget + it.weight)
						}

						val edgeTemp = edgeNext
						if (edgeTemp == null || it.weight < edgeTemp.weight) {
							edgeNext = it
						}

						heapEdges.add(it)
					} else if (weightPathIdTarget + it.weight < checkAndGetSecond(table[it.idTarget])) {
						table[it.idTarget] = Pair(edge.idTarget, weightPathIdTarget + it.weight)
					}
				}

				countVisited++
			}

			if (heapEdges.isEmpty()) break
			edgeNext = heapEdges.removeFirst()
		}

		statusTable = Pair(idSource, true)
		return table
	}
}
