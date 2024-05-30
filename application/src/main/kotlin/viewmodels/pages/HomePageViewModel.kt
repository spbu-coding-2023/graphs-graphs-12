package viewmodels.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
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
	}

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
		val graphViewModel = GraphViewModel(graph, vertexIDType, isUnweighted = !isGraphWeighted)
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
}
