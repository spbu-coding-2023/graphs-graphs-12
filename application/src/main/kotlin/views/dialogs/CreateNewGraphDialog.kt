package views.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.launch
import utils.ComboBox
import utils.CustomRadioButton
import utils.GraphSavingType
import utils.VertexIDType
import viewmodels.dialogs.CreateNewGraphDialogViewModel

@Composable
fun CreateNewGraphDialog(viewModel: CreateNewGraphDialogViewModel) {
	val coroutineScope = rememberCoroutineScope()
//	val graphName = remember { mutableStateOf("") }
//	val selectedVertexTypeID = remember { mutableStateOf(VertexIDType.INT_TYPE) }
//	val isGraphWeighted = remember { mutableStateOf(false) }
//	val isGraphDirected = remember { mutableStateOf(false) }
//	val selectedSaveType = remember { mutableStateOf(SaveType.LOCAL_FILE) }
	DialogWindow(
		onCloseRequest = { viewModel.homePageViewModel.isOpenDialogOfCreatingNewGraph = false },
		state = rememberDialogState(position = WindowPosition(Alignment.Center), size = DpSize(500.dp, 350.dp)),
		title = "Create new graph",
		resizable = true
	) {
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
			) {
				Text(
					"Graph name:",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center
				)
				TextField(
					modifier = Modifier.weight(1f),
					value = viewModel.graphName.value,
					onValueChange = { newValue -> viewModel.graphName.value = newValue },
					singleLine = true,
					textStyle = TextStyle(textAlign = TextAlign.Center)
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
			) {
				Text(
					"Vertices ID type:",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center
				)
				ComboBox(
					items = VertexIDType.entries.toTypedArray(),
					modifier = Modifier.weight(1f),
					onItemClick = { item: VertexIDType -> viewModel.selectedVertexTypeID.value = item },
					textAlign = TextAlign.Center
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
			) {
				CustomRadioButton(
					selected = viewModel.isGraphWeighted.value,
					onClick = { viewModel.isGraphWeighted.value = !viewModel.isGraphWeighted.value },
					text = "Weighted graph",
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.weight(1f),
					reversed = true
				)
				CustomRadioButton(
					selected = viewModel.isGraphDirected.value,
					onClick = { viewModel.isGraphDirected.value = !viewModel.isGraphDirected.value },
					text = "Directed graph",
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.weight(1f),
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
			) {
				Text(
					"Vertices ID type:",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center
				)
				ComboBox(
					items = GraphSavingType.entries.toTypedArray(),
					modifier = Modifier.weight(1f),
					onItemClick = { item: GraphSavingType -> viewModel.selectedSaveType.value = item },
					textAlign = TextAlign.Center
				)
			}
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp)
			) {
				Spacer(modifier = Modifier.weight(1f))
				Button(
					onClick = { viewModel.homePageViewModel.isOpenDialogOfCreatingNewGraph = false }
				) {
					Text("Cancel")
				}
				Spacer(modifier = Modifier.weight(0.005f))
				Button(
					onClick = {
						if (viewModel.graphName.value.trim() == "") {
							println("Invalid value")
						}
						else {
							viewModel.homePageViewModel.isOpenDialogOfCreatingNewGraph = false
							coroutineScope.launch {
								val graph = viewModel.homePageViewModel.createGraph(
									viewModel.graphName.value,
									viewModel.selectedVertexTypeID.value,
									viewModel.isGraphDirected.value,
									viewModel.isGraphWeighted.value
								)
								viewModel.homePageViewModel.settings.saveGraph(
									graph,
									viewModel.selectedVertexTypeID.value,
									viewModel.selectedSaveType.value
								)
							}
						}
					}
				) {
					Text("Create")
				}
			}
		}
	}
}
