package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import themes.MainTheme
import utils.SideMenuTabType
import views.pages.GraphViewPage
import views.pages.HomePage
import views.pages.SettingsPage
import viewmodels.MainScreenViewModel

/**
 * MainScreen is the main composable function that represents the main screen of the application.
 * It takes a [viewModel] parameter which is an instance of [MainScreenViewModel].
 *
 * @param viewModel the view model for the main screen
 */
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
		Row(Modifier.fillMaxSize()) { // todo(add padding and delete other places)
			SideMenu(statePager, viewModel.indexSelectedPage, viewModel.sideMenuViewModel)
			VerticalPager(
				modifier = Modifier.fillMaxSize(),
				state = statePager,
				userScrollEnabled = false
			) { pageIndex ->
				when (pageIndex) {
					viewModel.sideMenuViewModel.pageOfTab("Home") -> {
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.ALGORITHMS, true)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.REPRESENTATION, true)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.SAVE, true)
						HomePage(viewModel.homePageViewModel)
					}
					viewModel.sideMenuViewModel.pageOfTab("GraphView") -> {
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.GRAPH_VIEW, false)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.ALGORITHMS, false)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.REPRESENTATION, false)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.SAVE, false)
						GraphViewPage(viewModel.graphPageViewModel)
					}
					viewModel.sideMenuViewModel.pageOfTab("Settings") -> {
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.ALGORITHMS, true)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.REPRESENTATION, true)
						viewModel.sideMenuViewModel.changeVisibility(SideMenuTabType.SAVE, true)
						SettingsPage(
							isDarkMode,
							currentFontSize,
							currentStyle,
							currentCornersStyle,
							currentFontFamily
						)
					}
					else -> { HomePage(viewModel.homePageViewModel) }
				}
			}
		}
	}
}
