package viewmodels

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import models.SideMenuModel
import models.utils.TabItem
import utils.PageType
import utils.representation.AllCenterPlacementStrategy
import utils.representation.CircularPlacementStrategy
import utils.representation.RandomPlacementStrategy
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
			TabItem("Home", Icons.Filled.Home, Icons.Outlined.Home, isPageSelectable = true),
			TabItem(
				"GraphView",
				Icons.Filled.Laptop,
				Icons.Outlined.Laptop,
				mutableStateOf(true),
				isPageSelectable = true
			),
		)
		sideMenuModel.addTabs(
			TabItem(
				"Algorithms",
				Icons.Filled.GraphicEq,
				Icons.Outlined.GraphicEq,
				mutableStateOf(true),
				isPageSelectable = false,
				dropDownMenuContext = { tabItem ->
					Text("Algorithms")
				}
			),
			TabItem(
				"Representation",
				Icons.Filled.FilePresent,
				Icons.Outlined.FilePresent,
				mutableStateOf(true),
				isPageSelectable = false,
				dropDownMenuContext = { tabItem ->
					Text("Representations modes")
					TextButton(
						onClick = { graphPageViewModel.representationStrategy = AllCenterPlacementStrategy() },
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Center")
					}
					TextButton(
						onClick = { graphPageViewModel.representationStrategy = RandomPlacementStrategy() },
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Randomize")
					}
					TextButton(
						onClick = { graphPageViewModel.representationStrategy = CircularPlacementStrategy() },
						modifier = Modifier.fillMaxWidth()
					) {
						Text("Circular")
					}
				}
			),
		)
		sideMenuModel.addSeparator()
		sideMenuModel.addTabs(
			TabItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, isPageSelectable = true),
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
