package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import java.lang.IllegalArgumentException
import java.util.Stack
import kotlin.math.min

class TarjanStrongConnectivityInspector<I : Any, E : Edge<I>>(val graph: Graph<I, E>) {
    private var time = 0
    private val discoveryTime = mutableMapOf<I, Int>()
    private val lowLink = mutableMapOf<I, Int>()
    private val stackMember = mutableMapOf<I, Boolean>()
    private val stack = Stack<I>()
    private val result = mutableMapOf<Int, MutableSet<I>>()

    init {
        if (!graph.isDirected) {
            throw IllegalArgumentException(
                "Strongly connected components cannot be found in an undirected graph '${graph.label}'."
            )
        }
    }

    fun stronglyConnectedComponents(): MutableMap<Int, MutableSet<I>> {
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

    private fun sccUtil(
        currentVertex: I
    ) {
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
            val index = result.count()
            while (vertex != currentVertex) {
                vertex = stack.pop()
                val setVertex = result.getOrDefault(index, mutableSetOf())
                setVertex.add(vertex)
                result[index] = setVertex
                stackMember[vertex] = false
            }
        }
    }
}
