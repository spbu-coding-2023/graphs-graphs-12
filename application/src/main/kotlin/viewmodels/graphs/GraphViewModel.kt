package viewmodels.graphs

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graphs_lab.algs.*
import graphs_lab.algs.clustering.louvainClusteringMethod
import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import themes.radiusVerticesStart
import utils.VertexIDType
import views.graphs.colorChangeFlag
import windowSizeStart
import kotlin.random.Random

/**
 * ViewModel for the Graph. Manages the state of the graph and provides methods for graph operations.
 *
 * @property graph the weighted graph to manage
 * @property vertexType the type of vertices in the graph
 * @property isUnweighted whether the graph is unweighted
 * @property vertices the collection of vertices in the graph
 * @property edges the collection of edges in the graph
 * @property countEdges the number of edges in the graph
 */
class GraphViewModel(
	val graph: WeightedGraph<VertexID>,
	val vertexType: VertexIDType,
	val isUnweighted: Boolean,
) {
	private val _vertices = mutableStateMapOf<VertexID, VertexViewModel>()
	val vertices: Collection<VertexViewModel>
		get() = _vertices.values
	private val _edges = mutableStateMapOf<WeightedEdge<VertexID>, EdgeViewModel>()
	val edges: Collection<EdgeViewModel>
		get() = _edges.values
	val countEdges: Int
		get() = if (graph.isDirected) _edges.size else _edges.size / 2

	init {
		graph.idVertices.forEach { idVertex ->
			val sourceViewModel = _vertices.getOrDefault(idVertex, VertexViewModel(idVertex))
			_vertices.putIfAbsent(idVertex, sourceViewModel)

			graph.vertexEdges(idVertex).forEach { edge ->
				val targetViewModel = _vertices.getOrDefault(edge.idTarget, VertexViewModel(edge.idTarget))
				_vertices.putIfAbsent(edge.idTarget, targetViewModel)
				_edges.putIfAbsent(
					edge,
					EdgeViewModel(
						edge,
						sourceViewModel,
						targetViewModel,
						isUnweighted
					)
				)
				sourceViewModel.degree++
				if (graph.isDirected) targetViewModel.degree++
			}
		}
	}

	/**
	 * Adds a vertex to the graph with the given id.
	 *
	 * @param id the id of the vertex to be added
	 */
	fun addVertex(id: VertexID) {
		graph.addVertex(id)
		_vertices.putIfAbsent(
			id, VertexViewModel(
				id,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp
			)
		)
	}

	/**
	 * Adds a vertex to the graph using the provided [VertexViewModel].
	 *
	 * @param vertex the [VertexViewModel] representing the vertex to be added
	 */
	fun addVertex(vertex: VertexViewModel) {
		graph.addVertex(vertex.id)
		_vertices.putIfAbsent(vertex.id, vertex)
	}

	fun removeVertex(id: VertexID) {
		_vertices.remove(id)
		_edges.forEach {
			if (it.key.idSource == id || it.key.idTarget == id) {
				_edges.remove(it.key)
				if (it.key.idSource == id) {
					it.value.target.degree--
					if (!graph.isDirected) it.value.source.degree--
				} else {
					it.value.source.degree--
					if (!graph.isDirected) it.value.target.degree--
				}
			}
		}
		graph.removeVertex(id)
	}

	/**
	 * Removes a vertex from the graph and updates the edges accordingly.
	 *
	 * @param id the id of the vertex to be removed
	 */
	fun addEdge(idSource: VertexID, idTarget: VertexID, weight: Double = 1.0) {
		graph.addEdge(idSource, idTarget, weight)
		val sourceViewModel = _vertices.getOrPut(idSource) {
			// TODO(Create a dependency on the actual size of the program window, and not on the starting size)
			VertexViewModel(
				idSource,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp,
				degree = 1
			)
		}
		val targetViewModel = _vertices.getOrPut(idTarget) {
			VertexViewModel(
				idTarget,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp,
				degree = 1
			)
		}
		val edge = graph.vertexEdges(idSource).filter { it.idTarget == idTarget }.first()
		_edges.putIfAbsent(
			edge,
			EdgeViewModel(
				edge,
				sourceViewModel,
				targetViewModel,
				isUnweighted
			)
		)
		if (!graph.isDirected) {
			val edgeUndirected = graph.vertexEdges(idTarget).filter { it.idTarget == idSource }.first()
			_edges.putIfAbsent(
				edgeUndirected,
				EdgeViewModel(
					edgeUndirected,
					targetViewModel,
					sourceViewModel,
					isUnweighted
				)
			)
		}
	}

	// TODO(remove !! nullable assertion in all method lower)
	/**
	 * Removes an edge from the graph and updates the vertices' degrees accordingly.
	 *
	 * @param idSource the id of the source vertex of the edge to be removed
	 * @param idTarget the id of the target vertex of the edge to be removed
	 *
	 * @throws NullPointerException if the edge does not exist in the graph or its vertices
	 */
	fun removeEdge(idSource: VertexID, idTarget: VertexID) {
		_vertices[idSource]!!.degree--
		if (!graph.isDirected) _vertices[idTarget]!!.degree--
		_edges.remove(WeightedEdge(idSource, idTarget, 1.0))
		if (!graph.isDirected) _edges.remove(WeightedEdge(idTarget, idSource, 1.0))
		graph.removeEdge(idSource, idTarget)
	}

	/**
	 * Parses and visualizes the shortest path between two vertices using the Dijkstra's algorithm.
	 *
	 * @param idSource the id of the source vertex
	 * @param idTarget the id of the target vertex
	 *
	 * @throws NullPointerException if the path contains vertices or edges
	 *
	 * @see DijkstraAlgorithm
	 */
	fun parseDijkstraAlgorithm(idSource: VertexID, idTarget: VertexID) {
		val resultAlgo = DijkstraAlgorithm(graph)
		val path = resultAlgo.getPath(idSource, idTarget) ?: return

		var idLast = idSource
		path.forEach { id ->
			if (idLast != id) {
				_edges[WeightedEdge(idLast, id, 1.0)]!!.color = Color(0, 102, 51) // Green
				_edges[WeightedEdge(idLast, id, 1.0)]!!.width = 4f
			}
			idLast = id
		}
	}

	/**
	 * Parses and visualizes the shortest path between two vertices using the Bellman-Ford algorithm.
	 *
	 * @param idSource the id of the source vertex
	 * @param idTarget the id of the target vertex
	 *
	 * @throws NullPointerException if the path contains vertices or edges
	 *
	 * @see BellmanFordShortestPath
	 */
	fun parseBellmanFordAlgorithm(idSource: VertexID, idTarget: VertexID) {
		val resultAlgo = BellmanFordShortestPath(graph)
		val path = resultAlgo.getPath(idSource, idTarget) ?: return

		var idLast = idSource
		path.forEach { id ->
			if (idLast != id) {
				_edges[WeightedEdge(idLast, id, 1.0)]!!.color = Color(0, 102, 51) // Green
				_edges[WeightedEdge(idLast, id, 1.0)]!!.width = 4f
			}
			idLast = id
		}
	}

	/**
	 * Parses and visualizes the strongly connected components of the graph using Tarjan's algorithm.
	 *
	 * @throws NullPointerException if the graph is null or the components contain vertices
	 *
	 * @see TarjanStrongConnectivityInspector
	 */
	fun parseTarjanStrongConnectivityAlgorithm() {
		val resultAlgo = TarjanStrongConnectivityInspector(graph)
		val components = resultAlgo.stronglyConnectedComponents()
		var color: Color

		for (i in 0 until components.size) {
			color = Color(Random.nextInt(64, 223), Random.nextInt(64, 223), Random.nextInt(64, 223))
			components[i]?.forEach { id ->
				graph.vertexEdges(id).forEach {
					if (components[i]!!.contains(it.idTarget)) {
						_edges[WeightedEdge(id, it.idTarget, 1.0)]!!.color = color
						_edges[WeightedEdge(id, it.idTarget, 1.0)]!!.width = 4f
					}
				}
			}
		}
	}

	/**
	 * Parses and visualizes the cycles in the graph that include the given vertex using the CyclesSearchAlgorithms.
	 *
	 * @param idVertex the id of the vertex to search for cycles
	 *
	 * @throws NullPointerException if the graph is null or the cycles contain vertices
	 *
	 * @see CyclesSearchAlgorithms
	 */
	fun parseCyclesSearchAlgorithm(idVertex: VertexID) {
		val resultAlgo = CyclesSearchAlgorithms(graph)
		val cycles = resultAlgo.searchVertexCycles(idVertex)
		var color: Color

		cycles.forEach { cycle ->
			color = Color(Random.nextInt(64, 223), Random.nextInt(64, 223), Random.nextInt(64, 223))
			cycle.forEach { id ->
				graph.vertexEdges(id).forEach {
					if (cycle.contains(it.idTarget)) {
						_edges[WeightedEdge(id, it.idTarget, 1.0)]!!.color = color
						_edges[WeightedEdge(id, it.idTarget, 1.0)]!!.width = 4f
					}
				}
			}
		}
	}

	/**
	 * Parses and visualizes the Minimum Spanning Tree (MST) of the graph using Kruskal's algorithm.
	 *
	 * @throws NullPointerException if the graph is null or the MST contains edges
	 *
	 * @see MSTAlgorithms.kruskalAlgorithm
	 */
	fun parseKruskalAlgorithm() {
		val resultAlgo = MSTAlgorithms(graph)
		val mst = resultAlgo.kruskalAlgorithm()

		mst.forEach {
			val idSource = it.idSource
			val idTarget = it.idTarget
			graph.vertexEdges(idSource).forEach { edge ->
				if (edge.idTarget == idTarget) {
					_edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)]!!.color = Color(83, 55, 122) // Purple
					_edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)]!!.width = 4f
				}
			}
		}
	}

	/**
	 * Parses and visualizes the Minimum Spanning Tree (MST) of the graph using Prim's algorithm.
	 *
	 * @throws NullPointerException if the graph is null or the MST contains edges
	 *
	 * @see MSTAlgorithms.primAlgorithm
	 */
	fun parsePrimAlgorithm() {
		val resultAlgo = MSTAlgorithms(graph)
		val mst = resultAlgo.primAlgorithm()

		mst.forEach {
			val idSource = it.idSource
			val idTarget = it.idTarget
			graph.vertexEdges(idSource).forEach { edge ->
				if (edge.idTarget == idTarget) {
					_edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)]!!.color = Color(83, 55, 122) // Purple
					_edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)]!!.width = 4f
				}
			}
		}
	}

	/**
	 * Parses and visualizes the bridges in the graph using Tarjan's bridge finding algorithm.
	 *
	 * @throws NullPointerException if the graph is null or the bridges contain edges
	 *
	 * @see TarjanBridgeFinding
	 */
	fun parseTarjanBridgeFinding() {
		val resultAlgo = TarjanBridgeFinding(graph)
		val bridges = resultAlgo.getBridges()

		bridges.forEach {
			val idSource = it.idSource
			val idTarget = it.idTarget
			graph.vertexEdges(idSource).forEach { edge ->
				if (edge.idTarget == idTarget) {
					_edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)]!!.color = Color(176, 0, 0) // Red
					_edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)]!!.width = 4f
				}
			}
		}
	}

	/**
	 * Parses and visualizes the scores of vertices using the LeaderRank algorithm.
	 * The scores are used to adjust the size of the vertices in the graph.
	 *
	 * @throws NullPointerException if the graph is null or the scores contain vertices
	 *
	 * @see LeaderRank
	 */
	fun parseLeaderRank() {
		val resultAlgo = LeaderRank(graph)
		val scores = resultAlgo.getVerticesScores()

		var newRadius: Dp
		scores.forEach { (id, score) ->
			newRadius = radiusVerticesStart * score.toFloat()
			if (_vertices[id]!!.radius != newRadius) _vertices[id]!!.radius = newRadius
		}
	}

	/**
	 * Parses and visualizes the clusters in the graph using the Louvain Clustering algorithm.
	 * The clusters are used to adjust the color of the vertices in the graph.
	 *
	 * @throws NullPointerException if the graph is null or the clusters contain vertices
	 *
	 * @see louvainClusteringMethod
	 */
	fun parseLouvainClustering() {
		val resultAlgo = louvainClusteringMethod(graph)
		colorChangeFlag.value = true
		var color: Color

		resultAlgo.first.getPartition().forEach { cluster ->
			color = Color(Random.nextInt(64, 223), Random.nextInt(64, 223), Random.nextInt(64, 223))
			cluster.forEach { id ->
				_vertices[id]!!.color = color
			}
		}
	}
}
