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

	fun addEdge(idSource: VertexID, idTarget: VertexID, weight: Double = 1.0) { // todo(fix?!)
		graph.addEdge(idSource, idTarget, weight)
		val sourceViewModel = _vertices.getOrPut(idSource) {
			VertexViewModel(
				idSource,
				Random.nextInt(
					radiusVerticesStart.value.toInt(),
					windowSizeStart.second.toInt() - radiusVerticesStart.value.toInt()
				).dp,  // todo(change 1000 to height)
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

	fun removeEdge(idSource: VertexID, idTarget: VertexID) {
		_vertices[idSource]!!.degree--
		if (!graph.isDirected) _vertices[idTarget]!!.degree--
		_edges.remove(WeightedEdge(idSource, idTarget, 1.0))
		if (!graph.isDirected) _edges.remove(WeightedEdge(idTarget, idSource, 1.0))
		graph.removeEdge(idSource, idTarget)
	}

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

	fun parseLeaderRank() {
		val resultAlgo = LeaderRank(graph)
		val scores = resultAlgo.getVerticesScores()

		var newRadius: Dp
		scores.forEach { (id, score) ->
			newRadius = radiusVerticesStart * score.toFloat()
			if (_vertices[id]!!.radius != newRadius) _vertices[id]!!.radius = newRadius
		}
	}

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
