package utils.filedialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import mu.KotlinLogging
import themes.JetTheme
import themes.sizeBottom
import java.io.File

private val logger = KotlinLogging.logger("FileDialogs")

/**
 * A composable function that displays a dialog window for selecting a file or directory.
 *
 * @param isOpen a boolean indicating whether the dialog is open or closed
 * @param icon the painter for the icon displayed in the dialog window
 * @param title the title of the dialog window
 * @param initDirectory the initial directory to display when the dialog is opened
 * @param onCloseRequest a lambda function to be called when the dialog is closed
 * @param onChooseItem an optional lambda function to be called when an item (file or directory) is chosen.
 *                      It takes two parameters: the chosen directory and the chosen file (or null if a file isn't chosen).
 *                      It should return a boolean indicating whether the dialog should be closed after the item is chosen
 * @param filter an optional lambda function to filter the items displayed in the dialog.
 *               It takes a File as a parameter and returns a boolean indicating whether the item should be displayed.
 */
@Composable
fun PickerDialog(
	isOpen: Boolean,
	icon: Painter,
	title: String,
	initDirectory: File,
	onCloseRequest: (() -> Unit),
	onChooseItem: ((File, File?) -> Boolean)? = null,
	filter: (File) -> Boolean = { true }
) {
	var currentDirectory by remember { mutableStateOf(initDirectory) }
	var currentFile by remember { mutableStateOf<File?>(null) }
	DialogWindow(
		visible = isOpen,
		onCloseRequest = onCloseRequest,
		title = title,
		state = DialogState(
			position = WindowPosition(Alignment.Center),
			size = DpSize(1024.dp, 720.dp)
		),
		icon = icon
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(JetTheme.colors.primaryBackground)
				.padding(4.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Row(
				modifier = Modifier.weight(1f),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					icon,
					title,
					modifier = Modifier.weight(0.5f).size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
					tint = JetTheme.colors.tintColor
				)
				IconButton(
					onClick = {
						currentDirectory = File(System.getProperty("user.home"))
					},
					modifier = Modifier.weight(0.5f)
				) {
					Icon(
						Icons.Filled.Home,
						"Home",
						modifier = Modifier.size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
						tint = JetTheme.colors.tintColor
					)
				}
				IconButton(
					onClick = {
						if (currentDirectory.parentFile != null) {
							currentDirectory = currentDirectory.parentFile
							currentFile = null
						}
					},
					modifier = Modifier.weight(0.5f)
				) {
					Icon(
						Icons.Filled.ArrowLeft,
						"Back",
						modifier = Modifier.size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
						tint = JetTheme.colors.tintColor
					)
				}
				OutlinedTextField(
					value = currentDirectory.absolutePath,
					onValueChange = { _ ->
					},
					readOnly = true,
					modifier = Modifier.weight(6f),
					singleLine = true,
					colors = TextFieldDefaults.textFieldColors(
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					textStyle = JetTheme.typography.toolbar.copy(textAlign = TextAlign.Left)
				)
			}
			Row(
				modifier = Modifier.weight(8f),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(4.dp)
			) {
				DirectoryContent(
					modifier = Modifier.weight(2f),
					currentDirectory,
					onItemClick = { file ->
						if (file.isFile) {
							currentFile = file
						} else if (file.isDirectory) {
							currentDirectory = file
							currentFile = null
						}
					},
					filter
				)
				currentFile?.let { file ->
					FileContent(
						modifier = Modifier.weight(1f),
						file
					)
				}
			}
			Row(
				modifier = Modifier.weight(1f),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(4.dp)
			) {
				Spacer(Modifier.weight(4f))
				TextButton(
					modifier = Modifier.weight(1f),
					onClick = onCloseRequest,
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
				) {
					Text("Cancel", textAlign = TextAlign.Center)
				}
				TextButton(
					modifier = Modifier.weight(1f),
					onClick = {
						var isClose = true
						if (onChooseItem != null) isClose = onChooseItem(currentDirectory, currentFile)
						if (isClose) onCloseRequest()
					},
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
				) {
					Text("Open", textAlign = TextAlign.Center)
				}
			}
		}
	}
}

/**
 * A composable function that displays a dialog window for selecting a directory.
 * It is a wrapper around the [PickerDialog] function, with a default icon for directory selection.
 *
 * @param isOpen a boolean indicating whether the dialog is open or closed
 * @param title the title of the dialog window. Default value is "Directory Picker"
 * @param initDirectory the initial directory to display when the dialog is opened
 * @param onCloseRequest a lambda function to be called when the dialog is closed
 * @param onChooseDirectory an optional lambda function to be called when a directory is chosen.
 *                          It takes a single parameter: the chosen directory.
 *                          It should return a boolean indicating whether the dialog should be closed after the directory is chosen.
 *                          Default value is null, meaning no action is taken when a directory is chosen
 */
@Composable
fun DirectoryPickerDialog(
	isOpen: Boolean,
	title: String = "Directory Picker",
	initDirectory: File,
	onCloseRequest: (() -> Unit),
	onChooseDirectory: ((File) -> Unit)? = null,
) {
	if (isOpen) {
		logger.info { "Open DirectoryPickerDialog for directory: $initDirectory" }
	}
	PickerDialog(
		isOpen,
		rememberVectorPainter(Icons.Filled.FolderOpen),
		title,
		initDirectory,
		onCloseRequest,
		onChooseItem = { currentDirectory, _ ->
			if (onChooseDirectory != null) onChooseDirectory(currentDirectory)
			true
		},
		filter = { item -> item.isDirectory }
	)
}

/**
 * A composable function that displays a dialog window for selecting a file.
 * It is a wrapper around the [PickerDialog] function, with a default icon for file selection.
 *
 * @param isOpen a boolean indicating whether the dialog is open or closed
 * @param title the title of the dialog window. Default value is "Directory Picker"
 * @param initDirectory the initial directory to display when the dialog is opened
 * @param onCloseRequest a lambda function to be called when the dialog is closed
 * @param onChooseFile an optional lambda function to be called when a file is chosen.
 *                     It takes a single parameter: the chosen file.
 *                     It should return a boolean indicating whether the dialog should be closed after the file is chosen.
 *                     Default value is null, meaning no action is taken when a file is chosen
 * @param fileExtension a list of regular expressions representing the allowed file extensions.
 *                      Only files with names matching any of the provided regular expressions will be displayed.
 *                      Default value is an empty list, meaning all files will be displayed
 */
@Composable
fun FilePickerDialog(
	isOpen: Boolean,
	title: String = "Directory Picker",
	initDirectory: File,
	onCloseRequest: (() -> Unit),
	onChooseFile: ((File) -> Unit)? = null,
	fileExtension: List<Regex> = listOf()
) {
	if (isOpen) {
		logger.info { "Open FilePickerDialog with init directory: $initDirectory" }
	}
	PickerDialog(
		isOpen,
		rememberVectorPainter(Icons.Filled.FileOpen),
		title,
		initDirectory,
		onCloseRequest,
		onChooseItem = { _, currentFile ->
			if (currentFile == null) {
				false
			} else if (onChooseFile != null) {
				onChooseFile(currentFile)
				true
			} else {
				true
			}
		},
		filter = { item ->
			item.isDirectory || (
				item.isFile && fileExtension.all { regex: Regex -> regex.matches(item.name) }
			)
		}
	)
}
