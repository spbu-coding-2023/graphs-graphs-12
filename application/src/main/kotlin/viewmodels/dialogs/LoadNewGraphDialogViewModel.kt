package viewmodels.dialogs

import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import utils.GraphSavingType
import viewmodels.pages.HomePageViewModel

/**
 * ViewModel for the Load New Graph Dialog.
 *
 * @property homePageViewModel the ViewModel of the Home Page
 * @property neo4jHost a MutableState of Neo4j host URL
 * @property neo4jUserName a MutableState of Neo4j username
 * @property neo4jPassword a MutableState of Neo4j password
 * @property isCrateNeo4jConnection a MutableState of whether to create a Neo4j connection
 * @property selectedLoadType a MutableState of the selected graph load type
 * @property loadFile a MutableState of the file path to load the graph from
 * @property settings the settings of the application
 */
class LoadNewGraphDialogViewModel(val homePageViewModel: HomePageViewModel) {
	var neo4jHost = mutableStateOf("")
	var neo4jUserName = mutableStateOf("")
	var neo4jPassword = mutableStateOf("")
	var isCrateNeo4jConnection = mutableStateOf(false)
	val selectedLoadType = mutableStateOf(GraphSavingType.LOCAL_FILE)
	val loadFile = mutableStateOf("")
	val settings: SettingsModel = homePageViewModel.settings
}
