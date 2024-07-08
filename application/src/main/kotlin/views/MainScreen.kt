package views

import JetSettings
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import themes.*
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
fun MainScreen(viewModel: MainScreenViewModel, jetSettings: JetSettings) {
	val statePager = rememberPagerState { viewModel.sideMenuViewModel.pagesCount }
	LaunchedEffect(viewModel.indexSelectedPage.value) {
		if (viewModel.indexSelectedPage.value != statePager.currentPage) {
			statePager.animateScrollToPage(viewModel.indexSelectedPage.value)
		}
	}

	MainTheme(
		style = jetSettings.currentStyle.value,
		darkTheme = jetSettings.isDarkMode.value,
		textSize = jetSettings.currentFontSize.value,
		corners = jetSettings.currentCornersStyle.value,
		fonts = jetSettings.currentFontFamily.value,
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
						viewModel.sideMenuViewModel.changeVisibility("Algorithms", true)
						viewModel.sideMenuViewModel.changeVisibility("Representation", true)
						viewModel.sideMenuViewModel.changeVisibility("Save", true)
						HomePage(viewModel.homePageViewModel)
					}

					viewModel.sideMenuViewModel.pageOfTab("GraphView") -> {
						viewModel.sideMenuViewModel.changeVisibility("GraphView", false)
						viewModel.sideMenuViewModel.changeVisibility("Algorithms", false)
						viewModel.sideMenuViewModel.changeVisibility("Representation", false)
						viewModel.sideMenuViewModel.changeVisibility("Save", false)
						GraphViewPage(viewModel.graphPageViewModel)
					}

					viewModel.sideMenuViewModel.pageOfTab("Settings") -> {
						viewModel.sideMenuViewModel.changeVisibility("Algorithms", true)
						viewModel.sideMenuViewModel.changeVisibility("Representation", true)
						viewModel.sideMenuViewModel.changeVisibility("Save", true)
						SettingsPage(jetSettings)
					}

					else -> HomePage(viewModel.homePageViewModel)
				}
			}
		}
	}
}
