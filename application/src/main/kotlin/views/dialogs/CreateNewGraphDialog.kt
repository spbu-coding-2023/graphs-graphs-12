package views.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.darkrockstudios.libraries.mpfilepicker.DirectoryPicker
import kotlinx.coroutines.launch
import themes.JetTheme
import utils.ComboBox
import utils.CustomRadioButton
import utils.GraphSavingType
import utils.VertexIDType
import viewmodels.dialogs.CreateNewGraphDialogViewModel

@Composable
fun CreateNewGraphDialog(viewModel: CreateNewGraphDialogViewModel) {
	val coroutineScope = rememberCoroutineScope()
	val focusRequester = remember { FocusRequester() }
	var isFoundGraphName by remember { mutableStateOf(true) }
	var isOpenFolderPickDialog by remember { mutableStateOf(false) }
	var neo4jHost by remember { mutableStateOf("") }
	var neo4jUserName by remember { mutableStateOf("") }
	var neo4jPassword by remember { mutableStateOf("") }
	var isCrateNeo4jConnection by remember { mutableStateOf(false) }
	DialogWindow(
		onCloseRequest = { viewModel.homePageViewModel.isOpenDialogOfCreatingNewGraph = false },
		state = rememberDialogState(position = WindowPosition(Alignment.Center), size = DpSize(700.dp, 470.dp)),
		title = "Create new graph",
		resizable = false,
	) {
		Column(
			Modifier
				.fillMaxSize()
				.background(JetTheme.colors.primaryBackground)
				.padding(4.dp)
		) {
			LaunchedEffect(Unit) {
				focusRequester.requestFocus()
			}
			val modifierRow = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp).weight(1f)
			val verticalRow = Alignment.CenterVertically
			Row(modifierRow, verticalAlignment = verticalRow) {
				Text(
					"Graph name",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center,
					style = JetTheme.typography.toolbar,
					color = JetTheme.colors.secondaryText
				)
				OutlinedTextField(
					modifier = Modifier.weight(1f).focusRequester(focusRequester),
					value = viewModel.graphName.value,
					onValueChange = { newValue ->
						if (!viewModel.isValidGraphName(newValue) && newValue.length >= viewModel.graphName.value.length) {
							isFoundGraphName = false
						} else {
							isFoundGraphName = true
							viewModel.graphName.value = newValue
						}
					},
					label = { Text("Name") },
					singleLine = true,
					colors = TextFieldDefaults.textFieldColors(
						textColor = JetTheme.colors.secondaryText,
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					isError = !isFoundGraphName,
				)
			}
			AnimatedVisibility(visible = !isFoundGraphName) {
				Text(
					text = "Graph name regex: '${viewModel.homePageViewModel.settings.graphNameRegEx}'",
					modifier = Modifier.fillMaxWidth(),
					style = LocalTextStyle.current.copy(color = MaterialTheme.colors.error),
					textAlign = TextAlign.Right
				)
			}
			Row(modifierRow, verticalAlignment = verticalRow) {
				Text(
					"Vertices ID type",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center,
					style = JetTheme.typography.toolbar,
					color = JetTheme.colors.secondaryText
				)
				ComboBox(
					items = VertexIDType.entries.toTypedArray(),
					modifier = Modifier.weight(1f),
					onItemClick = { item: VertexIDType -> viewModel.selectedVertexTypeID.value = item },
					textAlign = TextAlign.Center
				)
			}
			Row(modifierRow, verticalAlignment = verticalRow) {
				Text(
					"Database type",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center,
					style = JetTheme.typography.toolbar,
					color = JetTheme.colors.secondaryText
				)
				ComboBox(
					items = GraphSavingType.entries.toTypedArray(),
					modifier = Modifier.weight(1f),
					onItemClick = { item: GraphSavingType -> viewModel.selectedSaveType.value = item },
					textAlign = TextAlign.Center
				)
			}
			Row(
				modifierRow,
				verticalAlignment = verticalRow,
				horizontalArrangement = Arrangement.spacedBy(4.dp)
			) {
				if (viewModel.selectedSaveType.value != GraphSavingType.NEO4J_DB) {
					OutlinedTextField(
						value = viewModel.saveFolder.value,
						readOnly = true,
						label = { Text("Folder path", style = JetTheme.typography.toolbar) },
						onValueChange = {},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							textColor = JetTheme.colors.secondaryText,
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
					)
					IconButton(
						onClick = { isOpenFolderPickDialog = true }
					) {
						Icon(Icons.Filled.Folder, "img-folder")
					}
				} else {
					OutlinedTextField(
						value = neo4jHost,
						label = { Text("Host", style = JetTheme.typography.toolbar) },
						onValueChange = {newValue -> neo4jHost = newValue; isCrateNeo4jConnection = false},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
						isError = isCrateNeo4jConnection
					)
					OutlinedTextField(
						value = neo4jUserName,
						label = { Text("User", style = JetTheme.typography.toolbar) },
						onValueChange = {newValue -> neo4jUserName = newValue; isCrateNeo4jConnection = false},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
						isError = isCrateNeo4jConnection
					)
					OutlinedTextField(
						value = neo4jPassword,
						label = { Text("Password", style = JetTheme.typography.toolbar) },
						onValueChange = {newValue -> neo4jPassword = newValue; isCrateNeo4jConnection = false},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
						isError = isCrateNeo4jConnection
					)
				}
			}
			Row(modifierRow, verticalAlignment = verticalRow) {
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
			Row(modifierRow, verticalAlignment = verticalRow) {
				Spacer(modifier = Modifier.weight(1f))
				TextButton(
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
					modifier = Modifier.weight(0.5f).padding(5.dp, 0.dp),
					onClick = { viewModel.homePageViewModel.isOpenDialogOfCreatingNewGraph = false }
				) {
					Text("Cancel", textAlign = TextAlign.Center)
				}
				TextButton(
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
					modifier = Modifier.weight(0.5f).padding(5.dp, 0.dp),
					onClick = {
						if (viewModel.graphName.value.isEmpty()) {
							isFoundGraphName = false
						} else {
							if (viewModel.selectedSaveType.value == GraphSavingType.NEO4J_DB){
								viewModel.settings.connectToNeo4J(neo4jHost, neo4jUserName, neo4jPassword)
								if (!viewModel.settings.isNeo4jConnected) {
									isCrateNeo4jConnection = true
									return@TextButton
								}
							}
							println("Load")
							isFoundGraphName = true
							coroutineScope.launch {
								viewModel.homePageViewModel.createGraph(
									viewModel.graphName.value,
									viewModel.selectedVertexTypeID.value,
									viewModel.isGraphDirected.value,
									viewModel.isGraphWeighted.value,
									viewModel.selectedSaveType.value,
									viewModel.saveFolder.value
								)
							}
							viewModel.homePageViewModel.isOpenDialogOfCreatingNewGraph = false
						}
					}
				) {
					Text("Create")
				}
			}
		}
		DirectoryPicker(isOpenFolderPickDialog) { path ->
			isOpenFolderPickDialog = false
			if (path != null) viewModel.saveFolder.value = path
		}
	}
}
