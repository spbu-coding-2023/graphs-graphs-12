package viewmodels.dialogs

import androidx.compose.runtime.mutableStateOf
import utils.GraphSavingType
import viewmodels.pages.HomePageViewModel

class LoadNewGraphDialogViewModel(val homePageViewModel: HomePageViewModel) {
	var neo4jHost =  mutableStateOf("")
	var neo4jUserName =  mutableStateOf("")
	var neo4jPassword =  mutableStateOf("")
	var isCrateNeo4jConnection =  mutableStateOf(false)
	val selectedLoadType = mutableStateOf(GraphSavingType.LOCAL_FILE)
	val loadFile = mutableStateOf("")
	val settings = homePageViewModel.settings
}
