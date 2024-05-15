package application.core.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import application.core.TabItem
import application.core.roundedCustom
import application.core.weightBottom
import application.core.whiteCustom
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun menu() {
	val listTabsFirst = listOf(
		TabItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
		TabItem("Layout", Icons.Filled.Grain, Icons.Outlined.Grain),
	)
	val tabSecond = TabItem("Algorithms", Icons.Filled.Menu, Icons.Outlined.Menu)
	val tabLast = TabItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)

	val statePager = rememberPagerState { listTabsFirst.size + 2 }
	val coroutineScope = rememberCoroutineScope()

	val indexSelectedTab = remember { mutableStateOf(0) }
	LaunchedEffect(indexSelectedTab.value) {
		if (indexSelectedTab.value != statePager.currentPage) {
			statePager.animateScrollToPage(indexSelectedTab.value)
		}
	}

	val isOpenedAlgorithms = remember { mutableStateOf(false) } // протащить в основной, чтобы после row появлась, мб даже box надо будет
	val isOpenedAlgorithmsW = remember { mutableStateOf(false) } // протащить в основной, чтобы после row появлась, мб даже box надо будет

	val scope = rememberCoroutineScope()

	Row {
		Column(
			modifier = Modifier
				.width(weightBottom)
				.fillMaxHeight()
		) {
			Column(
				modifier = Modifier
					.padding(4.dp)
					.clip(roundedCustom)
					.background(whiteCustom, roundedCustom),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			) {
				listTabsFirst.forEachIndexed { index, item ->
					Tab(
						selected = statePager.currentPage == index,
						onClick = {
							coroutineScope.launch {
								statePager.scrollToPage(index)
								indexSelectedTab.value = index
							}
						},
						icon = {
							Icon(
								imageVector = if (statePager.currentPage == index) item.iconSelected else item.iconUnselected,
								contentDescription = item.title
							)
						}
					)
				}
			}
			Column(
				modifier = Modifier
					.padding(4.dp)
					.clip(roundedCustom)
					.background(whiteCustom, roundedCustom),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			) {
				Icon(
					imageVector = if (statePager.currentPage == 2) tabSecond.iconSelected else tabSecond.iconUnselected,
					contentDescription = tabSecond.title,
					modifier = Modifier
						.fillMaxWidth()
						.height(50.dp) // why 50?
						.padding(4.dp)
//						.align(Alignment.Center)
						.pointerMoveFilter(
							onEnter = { isOpenedAlgorithms.value = true; false},
							onExit = {
								scope.launch {
//									delay(52)
									isOpenedAlgorithms.value = false;
								}
								false
							}
						)
				)

//				Tab(
//					selected = statePager.currentPage == 2,
//					onClick = {
//						coroutineScope.launch {
//							statePager.scrollToPage(2)
//							indexSelectedTab.value = 2
//						}
//					},
//					icon = {
//						Icon(
//							imageVector = if (statePager.currentPage == 2) Icons.Filled.Menu else Icons.Outlined.Menu,
//							contentDescription = "Menu"
//						)
//					}
//				)
			}
			Spacer(
				modifier = Modifier.weight(1f)
			)
			Column(
				modifier = Modifier
					.padding(4.dp)
					.clip(roundedCustom)
					.background(whiteCustom, roundedCustom),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			){
				Tab(
					selected = statePager.currentPage == 3,
					onClick = {
						coroutineScope.launch {
							statePager.scrollToPage(3)
							indexSelectedTab.value = 3
						}
					},
					icon = {
						Icon(
							imageVector = if (statePager.currentPage == 3) tabLast.iconSelected else tabLast.iconUnselected,
							contentDescription = tabLast.title
						)
					}
				)
			}
		}

		VerticalPager(
			state = statePager,
			userScrollEnabled = false
		) {index ->
			when (index) {
				0 -> showHome(indexSelectedTab)
				1 -> showLayout(isOpenedAlgorithms, isOpenedAlgorithmsW)
//				2 -> showAlgorithms()
				3 -> showSettings()
			}
		}
	}
}
