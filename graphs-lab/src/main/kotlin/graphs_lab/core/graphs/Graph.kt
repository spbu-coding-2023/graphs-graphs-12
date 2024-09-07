package graphs_lab.core.graphs

import graphs_lab.core.edges.Edge
import graphs_lab.core.Vertex

/**
 * Provides an abstract representation of a graph structure.
 *
 * @param I the type of the vertex identifiers
 * @param E the type of the edges in the graph
 * @property label the label or name of the graph
 * @property isDirected indicates whether the graph is directed or not
 * @property isAutoAddVertex specifies whether new vertices should be automatically added when adding edges
 * @property idVertices set of all vertex's IDs  which were added at the moment, not updated
 * 						if graph will updated after get it
 * @property size count of vertices in the graph
 */
abstract class Graph<I, E : Edge<I>>(
	val label: String,
	val isDirected: Boolean = false,
	val isAutoAddVertex: Boolean = false
) {
	private val vertices = mutableMapOf<I, Vertex<I>>()
	private val edges = mutableMapOf<I, MutableSet<E>>()
	val idVertices
		get() = vertices.keys.toSet()
	val size: Int
		get() = vertices.size

	/**
	 * Adds a vertex to the graph with the specified id.
	 *
	 * @param id the identifier of the vertex to add
	 */
	fun addVertex(id: I) {
		vertices.putIfAbsent(id, Vertex(id))
	}

	/**
	 * Removes the vertex with the specified id from the graph.
	 *
	 * @param id the identifier of the vertex to remove
	 */
	fun removeVertex(id: I) {
		if (id !in idVertices) return
		vertices.remove(id)
		clearEdgesVertex(id)
	}

	/**
	 * Removes the edge between the vertices with the specified ids from the graph.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 */
	fun removeEdge(idSource: I, idTarget: I) {
		if (idSource !in idVertices || idTarget !in idVertices) return
		removeDirectedEdge(idSource, idTarget)
		if (!isDirected) removeDirectedEdge(idTarget, idSource)
	}

	/**
	 * Checks if the graph contains a vertex with the specified id.
	 *
	 * @param id the identifier of the vertex to check
	 * @return true if the graph contains the specified vertex, false otherwise
	 */
	fun containsVertex(id: I): Boolean {
		return vertices[id] != null
	}

	/**
	 * Аinds all edges coming from a vertex with the ID.
	 *
	 * @param id the identifier of the source vertex
	 * @return set if edges by type [E], if vertex is not contained in the graph return empty set
	 */
	fun vertexEdges(id: I): Set<E> {
		return edges.getOrDefault(id, mutableSetOf())
	}

	override fun toString(): String {
		val builder = StringBuilder("${javaClass.name}(label = $label")
		builder.append(", vertices = ${vertices.keys}")
		builder.append(", edges = ${edgesToString()}")
		return builder.append(")").toString()
	}

	/**
	 * Adds the specified edge to the graph.
	 * If graph is directed add edge from _edge.idTarget_ to _edge.idSource_
	 *
	 * @param edge the edge to add to the graph
	 * @throws AssertionError if graph is not at auto add vertex mode and one of edge's vertex is not in graph
	 */
	protected fun addEdge(edge: E) {
		assertContainsVertex(edge.idSource)
		assertContainsVertex(edge.idTarget)
		updateEdgesContainer(edge)
		if (!isDirected) updateEdgesContainer(reverseEdge(edge))
	}

	/**
	 * Removes the edge between the vertices with the specified ids from the graph.
	 *
	 * @param idSource the identifier of the source vertex
	 * @param idTarget the identifier of the target vertex
	 */
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

	/**
	 * Removes all edges connected to the specified vertex from the graph.
	 *
	 * @param id the identifier of the vertex to clear edges for
	 */
	private fun clearEdgesVertex(id: I) {
		for (idVertex in vertices.keys) removeDirectedEdge(idVertex, id)
		edges.remove(id)
	}

	/**
	 * Updates the container of edges connected to the specified vertex in the graph.
	 *
	 * @param edge the edge to add to the container of edges connected to the specified vertex
	 */
	private fun updateEdgesContainer(edge: E) {
		val setEdgesVertex = edges.getOrDefault(edge.idSource, mutableSetOf())
		setEdgesVertex.add(edge)
		edges.putIfAbsent(edge.idSource, setEdgesVertex)
	}

	/**
	 * Assertion is vertex with [id] contains in graph.
	 * If graph is at auto add vertex mode than add new vertex with [id].
	 *
	 * @param id the vertex's id
	 * @throws AssertionError if graph is not at auto add vertex mode and vertex with [id] not contains in graph
	 */
	private fun assertContainsVertex(id: I) {
		assert(isAutoAddVertex || containsVertex(id)) {
			"Graph $label is not auto added. Trying add edge with vertex $id that is not in it"
		}
		addVertex(id)
	}

	/**
	 * Converts the set of edges into a string representation.
	 *
	 * @return a string representation of the set of edges connected to a vertex.
	 */
	private fun edgesToString(): String {
		val viewEdges = mutableSetOf<String>()
		for (idVertex in edges.keys) {
			edges.getOrDefault(idVertex, mutableSetOf()).forEach { edge ->
				viewEdges.add(edgeToString(edge))
			}
		}
		return viewEdges.toString()
	}

	/**
	 * Converts the edge into a string representation.
	 *
	 * @param edge the weighted edge to convert
	 * @return a string representing the edge
	 */
	protected abstract fun edgeToString(edge: E): String

	/**
	 * Reverses the direction of the specified [edge].
	 *
	 * @param edge the edge to reverse
	 * @return the edge with its source and target vertices swapped
	 */
	protected abstract fun reverseEdge(edge: E): E
}
