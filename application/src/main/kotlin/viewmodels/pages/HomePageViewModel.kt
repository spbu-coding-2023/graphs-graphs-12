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
//		graph.addEdge(VertexID(0, VertexIDType.INT_TYPE), VertexID(7, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(6, VertexIDType.INT_TYPE), VertexID(0, VertexIDType.INT_TYPE), 10.0)
//		graph.addEdge(VertexID(7, VertexIDType.INT_TYPE), VertexID(6, VertexIDType.INT_TYPE), 100.0)
//		graph.addEdge(VertexID(7, VertexIDType.INT_TYPE), VertexID(1, VertexIDType.INT_TYPE), 50.0)
//		graph.addEdge(VertexID(6, VertexIDType.INT_TYPE), VertexID(1, VertexIDType.INT_TYPE), -11.0)
//		graph.addEdge(VertexID(6, VertexIDType.INT_TYPE), VertexID(4, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(1, VertexIDType.INT_TYPE), VertexID(9, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(9, VertexIDType.INT_TYPE), VertexID(4, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(4, VertexIDType.INT_TYPE), VertexID(2, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(2, VertexIDType.INT_TYPE), VertexID(9, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(11, VertexIDType.INT_TYPE), VertexID(9, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(11, VertexIDType.INT_TYPE), VertexID(2, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(11, VertexIDType.INT_TYPE), VertexID(5, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(2, VertexIDType.INT_TYPE), VertexID(10, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(5, VertexIDType.INT_TYPE), VertexID(10, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(10, VertexIDType.INT_TYPE), VertexID(8, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(5, VertexIDType.INT_TYPE), VertexID(3, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(8, VertexIDType.INT_TYPE), VertexID(5, VertexIDType.INT_TYPE), 1.0)
//		graph.addEdge(VertexID(3, VertexIDType.INT_TYPE), VertexID(8, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(1, VertexIDType.INT_TYPE), VertexID(2, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(1, VertexIDType.INT_TYPE), VertexID(3, VertexIDType.INT_TYPE), 10.0)
		graph.addEdge(VertexID(1, VertexIDType.INT_TYPE), VertexID(4, VertexIDType.INT_TYPE), 100.0)
		graph.addEdge(VertexID(1, VertexIDType.INT_TYPE), VertexID(5, VertexIDType.INT_TYPE), 50.0)
		graph.addEdge(VertexID(2, VertexIDType.INT_TYPE), VertexID(6, VertexIDType.INT_TYPE), -11.0)
		graph.addEdge(VertexID(2, VertexIDType.INT_TYPE), VertexID(7, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(2, VertexIDType.INT_TYPE), VertexID(8, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(2, VertexIDType.INT_TYPE), VertexID(9, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(3, VertexIDType.INT_TYPE), VertexID(8, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(3, VertexIDType.INT_TYPE), VertexID(10, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(3, VertexIDType.INT_TYPE), VertexID(11, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(6, VertexIDType.INT_TYPE), VertexID(10, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(6, VertexIDType.INT_TYPE), VertexID(11, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(6, VertexIDType.INT_TYPE), VertexID(12, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(13, VertexIDType.INT_TYPE), VertexID(14, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(13, VertexIDType.INT_TYPE), VertexID(15, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(13, VertexIDType.INT_TYPE), VertexID(16, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(14, VertexIDType.INT_TYPE), VertexID(17, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(14, VertexIDType.INT_TYPE), VertexID(18, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(17, VertexIDType.INT_TYPE), VertexID(19, VertexIDType.INT_TYPE), 1.0)
		graph.addEdge(VertexID(19, VertexIDType.INT_TYPE), VertexID(20, VertexIDType.INT_TYPE), 1.0)
//		for (i in 0..10) {
//			for (j in 0..10) {
//				if (i == j) continue
//				graph.addEdge(VertexID(i, VertexIDType.INT_TYPE), VertexID(j, VertexIDType.INT_TYPE), 1.0)
//			}
//		}
		graphPage.graphViewModel = GraphViewModel(graph, vertexIDType, isUnweighted = !isGraphWeighted)
		indexSelectedPage.value = sideMenu.pageOfTab("GraphView")
		return graph
	}
}
