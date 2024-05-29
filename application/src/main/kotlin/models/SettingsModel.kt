package models

import databases.GraphJSONDatabase
import databases.Neo4jRepository
import models.utils.GraphInfo
import org.neo4j.driver.exceptions.ServiceUnavailableException
import utils.GraphSavingType
import utils.PageType
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel
import java.io.File

class SettingsModel {
	val jsonDB = GraphJSONDatabase()
	val neo4jDB = Neo4jRepository()
	var isNeo4jConnected = false
	val graphNameRegEx = Regex("[a-zA-Z][a-zA-Z0-9_-]*")

	fun connectToNeo4J(uri: String, user: String, password: String) {
		isNeo4jConnected = false
		try {
			neo4jDB.connect(uri, user, password)
			isNeo4jConnected = true
		} catch (ex: ServiceUnavailableException) {
			println("Can't access to Neo4J: ${ex.message}")
		} catch (ex: IllegalArgumentException) {
			println("Neo4j invalid connection: ${ex.message}")
		}
	}

	fun saveGraph(
		graphPageViewModel: GraphPageViewModel,
		savingType: GraphSavingType,
		folderPath: String
	): GraphInfo? {
		val graphViewModel = graphPageViewModel.graphViewModel ?: return null
		when (savingType) {
			GraphSavingType.LOCAL_FILE -> saveGraphByJSON(graphViewModel, folderPath)
			GraphSavingType.NEO4J_DB -> {
				if (!isNeo4jConnected) return null
				saveGraphByNeo4j(graphPageViewModel)
			}
			else -> {
				println("Unsupported saving type: ${savingType.label}")
				return null
			}
		}
		return GraphInfo(
			graphViewModel.graph.label,
			folderPath,
			savingType,
			onClick = { name, folder, saveType ->
				when (saveType) {
					GraphSavingType.LOCAL_FILE -> loadGraphFromJSON(graphPageViewModel, name, folder)
					GraphSavingType.NEO4J_DB -> loadGraphFromNEO4J(graphPageViewModel, name)
					else -> println("Unsupported saving type: ${savingType.label}")
				}
			}
		)
	}

	private fun loadGraphFromNEO4J(graphPageViewModel: GraphPageViewModel, name: String) {
		if (!isNeo4jConnected) {
			println("Can't load graph: $name because not found connection to neo4j DB")
			return
		}
		try {
			neo4jDB.writeData(graphPageViewModel)
		} catch (ex: Exception) {
			println("Load neo4j error: ${ex.message}")
		}
	}

	private fun loadGraphFromJSON(graphPageViewModel: GraphPageViewModel, name: String, folder: String) {
		try {
			val graphViewModel = jsonDB.load(File(folder, name))
			graphPageViewModel.graphViewModel = graphViewModel
			graphPageViewModel.indexSelectedPage.value = PageType.GRAPH_VIEW_PAGE.ordinal
		} catch (ex: Exception) {
			println("Load JSON error: ${ex.message}")
		}
	}

	private fun saveGraphByNeo4j(graphPageViewModel: GraphPageViewModel) {
		if (!isNeo4jConnected) {
			println("Can't save graph because not found connection to neo4j DB")
			return
		}
		try {
			neo4jDB.writeData(graphPageViewModel)
		} catch (ex: Exception) {
			println("Save neo4j error: ${ex.message}")
		}
	}

	private fun saveGraphByJSON(graphViewModel: GraphViewModel, folderPath: String) {
		val file = File(folderPath, graphViewModel.graph.label)
		try {
			jsonDB.save(file, graphViewModel)
		} catch (ex: Exception) {
			println("Save JSON error: ${ex.message}")
		}
	}

	companion object {
		@JvmStatic
		fun loadSettings(): SettingsModel {
			// TODO(valid load of settings from configuration)
			return SettingsModel()
		}
	}
}
