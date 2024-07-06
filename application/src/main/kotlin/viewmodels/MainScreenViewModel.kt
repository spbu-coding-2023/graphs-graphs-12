package viewmodels

import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import viewmodels.pages.GraphPageViewModel
import viewmodels.pages.HomePageViewModel
import viewmodels.pages.SettingsPageViewModel

/**
 * MainScreenViewModel is the central point of interaction for the main screen of the application.
 * It manages the state of the selected page index, and provides view models for different screens.
 *
 * @param settings the settings model to be used by the view models
 * @property indexSelectedPage the index of the selected page
 * @property settingsPageViewModel the view model for the settings screen
 */
class MainScreenViewModel(settings: SettingsModel) {
	val indexSelectedPage = mutableStateOf(0) // TODO(change to mutableIntState)
	val settingsPageViewModel = SettingsPageViewModel(settings)
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
