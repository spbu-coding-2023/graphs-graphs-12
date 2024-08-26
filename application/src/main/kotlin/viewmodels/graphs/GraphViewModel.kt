package viewmodels.graphs

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import graphs_lab.algs.BellmanFordShortestPath
import graphs_lab.algs.CyclesSearchAlgorithms
import graphs_lab.algs.DijkstraAlgorithm
import graphs_lab.algs.LeaderRank
import graphs_lab.algs.MSTAlgorithms
import graphs_lab.algs.TarjanBridgeFinding
import graphs_lab.algs.TarjanStrongConnectivityInspector
import graphs_lab.algs.clustering.louvainClusteringMethod
import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph
import models.SettingsModel
import models.VertexID
import mu.KotlinLogging
import themes.radiusVerticesStart
import utils.VertexIDType
import views.graphs.colorChangeFlag
import kotlin.random.Random

private val logger = KotlinLogging.logger("GraphViewModel")

/**
 * ViewModel for the Graph. Manages the state of the graph and provides methods for graph operations.
 *
 * @property graph the weighted graph to manage
 * @property vertexType the type of vertices in the graph
 * @property isUnweighted whether the graph is unweighted
 * @property vertices the collection of vertices in the graph
 * @property edges the collection of edges in the graph
 * @property countEdges the number of edges in the graph
 * @property settings application settings to depend on actual window size
 */
