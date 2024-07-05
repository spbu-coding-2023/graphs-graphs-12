package viewmodels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import models.SideMenuModel
import models.utils.AlgorithmButton
import models.utils.TabItem
import themes.JetTheme
import utils.PageType
import utils.TextButtonAlgorithm
import utils.TextButtonRepresentation
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
					Text(
						"Algorithms",
						style = JetTheme.typography.toolbar,
						color = JetTheme.colors.secondaryText
					) // todo(hide out of layout and add modifier)
					Column(
						modifier = Modifier
							.padding(4.dp)
							.clip(JetTheme.shapes.cornerStyle)
							.background(JetTheme.colors.tertiaryBackground)
					) {
						graphPageViewModel.algorithms.forEach { algorithmButton ->
							if (graphPageViewModel.graphViewModel?.graph?.isDirected == true) {
								if (!algorithmButton.nonDirectionalRequirement) {
									TextButtonAlgorithm(
										graphPageViewModel.graphViewModel, algorithmButton,
										Modifier.fillMaxWidth()
									)
								}
							} else {
								if (!algorithmButton.directionalRequirement) {
									TextButtonAlgorithm(
										graphPageViewModel.graphViewModel,
										algorithmButton,
										Modifier.fillMaxWidth()
									)
								}
							}
						}
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
					Text(
						"Representation",
						style = JetTheme.typography.toolbar,
						color = JetTheme.colors.secondaryText
					) // todo(hide out of layout and add modifier)
					Column(
						modifier = Modifier
							.padding(4.dp)
							.clip(JetTheme.shapes.cornerStyle)
							.background(JetTheme.colors.tertiaryBackground)
					) {
						graphPageViewModel.mapRepresentationModes.forEach {
							TextButtonRepresentation(graphPageViewModel, it, Modifier.fillMaxWidth())
							Divider(
								thickness = 0.5.dp,
								color = JetTheme.colors.primaryBackground
							)
						}
					}
				}
			),
		)
		sideMenuModel.addSeparator()
		sideMenuModel.addTabs(
			TabItem("Save", Icons.Filled.Save, Icons.Outlined.Save, isSelectablePage = false) {
				graphPageViewModel.save()
			}
		)
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
