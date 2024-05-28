package models

import databases.GraphJSONDatabase
import databases.Neo4jRepository
import utils.GraphSavingType
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel
import java.io.File

class SettingsModel {
	val jsonDB = GraphJSONDatabase()
	val neo4jDB = Neo4jRepository()
	val graphNameRegEx = Regex("[a-zA-Z][a-zA-Z0-9_-]*")

	fun saveGraph(
		graphPageViewModel: GraphPageViewModel,
		savingType: GraphSavingType,
		folderPath: String
	) {
		val graphViewModel = graphPageViewModel.graphViewModel ?: return
		when (savingType) {
			GraphSavingType.LOCAL_FILE -> saveGraphByJSON(graphViewModel, folderPath)
			GraphSavingType.NEO4J_DB -> saveGraphByNeo4j(graphPageViewModel)
			else -> println("Unsupported saving type: ${savingType.label}")
		}
	}

	private fun saveGraphByNeo4j(graphPageViewModel: GraphPageViewModel) {
		val uri = "bolt://localhost:7687"
		val password = "neo4j"
		val user = "neo4j"
		try {
			neo4jDB.connect(uri, user, password)
			neo4jDB.writeData(graphPageViewModel)
		}
		catch (e: Exception) {
			println(e.message)
		}
	}

	private fun saveGraphByJSON(graphViewModel: GraphViewModel, folderPath: String) {
		val file = File(folderPath)
		jsonDB.save(
			if (file.isDirectory){
				File("$folderPath${File.separator}${graphViewModel.graph.label}.json")
			} else {
				file
			},
			graphViewModel
		)
	}

	companion object {
		@JvmStatic
		fun loadSettings(): SettingsModel {
			// TODO(valid load of settings from configuration)
			return SettingsModel()
		}
	}
}
