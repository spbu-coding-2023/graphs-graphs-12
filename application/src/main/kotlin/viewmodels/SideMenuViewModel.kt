package viewmodels

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import models.SideMenuModel
import models.VertexID
import models.utils.TabItem
import utils.PageType
import utils.representation.AllCenterPlacementStrategy
import utils.representation.CircularPlacementStrategy
import utils.representation.ForceDirectedPlacementStrategy
import utils.representation.RandomPlacementStrategy
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel
import views.pages.TextFieldItem

val mapAlgorithms: Map<String, (GraphViewModel) -> Unit> = mapOf(
	"Community detection" to { graphViewModel -> },
	"Identifying key vertices" to { graphViewModel -> },
	"Finding SCC" to { graphViewModel -> },
	"Finding cycles" to { graphViewModel -> },
	"Finding MST" to { graphViewModel -> },
	"Bridge-finding" to { graphViewModel -> },
	"Finding SP (Dijkstra)" to { graphViewModel ->
							   graphViewModel.parseDijkstraAlgorithm(
								   graphViewModel.graph.idVertices.first(),
								   graphViewModel.graph.idVertices.last()
							   )
//		var expanded by remember { mutableStateOf(false) }
//		val idVertexSource = remember { mutableStateOf("") }
//		val idVertexTarget = remember { mutableStateOf("") }
//
//		DropdownMenu(
//			expanded = expanded,
//			onDismissRequest = { expanded = false }
//		) {
//			TextFieldItem(idVertexSource, "Source vertex", Modifier)
//			TextFieldItem(idVertexTarget, "Target vertex", Modifier)
//		}
//
//		graphViewModel.parseDijkstraAlgorithm(
//			VertexID(idVertexSource, graphViewModel.vertexType),
//			VertexID(idVertexTarget, graphViewModel.vertexType))
							   },
	"Finding SP (Ford)" to { graphViewModel -> }
)

val mapRepresentationModes: Map<String, (GraphPageViewModel) -> Unit> = mapOf(
	"Central" to { graphPageViewModel -> graphPageViewModel.representationStrategy = AllCenterPlacementStrategy() },
	"Random" to { graphPageViewModel -> graphPageViewModel.representationStrategy = RandomPlacementStrategy() },
	"Circular" to { graphPageViewModel -> graphPageViewModel.representationStrategy = CircularPlacementStrategy() },
	// todo(add layout)
	"Force-directed" to {
		graphPageViewModel -> graphPageViewModel.representationStrategy = ForceDirectedPlacementStrategy(
		graphPageViewModel.graphViewModel
			?: throw ExceptionInInitializerError("An attempt to layout an uncreated graph."))
	}
)

class SideMenuViewModel(graphPageViewModel: GraphPageViewModel) {
	private val pagesOfTabsItems = mutableMapOf<String, Int>()
	val pagesCount: Int
		get() = pagesOfTabsItems.size
	private val sideMenuModel = SideMenuModel()
	val tabsItems: List<List<TabItem>?>
		get() = sideMenuModel.tabsItems

	init {
		sideMenuModel.addTabs(
			TabItem("Home", Icons.Filled.Home, Icons.Outlined.Home, isSelectablePage = true),
			TabItem(
				"GraphView",
				Icons.Filled.Laptop,
				Icons.Outlined.Laptop,
				mutableStateOf(true),
				isSelectablePage = true
			),
		)
		sideMenuModel.addTabs(
			TabItem(
				"Algorithms",
				Icons.Outlined.Analytics,
				Icons.Outlined.Analytics,
				mutableStateOf(true),
				isSelectablePage = false,
				dropDownMenuContext = { tabItem ->
					Text("Algorithms") // todo(hide out of layout and add modifier)
					mapAlgorithms.forEach {
						TextButtonAlgorithm(graphPageViewModel.graphViewModel, it, Modifier.fillMaxWidth())
					}
				}
			),
			TabItem(
				"Representation",
				Icons.Outlined.Grain,
				Icons.Outlined.Grain,
				mutableStateOf(true),
				isSelectablePage = false,
				dropDownMenuContext = { tabItem ->
					Text("Representation") // todo(hide out of layout and add modifier)
					mapRepresentationModes.forEach {
						TextButtonRepresentation(graphPageViewModel, it, Modifier.fillMaxWidth())
					}
				}
			),
		)
		sideMenuModel.addSeparator()
		sideMenuModel.addTabs(
			TabItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, isSelectablePage = true),
		)
	}

	init {
		pagesOfTabsItems["Home"] = PageType.HOME_PAGE.ordinal
		pagesOfTabsItems["GraphView"] = PageType.GRAPH_VIEW_PAGE.ordinal
		pagesOfTabsItems["Settings"] = PageType.SETTINGS_PAGE.ordinal
	}

	fun pageOfTab(tabName: String): Int {
		return pagesOfTabsItems.getOrDefault(tabName, 0)
	}

	fun changeVisibility(tabName: String, isHiddenState: Boolean) {
		sideMenuModel.changeTabVisibility(tabName, isHiddenState)
	}
}

@Composable
fun TextButtonAlgorithm(graphViewModel: GraphViewModel?, entry: Map.Entry<String, (GraphViewModel) -> Unit>, modifier: Modifier) {
	TextButton(
		onClick = { if (graphViewModel != null) entry.value(graphViewModel) },
		modifier = modifier
	) {
		Text(entry.key)
	}
}

@Composable
fun TextButtonRepresentation(graphPageViewModel: GraphPageViewModel, entry: Map.Entry<String, (GraphPageViewModel) -> Unit>, modifier: Modifier) {
	TextButton(
		onClick = { entry.value(graphPageViewModel) },
		modifier = modifier
	) {
		Text(entry.key)
	}
}
