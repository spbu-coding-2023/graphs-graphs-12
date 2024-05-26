package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import models.WeightedUnweightedGraph
import org.json.*
import utils.VertexIDType
import viewmodels.graphs.EdgeViewModel
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import java.io.BufferedReader
import java.io.BufferedWriter

class GraphJSONDatabase : FileDatabase<GraphViewModel>() {
	override fun load(reader: BufferedReader): GraphViewModel {
		val graphJSONObject = JSONObject(JSONTokener(reader))
		val isUnweightedGraph = graphJSONObject.getBoolean("is-unweighted")
		val isDirectedGraph = graphJSONObject.getBoolean("is-directed")
		val graph: WeightedGraph<VertexID> = if (isUnweightedGraph) {
			WeightedUnweightedGraph(graphJSONObject.getString("graph-name"), isDirectedGraph, isAutoAddVertex = true)
		} else {
			WeightedGraph(graphJSONObject.getString("graph-name"), isDirectedGraph, isAutoAddVertex = true)
		}
		val vertexIDType = VertexIDType.valueOf(graphJSONObject.getString("vertex-id-type"))
		val graphViewModel = GraphViewModel(graph, vertexIDType, isUnweightedGraph)
		val vertices = graphJSONObject.getJSONArray("vertices")
		vertices.forEach { vertexJSONObject ->
			if (vertexJSONObject !is JSONObject) return@forEach
			graphViewModel.addVertex(
				VertexViewModel(
					VertexID(vertexJSONObject.getString("id"), vertexIDType),
					vertexJSONObject.getFloat("xPos").dp,
					vertexJSONObject.getFloat("yPos").dp,
					Color(vertexJSONObject.getInt("color")),
					vertexJSONObject.getFloat("radius").dp,
				)
			)
		}
		val edges = graphJSONObject.getJSONArray("edges")
		edges.forEach { edgeJSONObject ->
			if (edgeJSONObject !is JSONObject) return@forEach
			val source = VertexID(edgeJSONObject.getString("source-id"), vertexIDType)
			val target = VertexID(edgeJSONObject.getString("target-id"), vertexIDType)
			graphViewModel.addEdge(
				source, target,
				edgeJSONObject.getDouble("weight")
			)
		}
		return graphViewModel
	}

	override fun save(writer: BufferedWriter, obj: GraphViewModel) {
		val graphJSONObject = JSONObject(
			mapOf(
				Pair("graph-name", obj.graph.label),
				Pair("is-unweighted", obj.isUnweighted),
				Pair("is-directed", obj.graph.isDirected),
				Pair("vertex-id-type", obj.vertexType),
				Pair("vertices", verticesToJSON(obj.vertices)),
				Pair("edges", edgesToJSON(obj.edges)),
			)
		)
		graphJSONObject.write(writer, 1, 0)
	}

	private fun edgesToJSON(edges: Collection<EdgeViewModel>): JSONArray {
		val edgesJSONObjects = mutableListOf<JSONObject>()
		edges.forEach { edge ->
			val edgeJSONObject = JSONObject(
				mapOf(
					Pair("source-id", edge.edge.idSource.valueToString()),
					Pair("target-id", edge.edge.idTarget.valueToString()),
					Pair("weight", edge.edge.weight),
				)
			)
			edgesJSONObjects.add(edgeJSONObject)
		}
		return JSONArray(edgesJSONObjects)
	}

	private fun verticesToJSON(vertices: Collection<VertexViewModel>): JSONArray {
		val verticesJSONObjects = mutableListOf<JSONObject>()
		vertices.forEach { vertex ->
			val vertexJSONObject = JSONObject(
				mapOf(
					Pair("id", vertex.label),
					Pair("xPos", vertex.xPos.value),
					Pair("yPos", vertex.yPos.value),
					Pair("color", vertex.color.toArgb()),
					Pair("radius", vertex.radius.value),
				)
			)
			verticesJSONObjects.add(vertexJSONObject)
		}
		return JSONArray(verticesJSONObjects)
	}
}
