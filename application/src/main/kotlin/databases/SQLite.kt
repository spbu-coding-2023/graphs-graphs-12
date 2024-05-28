package databases

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graphs_lab.core.graphs.UnweightedGraph
import graphs_lab.core.graphs.WeightedGraph
import models.VertexID
import mu.KotlinLogging
import utils.VertexIDType
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel
import java.io.Closeable
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

private val logger = KotlinLogging.logger { }

class SqliteRepository(private val labelGraph: String) : Closeable {
	private val connection = DriverManager.getConnection("$DB_DRIVER:$labelGraph.db")
		?: throw SQLException("Cannot connect to database.")
	private val addGraphStatement by lazy {
		connection.prepareStatement(
			"INSERT INTO graphs (name, type, isWeighted, isDirected, isAutoAddVertex) VALUES (?, ?, ?, ?, ?);"
		)
	}
	private val addVertexStatement by lazy {
		connection.prepareStatement(
			"INSERT INTO vertices (id, xPos, yPos, color, radius, degree) VALUES (?, ?, ?, ?, ?, ?);"
		)
	}
	private val addEdgeStatement by lazy {
		connection.prepareStatement(
			"INSERT INTO edges (idSource, idTarget, weight) VALUES (?, ?, ?);"
		)
	}

	init {
		logger.info { "Connected to database: $labelGraph." }
	}

	fun writeDb(graphViewModel: GraphViewModel) {
		createDb()
		addGraph(graphViewModel)
		addVertices(graphViewModel)
		addEdges(graphViewModel)
		closeWrite()
	}

	fun createDb() {
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

	fun addGraph(graphViewModel: GraphViewModel) {
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
		}
	}



	fun addVertices(graphViewModel: GraphViewModel) {
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
		}
	}

	fun addEdges(graphViewModel: GraphViewModel) {
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
		}
	}

	fun closeWrite() {
		addGraphStatement.close()
		addVertexStatement.close()
		addEdgeStatement.close()
		connection.close()
		logger.info { "Connection closed." }
	}

	fun getGraph(labelGraph: String): GraphViewModel? {
		val database = "$DB_DRIVER:$labelGraph.db"

		var graphViewModel: GraphViewModel? = null

		var resultGraphs: ResultSet? = null
		var resultVertices: ResultSet? = null
		var resultEdges: ResultSet? = null

		DriverManager.getConnection(database).use { connection ->
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
				println(tableName)

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
			} catch (exception: Exception) {
				logger.error(exception) { "Cannot load graph." }
			}

			connection.close()
		}

		return graphViewModel
	}

	override fun close() {
		closeWrite()
	}

	companion object {
		private const val DB_DRIVER = "jdbc:sqlite"
	}
}
