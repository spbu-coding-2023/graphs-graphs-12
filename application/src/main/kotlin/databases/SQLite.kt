package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import mu.KotlinLogging
import utils.VertexIDType
import viewmodels.graphs.GraphViewModel
import java.sql.*

private val logger = KotlinLogging.logger { }

class SqliteRepository {
	fun writeDb(graphViewModel: GraphViewModel) {
		val connection = DriverManager.getConnection("$DB_DRIVER:${graphViewModel.graph.label}.db")
			?: throw SQLException("Cannot connect to database.")

		createDb(connection)
		addGraph(graphViewModel, connection)
		addVertices(graphViewModel, connection)
		addEdges(graphViewModel, connection)

		connection.close()
		logger.info { "Connection closed." }
	}

	private fun createDb(connection: Connection) {
		connection.createStatement().also { statement ->
			try {
				statement.execute(
					"CREATE TABLE if not exists graphs(" +
						"name text PRIMARY KEY, " +
						"type string, " +
						"isWeighted int, " +
						"isDirected int, " +
						"isAutoAddVertex int" +
						");"
				)
				statement.execute(
					"CREATE TABLE if not exists vertices(" +
						"id text PRIMARY KEY, " +
						"xPos real, " +
						"yPos real, " +
						"color int, " +
						"radius real, " +
						"degree int" +
						");"
				)
				statement.execute(
					"CREATE TABLE if not exists edges(" +
						"idSource text, " +
						"idTarget text, " +
						"weight real" +
						");"
				)
				logger.info { "Graphs, vertices and edges tables created or already exists." }
			} catch (exception: Exception) {
				logger.error(exception) { "Cannot create tables in database." }
			} finally {
				statement.close()
			}
		}
	}

	private fun addGraph(graphViewModel: GraphViewModel, connection: Connection) {
		val addGraphStatement by lazy {
			connection.prepareStatement(
				"INSERT INTO graphs (name, type, isWeighted, isDirected, isAutoAddVertex) VALUES (?, ?, ?, ?, ?);"
			)
		}

		try {
			val graph = graphViewModel.graph
			addGraphStatement.setString(1, graph.label)
			addGraphStatement.setString(2, graphViewModel.vertexType.name)
			addGraphStatement.setBoolean(3, !graphViewModel.isUnweighted)
			addGraphStatement.setBoolean(4, graph.isDirected)
			addGraphStatement.setBoolean(5, graph.isAutoAddVertex)

			addGraphStatement.execute()
			logger.info { "Added ${graph.label} graph." }
		} catch (exception: Exception) {
			logger.error(exception) { "Cannot add ${graphViewModel.graph.label} graph." }
		} finally {
			addGraphStatement.close()
		}
	}

	private fun addVertices(graphViewModel: GraphViewModel, connection: Connection) {
		val addVertexStatement by lazy {
			connection.prepareStatement(
				"INSERT INTO vertices (id, xPos, yPos, color, radius, degree) VALUES (?, ?, ?, ?, ?, ?);"
			)
		}

		try {
			graphViewModel.vertices.forEach { vertexViewModel ->
				addVertexStatement.setString(1, vertexViewModel.id.valueToString())
				addVertexStatement.setFloat(2, vertexViewModel.xPos.value)
				addVertexStatement.setFloat(3, vertexViewModel.yPos.value)
				addVertexStatement.setLong(4, vertexViewModel.color.value.toLong())
				addVertexStatement.setFloat(5, vertexViewModel.radius.value)
				addVertexStatement.setInt(6, vertexViewModel.degree)

				addVertexStatement.execute()
			}
			logger.info { "Added vertices." }
		} catch (exception: Exception) {
			logger.error(exception) { "Cannot add vertices." }
		} finally {
			addVertexStatement.close()
		}
	}

	private fun addEdges(graphViewModel: GraphViewModel, connection: Connection) {
		val addEdgeStatement by lazy {
			connection.prepareStatement(
				"INSERT INTO edges (idSource, idTarget, weight) VALUES (?, ?, ?);"
			)
		}

		try {
			graphViewModel.edges.forEach { edgeViewModel ->
				addEdgeStatement.setString(1, edgeViewModel.source.id.valueToString())
				addEdgeStatement.setString(2, edgeViewModel.target.id.valueToString())
				addEdgeStatement.setDouble(3, edgeViewModel.edge.weight)

				addEdgeStatement.execute()
			}
			logger.info { "Added edges." }
		} catch (exception: Exception) {
			logger.error(exception) { "Cannot add edges." }
		} finally {
			addEdgeStatement.close()
		}
	}

