package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

/**
 * Class for searching cycles in graph.
 *
 * @param I the type of the vertex ID
 * @param E the type of the edge
 * @property graph the graph to search for cycles in
 */
class CyclesSearchAlgorithms<I, E : Edge<I>>(val graph: Graph<I, E>) {
	private val cyclesPaths = mutableSetOf<List<I>>()
	private val visitedVertices = mutableSetOf<I>()

	/**
	 * Searches for cycles containing the specified vertex in the graph.
	 *
	 * @param idVertex the ID of the vertex to search cycles for
	 * @return set of lists representing cycles containing the specified vertex
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
	 * @param startVertex the starting vertex for the cycle search
	 * @param currentVertex the current vertex during traversal
	 * @param path the current path through the graph
	 */
	private fun findCycles(startVertex: I, currentVertex: I, path: MutableList<I>) {
		if (path.size >= smallestCycleLength() && currentVertex == startVertex) cyclesPaths.add(path.toList())
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

	/**
	 * Defines smallest cycle length at graph.
	 *
	 * @return if graph is directed then 2 else 3
	 */
	private fun smallestCycleLength(): Int {
		return 2 + if (graph.isDirected) 0 else 1
	}
}
