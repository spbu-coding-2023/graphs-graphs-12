package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

/**
 * Class for searching cycles in graph.
 *
 * @param I The type of the vertex ID
 * @param E The type of the edge
 * @property graph The graph to search for cycles in
 */
class CyclesSearchAlgorithms<I, E : Edge<I>>(val graph: Graph<I, E>) {
	private val cyclesPaths = mutableSetOf<List<I>>()
	private val visitedVertices = mutableSetOf<I>()

	/**
	 * Searches for cycles containing the specified vertex in the graph.
	 *
	 * @param idVertex The ID of the vertex to search cycles for
	 * @return A set of lists representing cycles containing the specified vertex
	 */
	fun searchVertexCycles(idVertex: I): Set<List<I>> {
		cyclesPaths.clear()
		findCycles(
			startVertex = idVertex,
			currentVertex = idVertex,
			path = mutableListOf()
		)
		return cyclesPaths.toSet()
	}

	/**
	 * Recursively finds cycles containing the specified vertex.
	 *
	 * @param startVertex The starting vertex for the cycle search
	 * @param currentVertex The current vertex during traversal
	 * @param path The current path through the graph
	 */
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