	fun loadGraph(pathToDB: String): GraphViewModel? {
		var graphViewModel: GraphViewModel? = null

		var resultGraphs: ResultSet? = null
		var resultVertices: ResultSet? = null
		var resultEdges: ResultSet? = null

		DriverManager.getConnection("$DB_DRIVER:$pathToDB.db").use { connection ->
			val metaData = connection.metaData
			val tables = metaData.getTables(null, null, "%", null)

			val getGraphStatement by lazy {
				connection.prepareStatement(
					"SELECT graphs.name as name, " +
						"graphs.type as type, " +
						"graphs.isWeighted as isWeighted, " +
						"graphs.isDirected as isDirected, " +
						"graphs.isAutoAddVertex as isAutoAddVertex " +
						"FROM graphs" +
						";"
				)
			}
			val getVertexStatement by lazy {
				connection.prepareStatement(
					"SELECT vertices.id as id, " +
						"vertices.xPos as xPos, " +
						"vertices.yPos as yPos, " +
						"vertices.color as color, " +
						"vertices.radius as radius, " +
						"vertices.degree as degree " +
						"FROM vertices" +
						";"
				)
			}
			val getEdgeStatement by lazy {
				connection.prepareStatement(
					"SELECT edges.idSource as idSource, " +
						"edges.idTarget as idTarget, " +
						"edges.weight as weight " +
						"FROM edges" +
						";"
				)
			}

			while (tables.next()) {
				val tableName = tables.getString("TABLE_NAME")

				if (tableName == "graphs") {
					resultGraphs = getGraphStatement.executeQuery()
				} else if (tableName == "vertices") {
					resultVertices = getVertexStatement.executeQuery()
				} else if (tableName == "edges") {
					resultEdges = getEdgeStatement.executeQuery()
				}
			}

			val vertexMap = mutableMapOf<VertexID, VertexData>()

			try {
				val label = resultGraphs!!.getString(1)
				val type = resultGraphs!!.getString(2)
				val isWeighted = resultGraphs!!.getBoolean(3)
				val isDirected = resultGraphs!!.getBoolean(4)
				val isAutoAddVertex = resultGraphs!!.getBoolean(5)

				val typeId = if (type == "String") VertexIDType.STRING_TYPE else VertexIDType.INT_TYPE
				graphViewModel = GraphViewModel(WeightedGraph(label, isDirected, isAutoAddVertex), typeId, !isWeighted)

				while (resultVertices!!.next()) {
					val id = resultVertices!!.getString(1)
					val xPos = resultVertices!!.getFloat(2)
					val yPos = resultVertices!!.getFloat(3)
					val color = resultVertices!!.getLong(4)
					val radius = resultVertices!!.getFloat(5)
					val degree = resultVertices!!.getInt(6)

					graphViewModel!!.addVertex(VertexID.vertexIDFromString(id, typeId))
					vertexMap[VertexID.vertexIDFromString(id, typeId)] = VertexData(xPos.dp, yPos.dp, radius.dp, Color(color), degree)
				}

				while (resultEdges!!.next()) {
					val idSource = resultEdges!!.getString(1)
					val idTarget = resultEdges!!.getString(2)
					val weight = resultEdges!!.getDouble(3)

					graphViewModel!!.addEdge(VertexID.vertexIDFromString(idSource, typeId), VertexID.vertexIDFromString(idTarget, typeId), weight)
				}

				graphViewModel!!.vertices.forEach {
					val vertex = vertexMap[it.id] ?: return@forEach
					it.xPos = vertex.x.value.dp
					it.yPos = vertex.y.value.dp
					it.color = vertex.color
					it.degree = vertex.degree
				}

				logger.info { "Loaded $pathToDB graph." }
			} catch (exception: Exception) {
				logger.error(exception) { "Cannot load $pathToDB graph." }
			} finally {
				connection.close()
			}
		}

		return graphViewModel
	}

	fun clear(pathToDB: String) {
		DriverManager.getConnection("$DB_DRIVER:$pathToDB.db").use { connection ->
			val metaData = connection.metaData
			val tables = metaData.getTables(null, null, "%", null)

			val deleteGraphStatement by lazy { connection.prepareStatement("DELETE FROM graphs;") }
			val deleteVertexStatement by lazy { connection.prepareStatement("DELETE FROM vertices;") }
			val deleteEdgeStatement by lazy { connection.prepareStatement("DELETE FROM edges;") }

			while (tables.next()) {
				val tableName = tables.getString("TABLE_NAME")
				println(tableName)

				if (tableName == "graphs") {
					deleteGraphStatement.execute()
				} else if (tableName == "vertices") {
					deleteVertexStatement.execute()
				} else if (tableName == "edges") {
					deleteEdgeStatement.execute()
				}
			}

			connection.close()
			logger.info { "Clearing connection is closed." }
		}
	}

	companion object {
		private const val DB_DRIVER = "jdbc:sqlite"
	}
}
