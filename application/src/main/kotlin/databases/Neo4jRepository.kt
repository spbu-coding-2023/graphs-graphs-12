package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import models.WeightedUnweightedGraph
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Session
import utils.VertexIDType
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel
import java.io.Closeable

class Neo4jRepository : Closeable {
	private lateinit var driver: Driver
	private lateinit var session: Session

	fun connect(uri: String, user: String, password: String) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
		driver.verifyConnectivity()
		session = driver.session()
		println("Connected to Neo4j database")
	}

	fun writeData(graphPageView: GraphPageViewModel) {
		clearDB()

		session.writeTransaction { tx ->
			val label = graphPageView.graphViewModel?.graph?.label
			val isDirected = graphPageView.graphViewModel?.graph?.isDirected
			val isAutoAddVertex = graphPageView.graphViewModel?.graph?.isAutoAddVertex
			val isUnweighted = graphPageView.graphViewModel?.isUnweighted
			val vertexType = graphPageView.graphViewModel?.vertexType
			tx.run(
				"CREATE (:GraphInfo {" +
					"label: '$label', " +
					"isDirected: ${isDirected}, " +
					"isAutoAddVertex: ${isAutoAddVertex}, " +
					"isUnweighted: ${isUnweighted}, " +
					"vertexType: '${vertexType}'" +
					"})"
			)
		}

		graphPageView.graphViewModel?.vertices?.forEach {
			session.writeTransaction { tx ->
				tx.run(
					"CREATE (:Vertex{" +
						"label: '${it.label}', " +
						"xPos: ${it.xPos.value}, " +
						"yPos: ${it.yPos.value}, " +
						"radius: ${it.radius.value}, " +
						"color: '${it.color.value}', " +
						"degree: ${it.degree}" +
						"})"
				)
			}
		}

		graphPageView.graphViewModel?.edges?.forEach {
			session.writeTransaction { tx ->
				tx.run(
					"MATCH (source:Vertex) WHERE source.label = '${it.source.label}' " +
						"MATCH (target:Vertex) WHERE target.label = '${it.target.label}' " +
						"CREATE (source)-[:Edge {" +
							"weight: ${it.edge.weight}, " +
							"source: '${it.source.label}', " +
							"target: '${it.target.label}'" +
						"}]->(target) "
				)
			}
		}

		println("Graph was saved")
	}

	fun readData(graphPageView: GraphPageViewModel) {
		val vertexMap = mutableMapOf<VertexID, VertexData>()

		session.readTransaction { tx ->
			var result =
				tx.run(
					"MATCH (g:GraphInfo) RETURN " +
						"g.label AS label, " +
						"g.isDirected AS isDirected, " +
						"g.isAutoAddVertex AS isAutoAddVertex, " +
						"g.isUnweighted AS isUnweighted, " +
						"g.vertexType AS vertexType"
				)
			val record = result.stream().findFirst().get()
			val label = record["label"].asString()
			val isDirected = record["isDirected"].toString().toBoolean()
			val isAutoAddVertex = record["isAutoAddVertex"].toString().toBoolean()
			val isUnweighted = record["isUnweighted"].toString().toBoolean()
			val vertexTypeString = record["vertexType"].asString()
			var vertexType: VertexIDType = VertexIDType.INT_TYPE
			when(vertexTypeString) {
				"Int" -> vertexType = VertexIDType.INT_TYPE
				"String" -> vertexType = VertexIDType.STRING_TYPE
			}

			val graph = when (isUnweighted) {
				true ->
					WeightedGraph<VertexID>(
						label, isDirected = isDirected, isAutoAddVertex = isAutoAddVertex
					)

				false -> WeightedUnweightedGraph<VertexID>(
					label, isDirected = isDirected, isAutoAddVertex = isAutoAddVertex
				)
			}

			result =
				tx.run(
					"MATCH (v:Vertex) RETURN " +
						"v.label AS label, " +
						"v.xPos AS xPos, " +
						"v.yPos AS yPos, " +
						"v.radius AS radius, " +
						"v.color AS color, " +
						"v.degree AS degree"
				)
			result.stream().forEach {
				val vertex = it["label"].asString()
				val xPos = it["xPos"].toString().toDouble().dp
				val yPos = it["yPos"].toString().toDouble().dp
				val radius = it["radius"].toString().toDouble().dp
				val color = Color(it["color"].asString().toULong())
				val degree = it["degree"].toString().toInt()

				graph.addVertex(VertexID.vertexIDFromString(vertex, vertexType))
				vertexMap[VertexID.vertexIDFromString(vertex, vertexType)] =
							VertexData(x = xPos, y = yPos, radius = radius, color = color, degree = degree)
			}

			result =
				tx.run(
					"MATCH ()-[e:Edge]->() RETURN " +
						"e.source AS source, " +
						"e.target AS target, " +
						"e.weight AS weight"
				)
			result.stream().forEach {
				graph.addEdge(
					VertexID.vertexIDFromString(it["source"].asString(), vertexType),
					VertexID.vertexIDFromString(it["target"].asString(), vertexType),
					(it["weight"].toString().toDouble())
				)
			}

			graphPageView.graphViewModel = GraphViewModel(graph, vertexType, isUnweighted)

			graphPageView.graphViewModel!!.vertices.forEach {
				val vertex = vertexMap[it.id] ?: return@forEach
				it.xPos = vertex.x.value.dp
				it.yPos = vertex.y.value.dp
				it.radius = vertex.radius
				it.color = vertex.color
				it.degree = vertex.degree
			}
		}
		println("Graph was loaded")
	}

	fun clearDB() {
		session.writeTransaction { tx ->
			tx.run("match (v) - [e] -> () delete v, e")
			tx.run("match (v) delete v")
			println("Removed all data from Neo4j database")
		}
	}

	override fun close() {
		session.close()
		driver.close()
	}
}
