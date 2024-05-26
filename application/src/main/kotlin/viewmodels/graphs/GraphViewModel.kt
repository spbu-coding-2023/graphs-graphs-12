package viewmodels.graphs

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import graphs_lab.algs.DijkstraAlgorithm
import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import utils.VertexIDType
import views.radiusStart
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
		_vertices.putIfAbsent(id, VertexViewModel(id,
			Random.nextInt(radiusStart.value.toInt(), windowSizeStart.second.toInt() - radiusStart.value.toInt()).dp,
			Random.nextInt(radiusStart.value.toInt(), windowSizeStart.second.toInt() - radiusStart.value.toInt()).dp))
	}

	fun removeVertex(id: VertexID) {
		_vertices.remove(id)
		_edges.forEach {
			if (it.key.idSource == id || it.key.idTarget == id) {
				_edges.remove(it.key)
				if (it.key.idSource == id) {
					it.value.target.degree--
				} else {
					it.value.source.degree--
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
				Random.nextInt(radiusStart.value.toInt(), windowSizeStart.second.toInt() - radiusStart.value.toInt()).dp,  // todo(change 1000 to height)
				Random.nextInt(radiusStart.value.toInt(), windowSizeStart.second.toInt() - radiusStart.value.toInt()).dp,
				degree = 1
			)
		}
		val targetViewModel = _vertices.getOrPut(idTarget) {
			VertexViewModel(
				idTarget,
				Random.nextInt(radiusStart.value.toInt(), windowSizeStart.second.toInt() - radiusStart.value.toInt()).dp,
				Random.nextInt(radiusStart.value.toInt(), windowSizeStart.second.toInt() - radiusStart.value.toInt()).dp,
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

	fun removeEdge(idSource: VertexID, idTarget: VertexID) {
		_vertices[idSource]!!.degree-- // todo(!!)
		_vertices[idTarget]!!.degree-- // todo(!!)
		_edges.remove(WeightedEdge(idSource, idTarget, 1.0))
		if (!graph.isDirected) _edges.remove(WeightedEdge(idTarget, idSource, 1.0))
		graph.removeEdge(idSource, idTarget)
	}

	fun parseDijkstraAlgorithm(idSource: VertexID, idTarget: VertexID) {
		val resultAlgo = DijkstraAlgorithm(graph)
		val path = resultAlgo.getPath(idSource, idTarget) ?: return

		var idLast = idSource
		path.forEach { id ->
			_vertices[id]!!.color = Color.Magenta // todo(!!)
			if (idLast != id) {
				_edges[WeightedEdge(idLast, id, 1.0)]!!.color = Color.Magenta // todo(!!)
				_edges[WeightedEdge(idLast, id, 1.0)]!!.size = 8f
			}
			idLast = id
		}
	}
}
