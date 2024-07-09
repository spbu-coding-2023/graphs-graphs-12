package models

import JetSettings
import databases.GraphJSONDatabase
import databases.Neo4jRepository
import databases.SQLiteRepository
import findOrCreateFile
import models.utils.GraphInfo
import org.neo4j.driver.exceptions.Neo4jException
import org.neo4j.driver.exceptions.ServiceUnavailableException
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import utils.GraphSavingType
import utils.PageType
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.sql.SQLException

/**
 * The SettingsModel class is responsible for managing application settings,
 * connecting to databases, saving and loading graphs, and validating graph names.
 *
 * @property isNeo4jConnected indicates whether a Neo4J database is connected
 * @property graphNameRegEx a regular expression used to validate graph names
 */
class SettingsModel {
	private val jsonDB = GraphJSONDatabase()
	private val neo4jDB = Neo4jRepository()
	private val sqliteDB = SQLiteRepository()
	var isNeo4jConnected = false
	val graphNameRegEx = Regex("[a-zA-Z][a-zA-Z0-9_-]*")

	/**
	 * Connects to a Neo4J database using the provided URI, username, and password.
	 *
	 * @param uri the URI of the Neo4J database
	 * @param user the username for authentication
	 * @param password the password for authentication
	 */
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

	/**
	 * Saves the graph in the specified format and returns a [GraphInfo] object.
	 *
	 * @param graphPageViewModel the [GraphPageViewModel] containing the graph to be saved
	 * @param savingType the type of database to save the graph in
	 * @param folderPath the path of the folder where the graph will be saved
	 *
	 * @return a [GraphInfo] object containing information about the saved graph, or null if the graph couldn't be saved
	 */
	fun saveGraph(
		graphPageViewModel: GraphPageViewModel,
		savingType: GraphSavingType,
		folderPath: String
	): GraphInfo? {
		val graphViewModel = graphPageViewModel.graphViewModel ?: return null
		when (savingType) {
			GraphSavingType.LOCAL_FILE -> { saveGraphByJSON(graphViewModel, folderPath) }
			GraphSavingType.NEO4J_DB -> {
				if (!isNeo4jConnected) return null
				saveGraphByNeo4j(graphPageViewModel)
			}
			GraphSavingType.SQLITE_DB -> { saveGraphBySQLite(graphViewModel, folderPath) }
		}
		return GraphInfo(
			graphViewModel.graph.label,
			folderPath,
			savingType,
			onClick = { name, folder, saveType ->
				val path = File(folder, name).absolutePath
				when (saveType) {
					GraphSavingType.LOCAL_FILE -> {
						loadGraphFromJSON(graphPageViewModel, path)
						graphPageViewModel.dbType = saveType
						graphPageViewModel.dbPath = path
					}
					GraphSavingType.NEO4J_DB -> {
						loadGraphFromNEO4J(graphPageViewModel)
						graphPageViewModel.dbType = saveType
						graphPageViewModel.dbPath = ""
					}
					GraphSavingType.SQLITE_DB -> {
						loadGraphFromSQLite(graphPageViewModel, path)
						graphPageViewModel.dbType = saveType
						graphPageViewModel.dbPath = path
					}
				}
			}
		)
	}

	/**
	 * Loads a graph from a SQLite database file and updates the [GraphPageViewModel] accordingly.
	 *
	 * @param graphPageViewModel the [GraphPageViewModel] to update with the loaded graph
	 * @param path the path of the SQLite database file to load the graph from
	 *
	 * @return `true` if the graph was successfully loaded, `false` otherwise
	 */
	fun loadGraphFromSQLite(graphPageViewModel: GraphPageViewModel, path: String): Boolean {
		try {
			val graphViewModel = sqliteDB.loadGraph(path)
			graphPageViewModel.graphViewModel = graphViewModel
			graphPageViewModel.indexSelectedPage.value = PageType.GRAPH_VIEW_PAGE.ordinal
			graphPageViewModel.dbType = GraphSavingType.SQLITE_DB
			graphPageViewModel.dbPath = path
		} catch (ex: SQLException) {
			println("Load SQLite error: ${ex.message}")
			return false
		}
		return true
	}

	/**
	 * Saves the graph in a SQLite database file.
	 *
	 * @param graphViewModel the [GraphViewModel] containing the graph to be saved
	 * @param folderPath the path of the folder where the SQLite database file will be saved
	 */
	fun saveGraphBySQLite(graphViewModel: GraphViewModel, folderPath: String) {
		try {
			sqliteDB.writeDb(graphViewModel, folderPath)
		} catch (ex: SQLException) {
			println("Save SQLite error: ${ex.message}")
		}
	}

