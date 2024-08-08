package utils.filedialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.NotAccessible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mu.KotlinLogging
import themes.JetTheme
import themes.sizeBottom
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.text.SimpleDateFormat
import java.util.*

private val logger = KotlinLogging.logger("FileDialogs.Widgets")

/**
 * This function is used to display an empty content screen with a specified message and an icon.
 *
 * @param modifier the modifier to be applied to the layout
 * @param message the message to be displayed on the screen. Default value is "Can't open content"
 */
@Composable
fun EmptyContent(
	modifier: Modifier = Modifier,
	message: String = "Can't open content"
) {
	logger.warn { "Open empty content: $message" }
	Column(modifier) {
		Icon(Icons.Filled.NotAccessible, message)
	}
}

/**
 * This function is used to display an empty content screen with a specified message and an icon.
 *
 * @param modifier the modifier to be applied to the layout
 * @param message the message to be displayed on the screen. Default value is "Can't open content"
 */
@Composable
fun FileContent(
	modifier: Modifier = Modifier,
	file: File
) {
	if (!file.isFile) {
		EmptyContent(
			modifier,
			"Can't open content of file: ${file.absolutePath}"
		)
	} else if (!file.exists()) {
		EmptyContent(
			modifier,
			"File ${file.absolutePath} not exists"
		)
	} else {
		logger.info { "Display content of file: ${file.absolutePath}" }
		val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
		Column(
			modifier.border(0.5.dp, Color.Black, RectangleShape).padding(0.25.dp),
			verticalArrangement = Arrangement.spacedBy(2.dp)
		) {
			Column(
				modifier = Modifier.border(0.5.dp, Color.Black, RectangleShape).padding(0.25.dp),
				verticalArrangement = Arrangement.spacedBy(2.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					"File info",
					modifier = Modifier.fillMaxWidth(),
					textAlign = TextAlign.Center,
					style = JetTheme.typography.body,
					color = JetTheme.colors.secondaryText
				)
				listOf(
					"Name: ${file.name}",
					"Last Modified: ${dateFormat.format(Date(file.lastModified()))}",
					"Size: ${file.length()} bytes"
				).forEach { text ->
					Text(
						text,
						modifier = Modifier.fillMaxWidth().padding(8.dp),
						textAlign = TextAlign.Left,
						style = JetTheme.typography.mini.copy(fontSize = JetTheme.typography.mini.fontSize / 1.15),
						color = JetTheme.colors.secondaryText
					)
				}
			}
			Box {
				LazyColumn(
					modifier = Modifier.fillMaxSize().align(Alignment.TopCenter),
					horizontalAlignment = Alignment.Start
				) {
					item {
						val reader = BufferedReader(FileReader(file))
						Text(
							modifier = modifier.padding(8.dp),
							text = reader.use { it.readText() },
							style = JetTheme.typography.toolbar,
							color = JetTheme.colors.secondaryText
						)
						reader.close()
					}
				}
			}
		}
	}
}

/**
 * This function is used to display the contents of a directory in a structured layout.
 * It provides a list of files and directories within the specified directory, and allows for
 * interaction with each item through the [onItemClick] callback.
 *
 * @param modifier the modifier to be applied to the layout
 * @param directory the directory to be displayed
 * @param onItemClick a callback function that is invoked when an item (file or directory) is clicked.
 * The clicked item is passed as a parameter to this function.
 * @param filter a function that filters the items to be displayed. By default, all items are displayed.
 */
@Composable
fun DirectoryContent(
	modifier: Modifier = Modifier,
	directory: File,
	onItemClick: ((File) -> (Unit)) = { file -> println("Click on ${file.absolutePath}") },
	filter: (File) -> Boolean = { true }
) {
	if (!directory.isDirectory) {
		EmptyContent(
			modifier,
			"Can't open content of ${directory.absolutePath}, bacause it's not a directory"
		)
	} else if (!directory.exists()) {
		EmptyContent(
			modifier,
			"Directory ${directory.absolutePath} not exists"
		)
	} else {
		logger.info { "Display content of directory: $directory" }
		var chosenFile by remember { mutableStateOf<File?>(null) }
		Box(
			modifier
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize().align(Alignment.TopCenter),
				horizontalAlignment = Alignment.Start,
				verticalArrangement = Arrangement.spacedBy(2.dp)
			) {
				val dirItems = filtrate(directory, filter)
				items(dirItems.filter { it.isDirectory }.sorted()) { item ->
					Row(
						modifier = Modifier.border(0.5.dp, Color.Black, RectangleShape),
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							imageVector = if (item.isDirectory) Icons.Filled.Folder else Icons.Filled.FilePresent,
							contentDescription = if (item.isDirectory) "Directory" else "File",
							modifier = Modifier.weight(0.5f).size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
							tint = JetTheme.colors.tintColor
						)
						TextButton(
							onClick = {
								chosenFile = if (!item.isFile) null else item
								onItemClick(item)
							},
							colors = ButtonDefaults.buttonColors(
								backgroundColor = JetTheme.colors.primaryBackground,
								contentColor = JetTheme.colors.secondaryText,
								disabledContentColor = JetTheme.colors.secondaryText,
								disabledBackgroundColor = JetTheme.colors.tertiaryBackground
							),
							modifier = Modifier.weight(4.5f).padding(5.dp, 5.dp),
						) {
							Text(item.absolutePath)
						}
					}
				}
				items(dirItems.filter { it.isFile }.sorted()) { item ->
					val isChosenItem = chosenFile == item
					val itemModifier = if (isChosenItem) Modifier.background(Color.Gray) else Modifier
					Row(
						modifier = itemModifier.fillMaxWidth().border(0.5.dp, Color.Black, RectangleShape),
						verticalAlignment = Alignment.CenterVertically
					) {
						Icon(
							imageVector = if (item.isDirectory) Icons.Filled.Folder else Icons.Filled.FilePresent,
							contentDescription = if (item.isDirectory) "Directory" else "File",
							modifier = Modifier.weight(0.5f).size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
							tint = JetTheme.colors.tintColor
						)
						TextButton(
							onClick = {
								chosenFile = if (!item.isFile) null else item
								onItemClick(item)
							},
							colors = ButtonDefaults.buttonColors(
								backgroundColor = JetTheme.colors.primaryBackground,
								contentColor = JetTheme.colors.secondaryText,
								disabledContentColor = JetTheme.colors.secondaryText,
								disabledBackgroundColor = JetTheme.colors.tertiaryBackground
							),
							modifier = Modifier.weight(4.5f).padding(5.dp, 5.dp),
						) {
							Text(item.absolutePath)
						}
					}
				}
			}
		}
	}
}
