package viewmodels.dialogs

import androidx.compose.runtime.mutableStateOf
import utils.GraphSavingType
import utils.VertexIDType
import viewmodels.pages.HomePageViewModel
import java.io.File

class CreateNewGraphDialogViewModel(val homePageViewModel: HomePageViewModel) {
	val graphName =  mutableStateOf("")
	val selectedVertexTypeID = mutableStateOf(VertexIDType.INT_TYPE)
	val isGraphWeighted = mutableStateOf(false)
	val isGraphDirected = mutableStateOf(false)
	val selectedSaveType = mutableStateOf(GraphSavingType.LOCAL_FILE)
	val saveFolder = mutableStateOf(System.getProperty("user.home"))
	val settings = homePageViewModel.settings

	fun isValidGraphName(newGraphName: String): Boolean {
		return settings.graphNameRegEx.matches(newGraphName)
	}
}
