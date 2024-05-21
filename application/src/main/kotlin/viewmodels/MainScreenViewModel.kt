package viewmodels

import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import viewmodels.pages.GraphPageViewModel
import viewmodels.pages.HomePageViewModel
import viewmodels.pages.SettingsPageViewModel

class MainScreenViewModel(settings: SettingsModel) {
	val indexSelectedPage = mutableStateOf(0)
	val settingsPageViewModel = SettingsPageViewModel(settings)
	val graphPageViewModel = GraphPageViewModel()
	val sideMenuViewModel = SideMenuViewModel(graphPageViewModel)
	val homePageViewModel = HomePageViewModel(indexSelectedPage, settings, sideMenuViewModel, graphPageViewModel)



}
