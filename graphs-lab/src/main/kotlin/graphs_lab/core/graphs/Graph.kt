package graphs_lab.core.graphs

import graphs_lab.core.edges.Edge
import graphs_lab.core.Vertex


abstract class Graph<I, E : Edge<I>>(
	val label: String,
	val isDirected: Boolean = false,
	val isAutoAddVertex: Boolean = false
) {
	private val vertices = mutableMapOf<I, Vertex<I>>()
	private val edges = mutableMapOf<I, MutableSet<E>>()

	fun addVertex(id: I) {
		vertices.putIfAbsent(id, Vertex(id))
	}

	fun removeVertex(id: I) {
		vertices.remove(id)
		clearEdgesVertex(id)
	}

	fun removeEdge(idSource: I, idTarget: I) {
		removeDirectedEdge(idSource, idTarget)
		if (!isDirected) removeDirectedEdge(idTarget, idSource)
	}

	fun containsVertex(id: I): Boolean {
		return vertices[id] != null
	}

	override fun toString(): String {
		val builder = StringBuilder("${javaClass.name}(label = $label")
		builder.append(", vertices = ${vertices.keys}")
		builder.append(", edges = ${edgesToString()}")
		return builder.append(")").toString()
	}

	protected fun addEdge(edge: E) {
		assertContainsVertex(edge.idSource)
		assertContainsVertex(edge.idTarget)
		updateEdgesContainer(edge)
		if (!isDirected) updateEdgesContainer(reverseEdge(edge))
	}

	private fun removeDirectedEdge(idSource: I, idTarget: I) {
		val setEdgesVertex = edges[idSource] ?: return
		for (edge in setEdgesVertex) {
			if (edge.idTarget == idTarget) {
				setEdgesVertex.remove(edge)
				break
			}
		}
		if (setEdgesVertex.isEmpty()) edges.remove(idSource)
	}

	private fun clearEdgesVertex(id: I) {
		for (idVertex in vertices.keys) removeDirectedEdge(idVertex, id)
		edges.remove(id)
	}

	private fun updateEdgesContainer(edge: E) {
		val setEdgesVertex = edges.getOrDefault(edge.idSource, mutableSetOf())
		setEdgesVertex.add(edge)
		edges.putIfAbsent(edge.idSource, setEdgesVertex)
	}

	private fun assertContainsVertex(id: I) {
		if (!isAutoAddVertex && !containsVertex(id)) throw AssertionError(
			"Graph $label is not auto added. Trying add edge with vertex $id that is not in it"
		)
		else addVertex(id)
	}

	private fun edgesToString(): String {
		val viewEdges = mutableSetOf<String>()
		for (idVertex in edges.keys) {
			edges.getOrDefault(idVertex, mutableSetOf()).forEach { edge ->
				viewEdges.add(edgeToString(edge))
			}
		}
		return viewEdges.toString()
	}

	protected abstract fun edgeToString(edge: E): String

	protected abstract fun reverseEdge(edge: E): E

}
