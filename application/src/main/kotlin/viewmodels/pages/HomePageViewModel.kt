package viewmodels.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import findOrCreateFile
import graphs_lab.core.graphs.WeightedGraph
import models.SettingsModel
import models.utils.GraphInfo
import models.utils.ListWidgetItem
import models.VertexID
import models.WeightedUnweightedGraph
import utils.GraphSavingType
import utils.VertexIDType
import viewmodels.SideMenuViewModel
import viewmodels.graphs.GraphViewModel
import java.io.File

/**
 * ViewModel for the Home Page.
 * Manages tasks related to creating and loading graphs.
 *
 * @property graphPageViewModel a ViewModel for the Graph Page
 * @property indexSelectedPage a MutableState to keep track of the selected page index
 * @property settings a SettingsModel instance for saving and loading graphs
 * @property sideMenu a SideMenuViewModel instance for managing the side menu
 * @property graphPage a GraphPageViewModel instance for the Graph Page
 * @property tasks a List of tasks to be displayed in the side menu
 * @property previouslyLoadedGraph a set of previously loaded graphs
 * @property isOpenDialogOfCreatingNewGraph indicate keep track of whether the dialog of creating a new graph is open
 * @property isOpenDialogOfLoadingNewGraph indicate keep track of whether the dialog of loading a new graph is open
 */
