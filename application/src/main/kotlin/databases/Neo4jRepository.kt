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

/**
 * [Neo4jRepository] class is responsible for connecting, writing, reading, and clearing data in Neo4j database.
 */
class Neo4jRepository : Closeable {
	private lateinit var driver: Driver
	private lateinit var session: Session

	/**
	 * Connects to `Neo4j` database using provided URI, username, and password.
	 *
	 * @param uri the URI of the Neo4j database
	 * @param user the username for authentication
	 * @param password the password for authentication
	 */
	fun connect(uri: String, user: String, password: String) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
		driver.verifyConnectivity()
		session = driver.session()
		println("Connected to Neo4j database")
	}

	/**
	 * Writes the graph data from the provided [GraphPageViewModel] to the Neo4j database.
	 *
	 * @param graphPageView the GraphPageViewModel containing the graph data to be written
	 */
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

	/**
	 * Reads the graph data from the Neo4j database and populates the provided [GraphPageViewModel].
	 *
	 * @param graphPageView the [GraphPageViewModel] to populate with the graph data
	 */
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

	/**
	 * Clears all data from the `Neo4j` database.
	 * This function deletes all vertices and edges from the database.
	 */
	fun clearDB() {
		session.writeTransaction { tx ->
			tx.run("match (v) - [e] -> () delete v, e")
			tx.run("match (v) delete v")
			println("Removed all data from Neo4j database")
		}
	}

	/**
	 * Closes the Neo4j database connection and frees up any resources held by the driver and session.
	 *
	 * This method should be called when the Neo4jRepository instance is no longer needed to ensure proper cleanup.
	 * After calling this method, the Neo4jRepository instance should not be used for any further operations.
	 *
	 * @throws Exception if an error occurs while closing the database connection or releasing resources
	 */
	override fun close() {
		session.close()
		driver.close()
	}
}
