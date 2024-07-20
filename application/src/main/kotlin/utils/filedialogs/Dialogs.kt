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
import themes.JetTheme
import themes.sizeBottom
import java.io.File

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
						if (currentDirectory.parentFile!= null) {
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
					onValueChange = { _ ->  },
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
			Row (
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

@Composable
fun DirectoryPickerDialog(
	isOpen: Boolean,
	title: String = "Directory Picker",
	initDirectory: File,
	onCloseRequest: (() -> Unit),
	onChooseDirectory: ((File) -> Unit)? = null,
) {
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

@Composable
fun FilePickerDialog(
	isOpen: Boolean,
	title: String = "Directory Picker",
	initDirectory: File,
	onCloseRequest: (() -> Unit),
	onChooseFile: ((File) -> Unit)? = null,
	fileExtension: List<Regex> = listOf()
) {
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
				onChooseFile(currentFile);
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
