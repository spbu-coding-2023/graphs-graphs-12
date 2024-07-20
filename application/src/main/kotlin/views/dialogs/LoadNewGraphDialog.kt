package views.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import kotlinx.coroutines.launch
import themes.JetTheme
import utils.ComboBox
import utils.GraphSavingType
import utils.filedialogs.FilePickerDialog
import viewmodels.dialogs.LoadNewGraphDialogViewModel

/**
 * A composable function that represents a dialog window for loading a new graph.
 *
 * @param viewModel the view model that holds the state and logic for the dialog
 */
@Composable
fun LoadNewGraphDialog(viewModel: LoadNewGraphDialogViewModel) {
	val coroutineScope = rememberCoroutineScope()
	var isOpenFilePickDialog by remember { mutableStateOf(false) }
	DialogWindow(
		onCloseRequest = { viewModel.homePageViewModel.isOpenDialogOfLoadingNewGraph = false },
		state = rememberDialogState(position = WindowPosition(Alignment.Center), size = DpSize(500.dp, 300.dp)),
		title = "Load graph",
		resizable = false,
	) {
		Column(
			Modifier
				.fillMaxSize()
				.background(JetTheme.colors.primaryBackground)
				.padding(4.dp)
		) {
			val modifierRow = Modifier.padding(0.dp, 5.dp, 0.dp, 5.dp).weight(1f)
			val verticalRow = Alignment.CenterVertically
			Row(modifierRow, verticalAlignment = verticalRow) {
				Text(
					"Database type",
					modifier = Modifier.weight(0.5f),
					textAlign = TextAlign.Center,
					style = JetTheme.typography.toolbar
				)
				ComboBox(
					items = GraphSavingType.entries.toTypedArray(),
					modifier = Modifier.weight(1f),
					onItemClick = { item: GraphSavingType -> viewModel.selectedLoadType.value = item },
					textAlign = TextAlign.Center
				)
			}
			Row(
				modifierRow,
				verticalAlignment = verticalRow,
				horizontalArrangement = Arrangement.spacedBy(4.dp)
			) {
				if (viewModel.selectedLoadType.value != GraphSavingType.NEO4J_DB) {
					OutlinedTextField(
						value = viewModel.loadFile.value,
						readOnly = true,
						label = { Text("File path", style = JetTheme.typography.toolbar) },
						onValueChange = {},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
					)
					IconButton(
						onClick = { isOpenFilePickDialog = true }
					) {
						Icon(Icons.Filled.Folder, "img-folder")
					}
				} else {
					OutlinedTextField(
						value = viewModel.neo4jHost.value,
						label = { Text("Host", style = JetTheme.typography.toolbar) },
						onValueChange = { newValue ->
							viewModel.neo4jHost.value = newValue
							viewModel.isCrateNeo4jConnection.value = false
						},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
						isError = viewModel.isCrateNeo4jConnection.value
					)
					OutlinedTextField(
						value = viewModel.neo4jUserName.value,
						label = { Text("User", style = JetTheme.typography.toolbar) },
						onValueChange = { newValue ->
							viewModel.neo4jUserName.value = newValue
							viewModel.isCrateNeo4jConnection.value = false
						},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
						isError = viewModel.isCrateNeo4jConnection.value
					)
					OutlinedTextField(
						value = viewModel.neo4jPassword.value,
						label = { Text("Password", style = JetTheme.typography.toolbar) },
						onValueChange = { newValue ->
							viewModel.neo4jPassword.value = newValue
							viewModel.isCrateNeo4jConnection.value = false
						},
						modifier = Modifier.weight(1f),
						singleLine = true,
						colors = TextFieldDefaults.textFieldColors(
							focusedIndicatorColor = JetTheme.colors.secondaryText,
							focusedLabelColor = JetTheme.colors.secondaryText,
							cursorColor = JetTheme.colors.tintColor
						),
						isError = viewModel.isCrateNeo4jConnection.value
					)
				}
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
					onClick = { viewModel.homePageViewModel.isOpenDialogOfLoadingNewGraph = false }
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
						var isLoad: Boolean
						when (viewModel.selectedLoadType.value) {
							GraphSavingType.NEO4J_DB -> {
								viewModel.settings.connectToNeo4J(
									viewModel.neo4jHost.value,
									viewModel.neo4jUserName.value,
									viewModel.neo4jPassword.value
								)
								if (!viewModel.settings.isNeo4jConnected) {
									viewModel.isCrateNeo4jConnection.value = true
									return@TextButton
								}
								coroutineScope.launch {
									isLoad = viewModel.settings.loadGraphFromNEO4J(
										viewModel.homePageViewModel.graphPage
									)
									if (isLoad) {
										viewModel.homePageViewModel.loadGraphInfo(GraphSavingType.NEO4J_DB, "")
										viewModel.homePageViewModel.isOpenDialogOfLoadingNewGraph = false
									}
								}
							}
							GraphSavingType.LOCAL_FILE -> {
								coroutineScope.launch {
									isLoad = viewModel.settings.loadGraphFromJSON(
										viewModel.homePageViewModel.graphPage,
										viewModel.loadFile.value
									)
									if (isLoad) {
										viewModel.homePageViewModel.loadGraphInfo(
											GraphSavingType.LOCAL_FILE,
											viewModel.loadFile.value
										)
										viewModel.homePageViewModel.isOpenDialogOfLoadingNewGraph = false
									}
								}
							}
							GraphSavingType.SQLITE_DB -> {
								coroutineScope.launch {
									isLoad = viewModel.settings.loadGraphFromSQLite(
										viewModel.homePageViewModel.graphPage,
										viewModel.loadFile.value
									)
									if (isLoad) {
										viewModel.homePageViewModel.loadGraphInfo(
											GraphSavingType.SQLITE_DB,
											viewModel.loadFile.value
										)
										viewModel.homePageViewModel.isOpenDialogOfLoadingNewGraph = false
									}
								}
							}
						}
					}
				) {
					Text("Load", textAlign = TextAlign.Center)
				}
			}
		}
		FilePickerDialog(
			isOpenFilePickDialog,
			"Load graph-info file",
			initDirectory = viewModel.settings.applicationContextDirectory,
			onCloseRequest = { isOpenFilePickDialog = false },
			onChooseFile = { selectedPath ->
				isOpenFilePickDialog = false
				viewModel.loadFile.value = selectedPath.path
			},
			fileExtension = viewModel.fileExtension
		)
	}
}
