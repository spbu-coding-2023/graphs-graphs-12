package viewmodels.dialogs

import androidx.compose.runtime.mutableStateOf
import utils.SaveType
import utils.VertexIDType
import viewmodels.pages.HomePageViewModel

class CreateNewGraphDialogViewModel(val homePageViewModel: HomePageViewModel) {
	val graphName =  mutableStateOf("")
	val selectedVertexTypeID = mutableStateOf(VertexIDType.INT_TYPE)
	val isGraphWeighted = mutableStateOf(false)
	val isGraphDirected = mutableStateOf(false)
	val selectedSaveType = mutableStateOf(SaveType.LOCAL_FILE)
}