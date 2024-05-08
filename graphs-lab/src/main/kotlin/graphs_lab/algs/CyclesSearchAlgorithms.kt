package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

class CyclesSearchAlgorithms<I, E : Edge<I>>(val graph: Graph<I, E>) {
	private val cyclesPaths = mutableSetOf<List<I>>()
	private val visitedVertices = mutableSetOf<I>()

	fun searchVertexCycles(idVertex: I): Set<List<I>> {
		cyclesPaths.clear()
		findCycles(
			startVertex = idVertex,
			currentVertex = idVertex,
			path = mutableListOf()
		)
		return cyclesPaths.toSet()
	}

	private fun findCycles(startVertex: I, currentVertex: I, path: MutableList<I>) {
		if (path.size > 2 && currentVertex == startVertex) cyclesPaths.add(path.toList())
		if (currentVertex in visitedVertices) return

		path.add(currentVertex)
		visitedVertices.add(currentVertex)
		for (edge in graph.vertexEdges(currentVertex)) {
			findCycles(
				startVertex = startVertex,
				currentVertex = edge.idTarget,
				path = path
			)
		}

		visitedVertices.remove(currentVertex)
		path.removeLast()
	}
}