	/**
	 * Loads a graph from a Neo4J database and updates the [GraphPageViewModel] accordingly.
	 *
	 * @param graphPageViewModel the [GraphPageViewModel] to update with the loaded graph
	 *
	 * @return `true` if the graph was successfully loaded, `false` otherwise
	 */
	fun loadGraphFromNEO4J(graphPageViewModel: GraphPageViewModel): Boolean {
		if (!isNeo4jConnected) {
			println("Can't load graph because not found connection to neo4j DB")
			return false
		}
		try {
			neo4jDB.readData(graphPageViewModel)
			graphPageViewModel.indexSelectedPage.value = PageType.GRAPH_VIEW_PAGE.ordinal
			graphPageViewModel.dbType = GraphSavingType.NEO4J_DB
			graphPageViewModel.dbPath = ""
		// TODO(check handling of exceptions)
		} catch (ex: Neo4jException) {
			println("Load neo4j error: ${ex.message}")
			return false
		}
		return true
	}

	/**
	 * Loads a graph from a JSON file and updates the [GraphPageViewModel] accordingly.
	 *
	 * @param graphPageViewModel the [GraphPageViewModel] to update with the loaded graph
	 * @param path the path of the JSON file to load the graph from
	 *
	 * @return `true` if the graph was successfully loaded, `false` otherwise
	 */
	fun loadGraphFromJSON(graphPageViewModel: GraphPageViewModel, path: String): Boolean {
		try {
			val graphViewModel = jsonDB.load(File(path))
			graphPageViewModel.graphViewModel = graphViewModel
			graphPageViewModel.indexSelectedPage.value = PageType.GRAPH_VIEW_PAGE.ordinal
			graphPageViewModel.dbType = GraphSavingType.LOCAL_FILE
			graphPageViewModel.dbPath = path
		} catch (ex: IOException) {
			println("Load JSON error: ${ex.message}")
			return false
		}
		return true
	}

	/**
	 * Saves the graph in Neo4J database.
	 *
	 * @param graphPageViewModel the [GraphPageViewModel] containing the graph to be saved
	 */
	private fun saveGraphByNeo4j(graphPageViewModel: GraphPageViewModel) {
		if (!isNeo4jConnected) {
			println("Can't save graph because not found connection to neo4j DB")
			return
		}
		try {
			neo4jDB.writeData(graphPageViewModel)
		// TODO(check handling of exceptions)
		} catch (ex: Neo4jException) {
			println("Save neo4j error: ${ex.message}")
		}
	}

	/**
	 * Saves the graph in a JSON file.
	 *
	 * @param graphViewModel the [GraphViewModel] containing the graph to be saved
	 * @param folderPath the path of the folder where the JSON file will be saved
	 */
	private fun saveGraphByJSON(graphViewModel: GraphViewModel, folderPath: String) {
		val file = File(folderPath, "${graphViewModel.graph.label}.json")
		try {
			jsonDB.save(file, graphViewModel)
		} catch (ex: IOException) {
			println("Save JSON error: ${ex.message}")
		}
	}

	companion object {
		/**
		 * Loads the application settings from a configuration file.
		 *
		 * @param jetSettings an object that stores the current customization parameters
		 *
		 * @return a [SettingsModel] object containing the loaded settings
		 */
		@JvmStatic
		fun loadSettings(jetSettings: JetSettings): SettingsModel {
			val file = File("../settings")

			findOrCreateFile(file)
			loadSettings(file, jetSettings)

			return SettingsModel()
		}

		/**
		 * Reads the current customization parameters from [file] and writes them to [jetSettings].
		 *
		 * @param file storing current customization parameters
		 * @param jetSettings an object that stores the current customization parameters
		 */
		private fun loadSettings(file: File, jetSettings: JetSettings) {
			val converters = arrayOf<(String) -> Any>(
				{ string -> JetStyle.valueOf(string) },
				{ string -> JetSize.valueOf(string) },
				{ string -> JetCorners.valueOf(string) },
				{ string -> JetFontFamily.valueOf(string) }
			)

			file.readLines().withIndex().map { indexedValue -> indexedValue.index to indexedValue.value }
				.forEach { (index, setting) ->
					try {
						when (index) {
							0 -> jetSettings.currentStyle.value = converters[index](setting) as JetStyle
							1 -> jetSettings.currentFontSize.value = converters[index](setting) as JetSize
							2 -> jetSettings.currentCornersStyle.value = converters[index](setting) as JetCorners
							3 -> jetSettings.currentFontFamily.value = converters[index](setting) as JetFontFamily
							4 -> jetSettings.isDarkMode.value = setting.toBoolean()
						}
					} catch (e: IllegalArgumentException) {
						println("The element '$setting' in line $index does not match any element of the corresponding enumeration.")
					}
				}
		}
	}
}
