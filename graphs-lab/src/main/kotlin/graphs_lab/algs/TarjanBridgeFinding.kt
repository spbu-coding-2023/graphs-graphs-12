package graphs_lab.algs

import graphs_lab.algs.utils.checkAndGetFirst
import graphs_lab.algs.utils.checkAndGetSecond
import graphs_lab.algs.utils.removeAndReturn
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

/**
 * The Tarjan's bridge-finding algorithm.
 *
 * Finding the bridges in an undirected graph.
 * A bridge is an edge, when it is removed, the number of connectivity components increases.
 *
 * @references https://en.wikipedia.org/wiki/Bridge_(graph_theory)
 *
 * @param I the type of the vertex identifiers
 * @param E the type of the edges in the graph
 * @property graph the input graph
 */
class TarjanBridgeFinding<I, E : Edge<I>>(
	val graph: Graph<I, E>
) {
	private val setBridges = mutableSetOf<Edge<I>>()

	/**
	 * [tableHeights] the map that associates the identifier of a vertex with a pair that consist of
	 * the minimal height (in an imaginary tree) among the vertices connected by an inverse edge (in an imaginary tree)
	 * to the vertices from the subtree with the associated vertex in root or the height (in an imaginary tree)
	 * of the associated vertex and the height (in an imaginary tree) of the associated vertex.
	 */
	private val tableHeights = mutableMapOf<I, Pair<Int?, Int?>>()
	private val listUnvisitedVertices = mutableListOf<I>()

	init {
		require(!graph.isDirected) { "Do not support directed graph." }
	}

	/**
	 * @return [setBridges]
	 * @throws IllegalStateException if there is an appeal to a non-existent identifier
	 */
	fun getBridges(): Set<Edge<I>> {
		if (graph.idVertices.isEmpty()) return setBridges

		graph.idVertices.forEach {
			listUnvisitedVertices.add(it)
		}

		while (listUnvisitedVertices.isNotEmpty()) {
			dfs(graph.vertexEdges(listUnvisitedVertices.removeFirst()), 1)
		}

		return setBridges
	}

	/**
	 * Goes down the tree to the depth and fills [tableHeights] in according to the definition.
	 *
	 * @throws IllegalStateException if there is an appeal to a non-existent identifier
	 */
	private fun dfs(setEdges: Set<Edge<I>>, height: Int) {
		if (setEdges.isEmpty()) return

		tableHeights[setEdges.first().idSource] = Pair(height, height)

		setEdges.forEach {
			var temp: Int

			if (tableHeights[it.idTarget] == null) { // Means a straight edge (in an imaginary tree)
				dfs(graph.vertexEdges(listUnvisitedVertices.removeAndReturn(it.idTarget)), height + 1)
				temp = checkAndGetFirst(tableHeights[it.idTarget])
			} else { // Means an inverse edge (in an imaginary tree)
				temp = checkAndGetSecond(tableHeights[it.idTarget])
				if (height - temp == 1) { // Avoids considering an inverse edge whose vertex heights differ by 1
					temp = height
				}
			}

			tableHeights[it.idSource] = Pair(minOf(temp, checkAndGetFirst(tableHeights[it.idSource])), height)

			if (checkAndGetFirst(tableHeights[it.idTarget]) > height) {
				setBridges.add(it)
			}
		}
	}
}
