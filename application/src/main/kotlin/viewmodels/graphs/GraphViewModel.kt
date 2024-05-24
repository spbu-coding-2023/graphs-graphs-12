package viewmodels.graphs

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.unit.dp
import graphs_lab.core.edges.Edge
import graphs_lab.core.edges.WeightedEdge
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import utils.VertexIDType
import views.radiusStart
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
	val sizeEdges: Int
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
		_vertices.putIfAbsent(id, VertexViewModel(id, Random.nextInt(0, 1000).dp, Random.nextInt(0, 1000).dp))
	}

	fun removeVertex(id: VertexID) {
		_vertices.remove(id)
		_edges.forEach {
			if (it.key.idSource == id || it.key.idTarget == id) {
				_edges.remove(it.key)
			}
		}
		graph.removeVertex(id)
	}

	fun addEdge(idSource: VertexID, idTarget: VertexID, weight: Double = 1.0) { // todo(fix?!)
		graph.addEdge(idSource, idTarget, weight)
		val sourceViewModel = _vertices.getOrPut(idSource) {
			VertexViewModel(
				idSource,
				Random.nextInt(0 + radiusStart, 1000 - radiusStart).dp,  // todo(change 1000 to height)
				Random.nextInt(0 + radiusStart, 1000 - radiusStart).dp
			)
		}
		val targetViewModel = _vertices.getOrPut(idTarget) {
			VertexViewModel(
				idTarget,
				Random.nextInt(0 + radiusStart, 1000 - radiusStart).dp,
				Random.nextInt(0 + radiusStart, 1000 - radiusStart).dp
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
		graph.removeEdge(idSource, idTarget)
		_edges.remove(Edge(idSource, idTarget))
		if (!graph.isDirected) _edges.remove(Edge(idTarget, idSource))
	}
}
