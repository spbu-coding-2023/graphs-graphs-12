package views

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import models.JetSettings
import themes.MainTheme
import utils.SideMenuTabType
import views.pages.GraphViewPage
import views.pages.HomePage
import views.pages.SettingsPage
import viewmodels.MainScreenViewModel

/**
 * MainScreen is the main composable function that represents the main screen of the application.
 * It takes a [viewModel] parameter which is an instance of [MainScreenViewModel] and [jetSettings] which is a object
 * of data class [JetSettings].
 *
 * @param viewModel the view model for the main screen
 * @param jetSettings a settings object that defines the visual appearance and style settings for application
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
						SettingsPage(jetSettings)
					}
					else -> { HomePage(viewModel.homePageViewModel) }
				}
			}
		}
	}
}
