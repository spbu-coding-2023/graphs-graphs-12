package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import views.pages.GraphViewPage
import views.pages.HomePage
import views.pages.SettingsPage
import viewmodels.MainScreenViewModel

// TODO(Move customize settings to other file)
val whiteCustom = Color(254, 249, 231)
val grayCustom = Color(217, 217, 217)
val greenCustom = Color(218, 228, 205)
val blueCustom = Color(175, 218, 252)
val roundedCustom = RoundedCornerShape(14.dp)

val weightBottom = 50.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
	val statePager = rememberPagerState { viewModel.sideMenuViewModel.pagesCount }
	LaunchedEffect(viewModel.indexSelectedPage.value) {
		if (viewModel.indexSelectedPage.value != statePager.currentPage) {
			statePager.animateScrollToPage(viewModel.indexSelectedPage.value)
		}
	}

	Row {
		SideMenu(statePager, viewModel.indexSelectedPage, viewModel.sideMenuViewModel)
		VerticalPager(
			state = statePager,
			userScrollEnabled = false
		) { pageIndex ->
			when (pageIndex) {
				viewModel.sideMenuViewModel.pageOfTab("Home") -> HomePage(viewModel.homePageViewModel)
				viewModel.sideMenuViewModel.pageOfTab("GraphView") -> {
					viewModel.sideMenuViewModel.changeVisibility("GraphView", false)
					viewModel.sideMenuViewModel.changeVisibility("Algorithms", false)
					viewModel.sideMenuViewModel.changeVisibility("Representation", false)
					viewModel.sideMenuViewModel.changeVisibility("Test", false)
					GraphViewPage(viewModel.graphPageViewModel)
				}
				viewModel.sideMenuViewModel.pageOfTab("Settings") -> SettingsPage(viewModel.settingsPageViewModel)
				else -> HomePage(viewModel.homePageViewModel)
			}
		}

	}
}
