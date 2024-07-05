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

/**
 * Graph json database.
 * Its simple implementation of [FileDatabase] for [GraphViewModel] object to `load`/`save`.
 */
class GraphJSONDatabase : FileDatabase<GraphViewModel>(".json") {
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
					vertexJSONObject.getInt("degree")
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

	/**
	 * Converts a collection of [EdgeViewModel] objects into a JSONArray.
	 * Each [EdgeViewModel] is represented as a JSONObject with the following properties:
	 * - "source-id": The string representation of the source vertex ID.
	 * - "target-id": The string representation of the target vertex ID.
	 * - "weight": The weight of the edge.
	 *
	 * @param edges The collection of [EdgeViewModel] objects to be converted.
	 * @return A JSONArray containing the JSON representations of the [EdgeViewModel] objects.
	 */
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

	/**
	 * Converts a collection of [VertexViewModel] objects into a JSONArray.
	 * Each [VertexViewModel] is represented as a JSONObject with the following properties:
	 * - "id": The string representation of the vertex ID.
	 * - "xPos": The x-coordinate position of the vertex.
	 * - "yPos": The y-coordinate position of the vertex.
	 * - "color": The color of the vertex represented as an integer in ARGB format.
	 * - "radius": The radius of the vertex.
	 * - "degree": The degree of the vertex.
	 *
	 * @param vertices The collection of [VertexViewModel] objects to be converted.
	 * @return A JSONArray containing the JSON representations of the [VertexViewModel] objects.
	 */
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
					Pair("degree", vertex.degree)
				)
			)
			verticesJSONObjects.add(vertexJSONObject)
		}
		return JSONArray(verticesJSONObjects)
	}
}
