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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

@Composable
fun EmptyContent(
	modifier: Modifier = Modifier,
	message: String = "Can't open content"
) {
	Column(modifier) {
		Icon(Icons.Filled.NotAccessible, message)
	}
}

@Composable
fun FileContent(
	modifier: Modifier = Modifier,
	file: File
) {
	if (!file.isFile) EmptyContent(
		modifier,
		"Can't open content of ${file.absolutePath}"
	)
	if (!file.exists()) EmptyContent(
		modifier,
		"File ${file.absolutePath} not exists"
	)
	Box(modifier.border(0.5.dp, Color.Black, RectangleShape).padding(0.25.dp)) {
		LazyColumn(
			modifier = Modifier.fillMaxSize().align(Alignment.TopCenter),
			horizontalAlignment = Alignment.Start
		) {
			item {
				val reader = BufferedReader(FileReader(file))
				Text(
					modifier = modifier.padding(8.dp),
					text = reader.use { it.readText() }
				)
				reader.close()
			}
		}
	}
}

@Composable
fun DirectoryContent(
	modifier: Modifier = Modifier,
	directory: File,
	onItemClick: ((File) -> (Unit)) = { file -> println("Click on ${file.absolutePath}") },
	filter: (File) -> Boolean = { true }
) {
	if (!directory.isDirectory) EmptyContent(
		modifier,
		"Can't open content of ${directory.absolutePath}"
	)
	if (!directory.exists()) EmptyContent(
		modifier,
		"File ${directory.absolutePath} not exists"
	)
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
						modifier = Modifier.weight(0.5f)
					)
					TextButton(
						onClick = {
							chosenFile = if (!item.isFile) null else item
							onItemClick(item)
						},
						modifier = Modifier.weight(4.5f)
					) {
						Text(item.absolutePath, textAlign = TextAlign.Left)
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
						modifier = Modifier.weight(0.5f)
					)
					TextButton(
						onClick = {
							chosenFile = if (!item.isFile) null else item
							onItemClick(item)
						},
						modifier = Modifier.weight(4.5f)
					) {
						Text(item.absolutePath, textAlign = TextAlign.Left)
					}
				}
			}
		}

	}
}
