package graphs_lab.algs

import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph

class DijkstraAlgorithm<I>(val graph: WeightedGraph<I>) {
	private val table = mutableMapOf<I, Pair<I?, Double>>()

	fun getPath(idSource: I, idTarget: I): MutableList<I> {
		val path = mutableListOf<I>()

		if (table[idSource] == null) getAllPaths(idSource)
		if (table[idTarget] == null) return path

		var idCurrent = idTarget
		path.add(idCurrent)
		while (idCurrent != idSource) {
			idCurrent = table[idCurrent]?.first ?: throw ExceptionInInitializerError("Undefined behaviour: unfounded vertex.")
			path.add(idCurrent)
		}

		return path.asReversed()
	}

	fun getWeightPath(idSource: I, idTarget: I): Double {
		if (table[idSource] == null) getAllPaths(idSource)
		if (table[idTarget] == null) return Double.POSITIVE_INFINITY

		var weight = 0.0

		var idCurrent = idTarget
		while (idCurrent != idSource) {
			weight += table[idCurrent]?.second ?: throw ExceptionInInitializerError("Undefined behaviour: unfounded vertex.")
			idCurrent = table[idCurrent]?.first ?: throw ExceptionInInitializerError("Undefined behaviour: unfounded vertex.")
		}

		return weight
	}

	fun getAllPaths(idSource: I): MutableMap<I, Pair<I?, Double>> {
		if (graph.idVertices.isEmpty()) return table

		val statusVertices = mutableMapOf<I, Boolean>()

		graph.idVertices.forEach {
			table[it] = Pair(null, Double.POSITIVE_INFINITY)
			statusVertices[it] = false
		}
		table[idSource] = Pair(null, 0.0)
		statusVertices[idSource] = true
		var countVisited = 1

		if (graph.vertexEdges(idSource).isEmpty()) return table

		val heapEdges = mutableListOf<WeightedEdge<I>>()
		var edgeNext: WeightedEdge<I>? = null

		graph.vertexEdges(idSource).forEach {
			if (it.weight < 0) throw IllegalArgumentException("Negative number does not support.")

			table[it.idTarget] = Pair(idSource, it.weight)
			heapEdges.add(it)

			val edgeTemp = edgeNext
			if (edgeTemp == null || it.weight < edgeTemp.weight)
				edgeNext = it
		}

		while (countVisited != graph.size) {
			while (true) {
				val edge = edgeNext ?: break
				heapEdges.remove(edge)
				edgeNext = null

				val valueIdTarget = table[edge.idTarget]
					?: throw ExceptionInInitializerError("Undefined behaviour: unfounded vertex.")
				statusVertices[edge.idTarget] = true

				graph.vertexEdges(edge.idTarget).forEach {
					if (it.weight < 0) throw IllegalArgumentException("Negative number does not support.")
					val valueIdTargetTemp = table[it.idTarget]
						?: throw ExceptionInInitializerError("Undefined behaviour: unfounded vertex.")

					if (statusVertices[it.idTarget] == false) {
						if (valueIdTargetTemp.second > valueIdTarget.second + it.weight) {
							table[it.idTarget] = Pair(edge.idTarget, valueIdTarget.second + it.weight)
						}

						val edgeTemp = edgeNext
						if (edgeTemp == null || it.weight < edgeTemp.weight) {
							edgeNext = it
						}

						heapEdges.add(it)
					} else if (valueIdTarget.second + it.weight < valueIdTargetTemp.second) {
						table[it.idTarget] = Pair(edge.idTarget, valueIdTarget.second + it.weight)
					}
				}

				countVisited++
			}

			if (heapEdges.isEmpty()) break
			edgeNext = heapEdges.removeFirst()
		}

		return table
	}
}
