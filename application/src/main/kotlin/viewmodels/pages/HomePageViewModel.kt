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
import utils.VertexIDType
import viewmodels.SideMenuViewModel
import viewmodels.dialogs.CreateNewGraphDialogViewModel
import viewmodels.graphs.GraphViewModel

class HomePageViewModel(
	val indexSelectedPage: MutableState<Int>,
	val settings: SettingsModel,
	val sideMenu: SideMenuViewModel,
	val graphPage: GraphPageViewModel
) {
	private val _tasks = mutableListOf<ListWidgetItem>()
	val tasks: List<ListWidgetItem>
		get() = _tasks.toList()

	private val _previouslyLoadedGraph = sortedSetOf<GraphInfo>()
	val previouslyLoadedGraph: List<GraphInfo>
		get() = _previouslyLoadedGraph.sorted()
	private val _isOpenDialogOfCreatingNewGraph = mutableStateOf(false)
	var isOpenDialogOfCreatingNewGraph: Boolean
		get() = _isOpenDialogOfCreatingNewGraph.value
		set(value) {
			_isOpenDialogOfCreatingNewGraph.value = value
		}

	init {
		_tasks.add(
			ListWidgetItem(
				mainText = "Create new graph",
				icon = Icons.Default.Add,
				alignment = Alignment.Center
			) { isOpenDialogOfCreatingNewGraph = true }
		)
		_tasks.add(
			ListWidgetItem(
				mainText = "Load new graph",
				icon = Icons.Default.Download,
				alignment = Alignment.Center,
			) {
				println("load")
			}
		)
	}

	fun createGraph(
		graphName: String,
		vertexIDType: VertexIDType,
		isGraphDirected: Boolean,
		isGraphWeighted: Boolean
	): WeightedGraph<VertexID> {
		val graph = if (isGraphWeighted) {
			WeightedGraph<VertexID>(graphName, isGraphDirected, isAutoAddVertex = true)
		} else {
			WeightedUnweightedGraph<VertexID>(graphName, isGraphDirected, isAutoAddVertex = true)
		}
		graphPage.graphViewModel = GraphViewModel(graph, vertexIDType, isUnweighted = false)
		indexSelectedPage.value = sideMenu.pageOfTab("GraphView")
		return graph
	}
}
