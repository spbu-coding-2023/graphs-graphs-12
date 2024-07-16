package viewmodels.dialogs

import androidx.compose.runtime.mutableStateOf
import models.SettingsModel
import utils.GraphSavingType
import utils.VertexIDType
import viewmodels.pages.HomePageViewModel
import java.io.File

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
 * @property isCustomSaveDirectory flag to indicate whether user didn't chosen default directory to save the new graph
 */
class CreateNewGraphDialogViewModel(val homePageViewModel: HomePageViewModel) {
	private val localFileSavingDirectory = File(
		homePageViewModel.settings.applicationContextDirectory,
		"local-simple-dbs"
	)
	private val sqliteSavingDirectory = File(
		homePageViewModel.settings.applicationContextDirectory,
		"sqlite-dbs"

	)
	val graphName = mutableStateOf("")
	val selectedVertexTypeID = mutableStateOf(VertexIDType.INT_TYPE)
	val isGraphWeighted = mutableStateOf(false)
	val isGraphDirected = mutableStateOf(false)
	val selectedSaveType = mutableStateOf(GraphSavingType.LOCAL_FILE)
	val settings: SettingsModel = homePageViewModel.settings
	val isCustomSaveDirectory = mutableStateOf(false)
	val saveFolder = mutableStateOf(localFileSavingDirectory)

	init {
		if (!localFileSavingDirectory.exists()) localFileSavingDirectory.mkdirs()
		if (!sqliteSavingDirectory.exists()) sqliteSavingDirectory.mkdirs()
	}

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

	/**
	 * Updates the save folder based on the selected graph saving type.
	 *
	 * This function checks if the user has chosen a custom save directory. If not, it updates the save folder
	 * based on the selected graph saving type.
	 *
	 * @param saveType the type of saving the new graph. It can be either [GraphSavingType.LOCAL_FILE] or
	 * [GraphSavingType.SQLITE_DB].
	 *
	 * @see isCustomSaveDirectory
	 * @see localFileSavingDirectory
	 * @see sqliteSavingDirectory
	 * @see saveFolder
	 */
	fun updateSaveFolder(saveType: GraphSavingType) {
		if (!isCustomSaveDirectory.value) {
			when (saveType) {
				GraphSavingType.LOCAL_FILE ->
					saveFolder.value = localFileSavingDirectory
				GraphSavingType.SQLITE_DB ->
					saveFolder.value = sqliteSavingDirectory
				else -> println("Choose saving type of graph as $saveType.")
			}
		}
	}
}
