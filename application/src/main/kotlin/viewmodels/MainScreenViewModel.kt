package viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import viewmodels.pages.GraphPageViewModel
import viewmodels.pages.HomePageViewModel

/**
 * MainScreenViewModel is the central point of interaction for the main screen of the application.
 * It manages the state of the selected page index, and provides view models for different screens.
 *
 * @param settings the settings model to be used by the view models
 * @property indexSelectedPage the index of the selected page
 * @property settingsPageViewModel the view model for the settings screen
 * @property graphPageViewModel the view model for the graph screen
 * @property sideMenuViewModel the view model for the side menu
 * @property homePageViewModel the view model for the home screen
 */
class MainScreenViewModel(settings: SettingsModel) {
	val indexSelectedPage = mutableIntStateOf(0)
	val graphPageViewModel = GraphPageViewModel(settings, indexSelectedPage)
	val sideMenuViewModel = SideMenuViewModel(graphPageViewModel)
	val homePageViewModel = HomePageViewModel(
		graphPageViewModel,
		indexSelectedPage,
		settings,
		sideMenuViewModel,
		graphPageViewModel
	)
}
