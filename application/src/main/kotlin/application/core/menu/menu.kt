package application.core.menu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import application.core.TabItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun menu() {
	val listTabs = listOf(
		TabItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
		TabItem("Layout", Icons.Filled.Grain, Icons.Outlined.Grain),
//		TabItem("Algorithms", Icons.Filled.Menu, Icons.Outlined.Menu),
//		TabItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings)
	)

	val statePager = rememberPagerState { listTabs.size + 2 }
	val coroutineScope = rememberCoroutineScope()

	val indexSelectedTab = remember { mutableStateOf(0) }
	LaunchedEffect(indexSelectedTab.value) {
		if (indexSelectedTab.value != statePager.currentPage) {
			statePager.animateScrollToPage(indexSelectedTab.value)
		}
	}

	Row {
		Column(
			modifier = Modifier
				.width(50.dp)
				.fillMaxHeight()
		) {
			Column(
				modifier = Modifier
					.padding(4.dp)
					.clip(RoundedCornerShape(14.dp))
					.background(Color(254, 249, 231), RoundedCornerShape(14.dp)),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			) {
				listTabs.forEachIndexed { index, item ->
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
					.clip(RoundedCornerShape(14.dp))
					.background(Color(254, 249, 231), RoundedCornerShape(14.dp)),
				verticalArrangement = Arrangement.spacedBy(10.dp)
			) {
				Tab(
					selected = statePager.currentPage == 2,
					onClick = {
						coroutineScope.launch {
							statePager.scrollToPage(2)
							indexSelectedTab.value = 2
						}
					},
					icon = {
						Icon(
							imageVector = if (statePager.currentPage == 2) Icons.Filled.Menu else Icons.Outlined.Menu,
							contentDescription = "Menu"
						)
					}
				)
			}
			Spacer(
				modifier = Modifier.weight(1f)
			)
			Column(
				modifier = Modifier
					.padding(4.dp)
					.clip(RoundedCornerShape(14.dp))
					.background(Color(254, 249, 231), RoundedCornerShape(14.dp)),
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
							imageVector = if (statePager.currentPage == 3) Icons.Filled.Settings else Icons.Outlined.Settings,
							contentDescription = "Settings"
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
				1 -> showLayout()
				2 -> showAlgorithms()
				3 -> showSettings()
			}
		}
	}
}
