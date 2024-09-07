package viewmodels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Grain
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Laptop
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import models.SideMenuModel
import models.utils.TabItem
import mu.KotlinLogging
import themes.JetTheme
import utils.PageType
import utils.AlgorithmTextButton
import utils.ActionTextButton
import utils.SideMenuTabType
import viewmodels.pages.GraphPageViewModel

private val logger = KotlinLogging.logger("SideMenuViewModel")

/**
 * ViewModel for the side menu. Manages the tabs, their visibility, and the pages they correspond to.
 *
 * @param graphPageViewModel the ViewModel for the graph page
 * @property pagesCount the number of pages in the side menu
 * @property tabsItems the list of tabs in the side menu
 */
class SideMenuViewModel(graphPageViewModel: GraphPageViewModel) {
	private val pagesOfTabsItems = mutableMapOf<String, Int>()
	val pagesCount: Int
		get() = pagesOfTabsItems.size
	private val sideMenuModel = SideMenuModel()
	val tabsItems: List<List<TabItem>?>
		get() = sideMenuModel.tabsItems

	/**
	 * Initializes the side menu with tabs and their properties.
	 */
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
				dropDownMenuContext = { _ ->
					Text(
						"Algorithms",
						style = JetTheme.typography.toolbar,
						color = JetTheme.colors.secondaryText
					)
					Column(
						modifier = Modifier
							.padding(4.dp)
							.clip(JetTheme.shapes.cornerStyle)
							.background(JetTheme.colors.tertiaryBackground)
					) {
						graphPageViewModel.algorithms.forEach { algorithmButton ->
							if (graphPageViewModel.graphViewModel?.graph?.isDirected == true) {
								if (!algorithmButton.nonDirectionalRequirement) {
									AlgorithmTextButton(
										graphPageViewModel.graphViewModel,
										algorithmButton,
										Modifier.fillMaxWidth()
									)
								}
							} else {
								if (!algorithmButton.directionalRequirement) {
									AlgorithmTextButton(
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
				dropDownMenuContext = { _ ->
					Text(
						"Representation",
						style = JetTheme.typography.toolbar,
						color = JetTheme.colors.secondaryText
					)
					Column(
						modifier = Modifier
							.padding(4.dp)
							.clip(JetTheme.shapes.cornerStyle)
							.background(JetTheme.colors.tertiaryBackground)
					) {
						graphPageViewModel.mapRepresentationModes.forEach {
							ActionTextButton(graphPageViewModel, it, Modifier.fillMaxWidth())
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

	/**
	 * Initializes the map of tab names to their corresponding page indices.
	 * This map is used to determine which page to display when a tab is selected.
	 */
	init {
		pagesOfTabsItems["Home"] = PageType.HOME_PAGE.ordinal
		pagesOfTabsItems["GraphView"] = PageType.GRAPH_VIEW_PAGE.ordinal
		pagesOfTabsItems["Settings"] = PageType.SETTINGS_PAGE.ordinal
	}

	/**
	 * Returns the page index corresponding to the given tab name.
	 *
	 * @param tabName the name of the tab for which the page index is required
	 * @return the page index corresponding to the given tab name. If the tab name is not found, returns 0
	 */
	fun pageOfTab(tabName: String): Int {
		return pagesOfTabsItems.getOrDefault(tabName, 0)
	}

	/**
	 * Changes the visibility of a tab in the side menu.
	 *
	 * @param tabType the type of the tab whose visibility needs to be changed
	 * @param isHiddenState a boolean indicating whether the tab should be hidden or visible.
	 * 						If true, the tab will be hidden; if false, the tab will be visible
	 *
	 *  @see SideMenuModel.changeTabVisibility
	 */
	fun changeVisibility(tabType: SideMenuTabType, isHiddenState: Boolean) {
		logger.info { "Changing visibility of tab $tabType to ${if (isHiddenState) "hidden" else "visible"}" }
		sideMenuModel.changeTabVisibility(tabType, isHiddenState)
	}
}