class HomePageViewModel(
	val graphPageViewModel: GraphPageViewModel,
	val indexSelectedPage: MutableState<Int>,
	val settings: SettingsModel,
	val sideMenu: SideMenuViewModel,
	val graphPage: GraphPageViewModel
) {
	private val _tasks = mutableListOf<ListWidgetItem>()
	val tasks: List<ListWidgetItem>
		get() = _tasks.toList()

	private val _previouslyLoadedGraph = mutableSetOf<GraphInfo>()
	val previouslyLoadedGraph: List<GraphInfo>
		get() = _previouslyLoadedGraph.sorted()
	private val _isOpenDialogOfCreatingNewGraph = mutableStateOf(false)
	var isOpenDialogOfCreatingNewGraph: Boolean
		get() = _isOpenDialogOfCreatingNewGraph.value
		set(value) {
			_isOpenDialogOfCreatingNewGraph.value = value
		}
	private val _isOpenDialogOfLoadingNewGraph = mutableStateOf(false)
	var isOpenDialogOfLoadingNewGraph: Boolean
		get() = _isOpenDialogOfLoadingNewGraph.value
		set(value) {
			_isOpenDialogOfLoadingNewGraph.value = value
		}

	/**
	 * Initializes the tasks list with two ListWidgetItem instances and loads the history of previously loaded graphs.
	 * The first item represents the "Create Graph" task, which opens a dialog to create a new graph.
	 * The second item represents the "Load graph" task, which opens a dialog to load an existing graph.
	 */
	init {
		_tasks.add(
			ListWidgetItem(
				mainText = "Create Graph",
				icon = Icons.Default.Add,
				alignment = Alignment.Center
			) { isOpenDialogOfCreatingNewGraph = true }
		)
		_tasks.add(
			ListWidgetItem(
				mainText = "Load graph",
				icon = Icons.Default.Download,
				alignment = Alignment.Center,
			) { isOpenDialogOfLoadingNewGraph = true }
		)

		loadHistory()
	}

	/**
	 * Function to create a new graph based on the provided parameters.
	 *
	 * @param graphName the name of the graph
	 * @param vertexIDType the type of vertex IDs (e.g., [String], [Int])
	 * @param isGraphDirected indicates whether the graph is directed or undirected
	 * @param isGraphWeighted indicates whether the graph is weighted or unweighted
	 * @param savingType the type of graph saving
	 * @param saveFolder the folder where the graph will be saved
	 */
	fun createGraph(
		graphName: String,
		vertexIDType: VertexIDType,
		isGraphDirected: Boolean,
		isGraphWeighted: Boolean,
		savingType: GraphSavingType,
		saveFolder: String
	) {
		val graph = if (isGraphWeighted) {
			WeightedGraph<VertexID>(graphName, isGraphDirected, isAutoAddVertex = true)
		} else {
			WeightedUnweightedGraph<VertexID>(graphName, isGraphDirected, isAutoAddVertex = true)
		}
		val graphViewModel = GraphViewModel(graph, vertexIDType, isUnweighted = !isGraphWeighted, settings)
		graphPage.graphViewModel = graphViewModel
		val saveResult = settings.saveGraph(graphPage, savingType, saveFolder)
		if (saveResult != null) {
			indexSelectedPage.value = sideMenu.pageOfTab("GraphView")
			_previouslyLoadedGraph.add(saveResult)
			graphPageViewModel.dbPath = File(saveFolder, graphName).absolutePath
			graphPageViewModel.dbType = savingType
		} else {
			graphPage.graphViewModel = null
		}
	}

	/**
	 * Function to load graph information from a specified path and saving type.
	 * This function adds the loaded graph information to the previously loaded graphs list.
	 *
	 * @param savingType the type of graph saving
	 * @param path the path where the graph is located
	 */
	fun loadGraphInfo(savingType: GraphSavingType, path: String) {
		val graphView = graphPage.graphViewModel ?: return
		val file = File(path)
		_previouslyLoadedGraph.add(
			GraphInfo(
				graphView.graph.label,
				if (path == "") "" else file.parent,
				savingType,
				onClick = { name, folder, saveType ->
					when (saveType) {
						GraphSavingType.LOCAL_FILE -> {
							settings.loadGraphFromJSON(graphPageViewModel, File(folder, name).absolutePath)
							graphPageViewModel.dbType = saveType
							graphPageViewModel.dbPath = File(folder, name).absolutePath
						}

						GraphSavingType.NEO4J_DB -> {
							settings.loadGraphFromNEO4J(graphPageViewModel)
							graphPageViewModel.dbType = saveType
							graphPageViewModel.dbPath = ""
						}

						GraphSavingType.SQLITE_DB -> {
							settings.loadGraphFromSQLite(graphPageViewModel, path)
							graphPageViewModel.dbType = saveType
							graphPageViewModel.dbPath = path
						}
					}
				}
			)
		)
	}

	/**
	 * Function to load graph information from a string.
	 * This function adds information from the string to the previously loaded graphs list.
	 *
	 * @param string containing information about the graph (name, savingType, folder)
	 */
	private fun loadGraphInfoFromString(string: String) {
		val regex = Regex("""(\w+)\s*=\s*([^,)]+)""")
		val matches = regex.findAll(string)

		val resultMap = mutableMapOf<String, String>()

		for (matchResult in matches) {
			val key = matchResult.groupValues[1]
			val value = matchResult.groupValues[2].trim()

			resultMap[key] = value
		}

		val nameValue = resultMap["name"] ?: return
		val folderValue = resultMap["folder"] ?: return
		val savingType: GraphSavingType = when (resultMap["savingType"]) {
			"Local file" -> GraphSavingType.LOCAL_FILE
			"SQLite DB" -> GraphSavingType.SQLITE_DB
			else -> return
		}

		_previouslyLoadedGraph.add(
			GraphInfo(
				nameValue,
				folderValue,
				savingType,
				onClick = { name, folder, saveType ->
					when (saveType) {
						GraphSavingType.LOCAL_FILE -> {
							settings.loadGraphFromJSON(graphPageViewModel, File(folder, name).absolutePath)
							graphPageViewModel.dbType = saveType
							graphPageViewModel.dbPath = File(folder, name).absolutePath
						}

						GraphSavingType.NEO4J_DB -> {}

						GraphSavingType.SQLITE_DB -> {
							settings.loadGraphFromSQLite(graphPageViewModel, File(folder, name).absolutePath)
							graphPageViewModel.dbType = saveType
							graphPageViewModel.dbPath = File(folder, name).absolutePath
						}
					}
				}
			)
		)
	}

	private fun loadHistory() {
		val file = File(settings.applicationContextDirectory, "/.history")
		findOrCreateFile(file)
		file.readLines().forEach {
			loadGraphInfoFromString(it)
		}
	}
}
