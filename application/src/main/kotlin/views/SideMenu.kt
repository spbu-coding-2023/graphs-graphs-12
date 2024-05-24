package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import models.utils.TabItem
import themes.JetTheme
import viewmodels.SideMenuViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SideMenuTabColumn(tabsColumn: List<TabItem>, statePager: PagerState, indexSelectedPage: MutableState<Int>, viewModel: SideMenuViewModel) {
	val modifierColumns = Modifier
		.padding(4.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)

	val coroutineScope = rememberCoroutineScope()
	Column(
		modifierColumns,
		Arrangement.spacedBy(10.dp)
	) {
		tabsColumn.forEach { item ->
			if (item.isHide.value) return@forEach
			Row {
				var expanded by remember { mutableStateOf(false) }
				val itemPageIndex = viewModel.pageOfTab(item.title)
				val onItemClick = item.onClick()
				Tab(
					selected = statePager.currentPage == itemPageIndex,
					onClick = {
						if (onItemClick != null) onItemClick()
						else expanded = true
						if (item.isPageSelectable) {
							coroutineScope.launch {
								statePager.scrollToPage(itemPageIndex)
								indexSelectedPage.value = itemPageIndex
							}
						}
					},
					icon = {
						Icon(
							imageVector = if (statePager.currentPage == itemPageIndex) item.iconSelected else item.iconUnselected,
							contentDescription = item.title,
							tint = JetTheme.colors.tintColor
						)
					}
				)
				val dropDownMenuContext = item.dropDownMenuContext
				if (dropDownMenuContext != null) {
					Box {
						DropdownMenu(
							expanded = expanded,
							onDismissRequest = { expanded = false },
							content = { dropDownMenuContext(item) }
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SideMenu(statePager: PagerState, indexSelectedPage: MutableState<Int>, viewModel: SideMenuViewModel) {
	Column(Modifier.width(sizeBottom).fillMaxHeight()) {
		viewModel.tabsItems.forEach { tabsColumn ->
			if (tabsColumn == null) Spacer(Modifier.weight(1f))
			else SideMenuTabColumn(tabsColumn, statePager, indexSelectedPage, viewModel)
		}
	}
	// TODO(Choose version of side menu: `newVersion` or 'lastVersion')
//	newVersion(statePager, indexSelectedPage, viewModel)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun newVersion(statePager: PagerState, indexSelectedPage: MutableState<Int>, viewModel: SideMenuViewModel) {
	val modifierColumns = Modifier
		.padding(4.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)

	val coroutineScope = rememberCoroutineScope()
	NavigationRail(header = null) {
		viewModel.tabsItems.forEach { tabs ->
			if (tabs == null) Spacer(Modifier.weight(1f))
			else Column(
				modifierColumns,
				Arrangement.spacedBy(10.dp)
			) {
				tabs.forEach loop@ { tab ->
					if (tab.isHide.value) return@loop // DOC(https://kotlinlang.org/docs/returns.html#return-to-labels)
					val itemPageIndex = viewModel.pageOfTab(tab.title)
					val onItemClick = tab.onClick()
					NavigationRailItem(
						modifier = Modifier.size(52.dp, 52.dp),
						icon = {
							Icon(
								imageVector = if (statePager.currentPage == itemPageIndex) tab.iconSelected else tab.iconUnselected,
								contentDescription = tab.title,
								tint = JetTheme.colors.tintColor
							)
						},
						selected = statePager.currentPage == itemPageIndex,
						onClick = {
							if (onItemClick != null) onItemClick()
							if (onItemClick == null || tab.isPageSelectable) {
								coroutineScope.launch {
									statePager.scrollToPage(itemPageIndex)
									indexSelectedPage.value = itemPageIndex
								}
							}
						},
					)
				}
			}
		}
	}
}

