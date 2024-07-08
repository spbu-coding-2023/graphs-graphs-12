package viewmodels.dialogs

import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import utils.GraphSavingType
import utils.VertexIDType
import viewmodels.pages.HomePageViewModel

/**
 * ViewModel for the Create New Graph Dialog.
 *
 * @property homePageViewModel the ViewModel of the Home Page
 * @property graphName the name of the new graph
 * @property selectedVertexTypeID the type of the vertices in the new graph
 * @property isGraphWeighted whether the new graph is weighted
 * @property isGraphDirected whether the new graph is directed
 * @property selectedSaveType the type of saving the new graph
 * @property saveFolder the folder where the new graph will be saved
 * @property settings the settings of the application
 */
class CreateNewGraphDialogViewModel(val homePageViewModel: HomePageViewModel) {
	val graphName = mutableStateOf("")
	val selectedVertexTypeID = mutableStateOf(VertexIDType.INT_TYPE)
	val isGraphWeighted = mutableStateOf(false)
	val isGraphDirected = mutableStateOf(false)
	val selectedSaveType = mutableStateOf(GraphSavingType.LOCAL_FILE)
	val saveFolder = mutableStateOf(System.getProperty("user.home"))
	val settings: SettingsModel = homePageViewModel.settings

	/**
	 * Checks if the given graph name is valid according to the application's settings.
	 *
	 * @param newGraphName the name of the new graph to be checked
	 * @return `true` if the graph name is valid, `false` otherwise
	 *
	 * @see SettingsModel.graphNameRegEx for the regular expression used to validate the graph name
	 */
	fun isValidGraphName(newGraphName: String): Boolean {
		return settings.graphNameRegEx.matches(newGraphName)
	}
}