class GraphViewModel(
	val graph: WeightedGraph<VertexID>,
	val vertexType: VertexIDType,
	val isUnweighted: Boolean,
	val settings: SettingsModel
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
		logger.info { "Start init graph: ${graph.label}." }
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
		logger.info { "Finish init graph: ${graph.label}." }
	}

	/**
	 * Adds a vertex to the graph with the given id.
	 *
	 * @param id the id of the vertex to be added
	 */
	fun addVertex(id: VertexID) {
		addVertex(
			VertexViewModel(
				id,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					(settings.actualWindowSize.width - radiusVerticesStart.value).toInt()
				).dp,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					(settings.actualWindowSize.height - radiusVerticesStart.value).toInt()
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
		logger.info { "Add vertex with id '${vertex.id}' to graph: ${graph.label}." }
		graph.addVertex(vertex.id)
		_vertices.putIfAbsent(vertex.id, vertex)
	}

	/**
	 * Removes a vertex from the graph and updates the edges accordingly.
	 *
	 * @param id the id of the vertex to be removed
	 */
	fun removeVertex(id: VertexID) {
		logger.info { "Remove vertex with id '$id' from graph: ${graph.label}." }
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
	 * @param idSource the id of the vertex of source of new edge
	 * @param idTarget the id of the vertex of target of new edge
	 * @param weight the weight of new edge
	 */
	fun addEdge(idSource: VertexID, idTarget: VertexID, weight: Double = 1.0) {
		logger.info {
			val weightInfo = if (isUnweighted) "" else "with weight $weight"
			"Add edge between vertices '$idSource' and '$idTarget' $weightInfo on graph: ${graph.label}."
		}
		graph.addEdge(idSource, idTarget, weight)
		val sourceViewModel = _vertices.getOrPut(idSource) {
			// TODO(Create a dependency on the actual size of the program window, and not on the starting size)
			VertexViewModel(
				idSource,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					(settings.actualWindowSize.width - radiusVerticesStart.value).toInt()
				).dp,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					(settings.actualWindowSize.height - radiusVerticesStart.value).toInt()
				).dp,
				degree = 1
			)
		}
		val targetViewModel = _vertices.getOrPut(idTarget) {
			VertexViewModel(
				idTarget,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					(settings.actualWindowSize.width - radiusVerticesStart.value).toInt()
				).dp,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					(settings.actualWindowSize.height - radiusVerticesStart.value).toInt()
				).dp,
				degree = 1
			)
		}
		val edge = graph.vertexEdges(idSource).first { it.idTarget == idTarget }
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
			val edgeUndirected = graph.vertexEdges(idTarget).first { it.idTarget == idSource }
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

	/**
	 * Removes an edge from the graph and updates the vertices' degrees accordingly.
	 *
	 * @param idSource the id of the source vertex of the edge to be removed
	 * @param idTarget the id of the target vertex of the edge to be removed
	 *
	 * @throws NullPointerException if the edge does not exist in the graph or its vertices
	 */
	fun removeEdge(idSource: VertexID, idTarget: VertexID) {
		val sourceVertexViewModel = _vertices[idSource] ?: return
		val targetVertexViewModel = _vertices[idTarget] ?: return

		logger.info { "Remove edge between vertices '$idSource' and '$idTarget' on graph: ${graph.label}." }
		sourceVertexViewModel.degree--
		if (!graph.isDirected) targetVertexViewModel.degree--
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
		logger.info {
			"Run Dijkstra algorithm  which start on '$idSource' and finish on '$idTarget' for graph: ${graph.label}."
		}
		val resultAlgo = DijkstraAlgorithm(graph)
		val path = resultAlgo.getPath(idSource, idTarget) ?: return
		logger.info { "Dijkstra algorithm result: $path." }

		var idLast = idSource
		path.forEach { id ->
			if (idLast != id) {
				val edge = _edges[WeightedEdge(idLast, id, 1.0)] ?: return@forEach
				edge.color = Color(0, 102, 51) // Green
				edge.width = 4f
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
		logger.info {
			"Run Bellman-Ford algorithm  which start on '$idSource' and finish on '$idTarget' for graph: ${graph.label}."
		}
		val resultAlgo = BellmanFordShortestPath(graph)
		val path = resultAlgo.getPath(idSource, idTarget) ?: return
		logger.info { "Bellman-Ford algorithm result: $path." }

		var idLast = idSource
		path.forEach { id ->
			if (idLast != id) {
				val e = _edges[WeightedEdge(idLast, id, 1.0)] ?: return@forEach
				e.color = Color(0, 102, 51) // Green
				e.width = 4f
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
		logger.info {
			"Run Tarjan strong connectivity algorithm for graph: ${graph.label}."
		}
		val resultAlgo = TarjanStrongConnectivityInspector(graph)
		val components = resultAlgo.stronglyConnectedComponents()
		logger.info { "Tarjan strong connectivity algorithm result: $components." }

		for (i in 0 until components.size) {
			val color = Color(
				Random.nextInt(64, 223),
				Random.nextInt(64, 223),
				Random.nextInt(64, 223)
			)
			components[i]?.forEach { id ->
				graph.vertexEdges(id).forEach inlineForEach@{
					val component = components[i] ?: return@inlineForEach
					if (component.contains(it.idTarget)) {
						val e = _edges[WeightedEdge(id, it.idTarget, 1.0)] ?: return@inlineForEach
						e.color = color
						e.width = 4f
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
		logger.info {
			"Run cycles search algorithm for vertex $idVertex on graph: ${graph.label}."
		}
		val resultAlgo = CyclesSearchAlgorithms(graph)
		val cycles = resultAlgo.searchVertexCycles(idVertex)
		logger.info { "Cycles search algorithm result: $cycles." }

		cycles.forEach { cycle ->
			val color = Color(
				Random.nextInt(64, 223),
				Random.nextInt(64, 223),
				Random.nextInt(64, 223)
			)
			cycle.forEach { id ->
				graph.vertexEdges(id).forEach inlineForEach@{
					if (cycle.contains(it.idTarget)) {
						val e = _edges[WeightedEdge(id, it.idTarget, 1.0)] ?: return@inlineForEach
						e.color = color
						e.width = 4f
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
		logger.info {
			"Run Kruskal's MST algorithm for graph: ${graph.label}."
		}
		val resultAlgo = MSTAlgorithms(graph)
		val mst = resultAlgo.kruskalAlgorithm()
		logger.info { "Kruskal's MST algorithm result: $mst." }

		mst.forEach {
			val idSource = it.idSource
			val idTarget = it.idTarget
			graph.vertexEdges(idSource).forEach inlineForEach@{ edge ->
				if (edge.idTarget == idTarget) {
					val e = _edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)] ?: return@inlineForEach
					e.color = Color(83, 55, 122) // Purple
					e.width = 4f
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
		logger.info {
			"Run Prim's MST algorithm for graph: ${graph.label}."
		}
		val resultAlgo = MSTAlgorithms(graph)
		val mst = resultAlgo.primAlgorithm()
		logger.info { "Prim's of MST algorithm result: $mst." }

		mst.forEach {
			val idSource = it.idSource
			val idTarget = it.idTarget
			graph.vertexEdges(idSource).forEach inlineForEach@{ edge ->
				if (edge.idTarget == idTarget) {
					val e = _edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)] ?: return@inlineForEach
					e.color = Color(83, 55, 122) // Purple
					e.width = 4f
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
		logger.info {
			"Run Tarjan's bridge finding algorithm for graph: ${graph.label}."
		}
		val resultAlgo = TarjanBridgeFinding(graph)
		val bridges = resultAlgo.getBridges()
		logger.info { "Tarjan's bridge finding algorithm result: $bridges." }

		bridges.forEach {
			val idSource = it.idSource
			val idTarget = it.idTarget
			graph.vertexEdges(idSource).forEach inlineForEach@{ edge ->
				if (edge.idTarget == idTarget) {
					val e = _edges[WeightedEdge(edge.idSource, edge.idTarget, 1.0)] ?: return@inlineForEach
					e.color = Color(176, 0, 0) // Red
					e.width = 4f
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
		logger.info {
			"Run leader rank algorithm for graph: ${graph.label}."
		}
		val resultAlgo = LeaderRank(graph)
		val scores = resultAlgo.getVerticesScores()
		logger.info { "Leader rank algorithm result: $scores." }

		scores.forEach { (id, score) ->
			val vertex = _vertices[id] ?: return@forEach
			vertex.radius = radiusVerticesStart * score.toFloat()
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
		logger.info {
			"Run Louvain clustering algorithm for graph: ${graph.label}."
		}
		val resultAlgo = louvainClusteringMethod(graph)
		logger.info { "Louvain clustering algorithm result: $resultAlgo." }

		colorChangeFlag.value = true
		resultAlgo.first.getPartition().forEach { cluster ->
			val color = Color(
				Random.nextInt(64, 223),
				Random.nextInt(64, 223),
				Random.nextInt(64, 223)
			)
			cluster.forEach inlineForEach@{ id ->
				val vertex = _vertices[id] ?: return@inlineForEach
				vertex.color = color
			}
		}
	}
}
