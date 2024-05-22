package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import themes.*
import views.pages.GraphViewPage
import views.pages.HomePage
import views.pages.SettingsPage
import viewmodels.MainScreenViewModel

// TODO(Move customize settings to other file)

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
	val isDarkModeValue = isSystemInDarkTheme()

	val currentStyle = remember { mutableStateOf(JetStyle.Black) }
	val currentFontSize = remember { mutableStateOf(JetSize.Medium) }
	val currentCornersStyle = remember { mutableStateOf(JetCorners.Rounded) }
	val currentFontFamily = remember { mutableStateOf(JetFontFamily.Default) }
	val isDarkMode = remember { mutableStateOf(isDarkModeValue) }

	MainTheme(
		style = currentStyle.value,
		darkTheme = isDarkMode.value,
		textSize = currentFontSize.value,
		corners = currentCornersStyle.value,
		fonts = currentFontFamily.value,
	) {
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

					viewModel.sideMenuViewModel.pageOfTab("Settings") -> SettingsPage(
						viewModel.settingsPageViewModel, isDarkMode,
						currentFontSize,
						currentStyle,
						currentCornersStyle,
						currentFontFamily
					)

					else -> HomePage(viewModel.homePageViewModel)
				}
			}
		}

	}
}
