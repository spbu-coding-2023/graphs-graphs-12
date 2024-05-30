package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import java.lang.IllegalArgumentException
import java.util.Stack
import kotlin.math.min

/**
 * Tarjan's strongly connected components algorithm
 *
 * Tarjan's strongly connected components algorithm is an algorithm in graph theory
 * for finding the strongly connected components (SCCs) of a directed graph.
 *
 * @references https://www.geeksforgeeks.org/tarjan-algorithm-find-strongly-connected-components/
 *
 * @param I the type of the vertex identifiers in the graph
 * @param E the type of the edges in the graph
 * @property graph the input graph for which the SCC will be found
 *
 * @throws IllegalArgumentException if the input graph is undirected, as SCC algorithm only work for directed graphs
 */
class TarjanStrongConnectivityInspector<I, E : Edge<I>>(val graph: Graph<I, E>) {
	private var time = 0
	private val discoveryTime = mutableMapOf<I, Int>() // stores discovery times of visited vertices
	private val lowLink = mutableMapOf<I, Int>() // contains the earliest visited vertex (the vertex with minimum discovery
												// time) that can be reached from subtree rooted with current vertex
	private val stack = Stack<I>() // needed to store all the connected ancestors (could be part of SCC)
	private val stackMember = mutableMapOf<I, Boolean>() // map for faster check whether a node is in stack
	private val result = mutableMapOf<Int, MutableSet<I>>()
	private var indexOfScc = 0

	init {
		require(graph.isDirected) {
			"Strongly connected components cannot be found in an undirected graph '${graph.label}'."
		}
	}

	/**
	 * Finds strongly connected components in a directed graph using Tarjan's algorithm.
	 *
	 * @return a map where keys - the index of each strongly connected component,
	 * and values - sets of vertices belonging to each component
	 */
	fun stronglyConnectedComponents(): Map<Int, Set<I>> {
		// Mark all the vertices as not visited
		graph.idVertices.forEach { discoveryTime[it] = -1 }
		graph.idVertices.forEach { lowLink[it] = -1 }

		graph.idVertices.forEach {
			if (discoveryTime[it] == -1) {
				sccUtil(it)
			}
		}
		return result
	}

	/**
	 * A recursive function that finds strongly connected components using DFS traversal.
	 *
	 * @param currentVertex the current vertex being visited
	 */
	private fun sccUtil(currentVertex: I) {
		discoveryTime[currentVertex] = time
		lowLink[currentVertex] = time
		time++
		stackMember[currentVertex] = true
		stack.push(currentVertex)

		var targetVertex: I
		// Go through all vertices adjacent to this
		graph.vertexEdges(currentVertex).forEach { edge ->
			targetVertex = edge.idTarget
			if (discoveryTime[targetVertex] == -1) {
				sccUtil(targetVertex)

				// Check if the subtree rooted targetVertex has a connection to
				// one of the ancestors of currentVertex
				lowLink[currentVertex] = min(lowLink[currentVertex]!!, lowLink[targetVertex]!!)
			} else if (stackMember[targetVertex] == true) {
				lowLink[currentVertex] = min(lowLink[currentVertex]!!, discoveryTime[targetVertex]!!)
			}
		}

		// The head node is found, pop the stack and place the vertices
		// in the corresponding strongly connected component.
		var vertex: I? = null
		if (lowLink[currentVertex]!! == discoveryTime[currentVertex]) {
			while (vertex != currentVertex) {
				vertex = stack.pop()
				val setVertex = result.getOrDefault(indexOfScc, mutableSetOf())
				setVertex.add(vertex)
				result[indexOfScc] = setVertex
				stackMember[vertex] = false
			}
			indexOfScc++
		}
	}
}
