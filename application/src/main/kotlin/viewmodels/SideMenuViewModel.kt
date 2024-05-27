package viewmodels

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import models.SideMenuModel
import models.VertexID
import models.utils.AlgorithmButton
import models.utils.TabItem
import utils.PageType
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel

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
					graphPageViewModel.algorithms.forEach {
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
					graphPageViewModel.mapRepresentationModes.forEach {
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
fun TextButtonAlgorithm(
	graphViewModel: GraphViewModel?,
	algButton: AlgorithmButton,
	modifier: Modifier = Modifier
) {
	val coroutineScope = rememberCoroutineScope()
	val expanded = mutableStateOf(false)
	val dropContext = algButton.dropDownMenuContext
	TextButton(
		modifier = modifier,
		onClick = {
			if (dropContext != null) expanded.value = true
			else algButton.isRun.value = true
		},
	) {
		Text(algButton.label)
		if (dropContext != null) {
			Box {
				DropdownMenu(
					expanded = expanded.value,
					onDismissRequest = { expanded.value = false },
					content = {
						Column(
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							dropContext(algButton)
						}
					}
				)
			}
		}
	}
	if (graphViewModel != null && algButton.isRun.value) {
		expanded.value = false
		coroutineScope.launch {
			algButton.onRun(
				graphViewModel,
				algButton.inputs.value.stream().map {
					VertexID.vertexIDFromString(it, graphViewModel.vertexType)
				}.toList()
			)
			algButton.isRun.value = false
		}
	}
}

@Composable
fun TextButtonRepresentation(
	graphPageViewModel: GraphPageViewModel,
	entry: Map.Entry<String, (GraphPageViewModel) -> Unit>,
	modifier: Modifier
) {
	TextButton(
		onClick = { entry.value(graphPageViewModel) },
		modifier = modifier
	) {
		Text(entry.key)
	}
}
