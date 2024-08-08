package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import models.utils.TabItem
import themes.JetTheme
import themes.sizeBottom
import viewmodels.SideMenuViewModel

/**
 * This function is responsible for rendering the side menu of the application.
 *
 * @param statePager the state of the pager component that manages the pages
 * @param indexSelectedPage the mutable state that holds the index of the currently selected page
 * @param viewModel the view model that provides data and functionality for the side menu
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SideMenu(statePager: PagerState, indexSelectedPage: MutableState<Int>, viewModel: SideMenuViewModel) {
	Column(Modifier.width(sizeBottom).fillMaxHeight()) {
		viewModel.tabsItems.forEach { tabsColumn ->
			if (tabsColumn == null) {
				Spacer(Modifier.weight(1f))
			} else {
				SideMenuTabColumn(tabsColumn, statePager, indexSelectedPage, viewModel)
			}
		}
	}
}

/**
 * This function is responsible for rendering a column of tabs within the side menu.
 *
 * @param tabsColumn a list of [TabItem] objects representing the tabs to be displayed in the column
 * @param statePager the [PagerState] object that keeps track of the current page index in the parent composable
 * @param indexSelectedPage a [MutableState] object that holds the index of the currently selected page
 * @param viewModel the [SideMenuViewModel] object that provides data and functionality for the side menu
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SideMenuTabColumn(
	tabsColumn: List<TabItem>,
	statePager: PagerState,
	indexSelectedPage: MutableState<Int>,
	viewModel: SideMenuViewModel
) {
	val modifierColumns = Modifier
		.padding(4.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)

	val coroutineScope = rememberCoroutineScope()
	Column(
		modifier = modifierColumns,
		verticalArrangement = Arrangement.spacedBy(10.dp)
	) {
		tabsColumn.forEach { item ->
			if (item.isHidden.value) return@forEach
			Row {
				var expanded by remember { mutableStateOf(false) }
				val indexItemPage = viewModel.pageOfTab(item.title)
				val onItemClick = item.onClick()

				Tab(
					selected = statePager.currentPage == indexItemPage,
					onClick = {
						coroutineScope.launch {
							if (onItemClick != null) { onItemClick() } else { expanded = true }
							if (item.isSelectablePage) {
								indexSelectedPage.value = indexItemPage
							}
						}
					},
					icon = {
						Icon(
							imageVector = if (statePager.currentPage == indexItemPage) item.iconSelected else item.iconUnselected,
							contentDescription = item.title,
							tint = JetTheme.colors.tintColor
						)
					}
				)
				val dropDownMenuContext = item.dropDownMenuContext
				if (dropDownMenuContext != null && indexSelectedPage.value == 1) {
					Box(
						Modifier.padding(12.dp)
					) {
						DropdownMenu(
							modifier = Modifier.background(JetTheme.colors.primaryBackground).padding(4.dp),
							expanded = expanded,
							onDismissRequest = { expanded = false },
							content = {
								Column(horizontalAlignment = Alignment.CenterHorizontally) {
									dropDownMenuContext(item)
								}
							}
						)
					}
				}
			}
		}
	}
}
