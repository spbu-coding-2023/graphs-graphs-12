package graphs_lab.algs

import graphs_lab.algs.utils.checkAndGetFirst
import graphs_lab.algs.utils.checkAndGetSecond
import graphs_lab.algs.utils.removeAndReturn
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

class TarjanBridgeFinding<I>(val graph: Graph<I, Edge<I>>) {
	private val table = mutableSetOf<Edge<I>>()
	private val tableHeights = mutableMapOf<I, Pair<Int?, Int?>>()
	private val listUnvisited = mutableListOf<I>()

	init {
		require(!graph.isDirected) { "Do not support directed graph." }
	}

	fun getBridges(): Set<Edge<I>> {
		if (graph.idVertices.isEmpty()) return table

		graph.idVertices.forEach {
			listUnvisited.add(it)
		}

		while (listUnvisited.isNotEmpty()) {
			dfs(graph.vertexEdges(listUnvisited.removeFirst()), 1)
		}

		return table
	}

	private fun dfs(setEdges: Set<Edge<I>>, height: Int) {
		if (setEdges.isEmpty()) return

		tableHeights[setEdges.first().idSource] = Pair(height, height)

		setEdges.forEach {
			var temp: Int

			if (tableHeights[it.idTarget] == null) {
				dfs(graph.vertexEdges(listUnvisited.removeAndReturn(it.idTarget)), height + 1)
				temp = checkAndGetFirst(tableHeights[it.idTarget])
			} else {
				temp = checkAndGetSecond(tableHeights[it.idTarget])
				if (height - temp == 1) {
					temp = height
				}
			}

			tableHeights[it.idSource] = Pair(minOf(temp, checkAndGetFirst(tableHeights[it.idSource])), height)

			if (checkAndGetFirst(tableHeights[it.idTarget]) > height) {
				table.add(it)
			}
		}
	}
}
