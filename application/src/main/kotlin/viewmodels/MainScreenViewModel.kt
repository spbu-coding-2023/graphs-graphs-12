package viewmodels

import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import viewmodels.pages.GraphPageViewModel
import viewmodels.pages.HomePageViewModel
import viewmodels.pages.SettingsPageViewModel

class MainScreenViewModel(settings: SettingsModel) {
	val indexSelectedPage = mutableStateOf(0) // todo(change to mutableIntState)
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
