package views.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import themes.JetTheme
import themes.paddingCustom
import themes.sizeBottom
import utils.ListWidget
import utils.StaticListWidget
import viewmodels.dialogs.CreateNewGraphDialogViewModel
import viewmodels.dialogs.LoadNewGraphDialogViewModel
import viewmodels.pages.HomePageViewModel
import views.dialogs.CreateNewGraphDialog
import views.dialogs.LoadNewGraphDialog

@Composable
fun HomePage(viewModel: HomePageViewModel) {
	val modifierColumn = Modifier
		.fillMaxHeight()
		.padding(paddingCustom)
		.clip(JetTheme.shapes.cornerStyle)
		.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)

	Row {
		ListWidget(
			modifier = modifierColumn.weight(1f),
			listItems = viewModel.previouslyLoadedGraph,
			itemWidth = 0.75f,
			dropDownMenuContext = { item ->
				// TODO(Change onClick lambdas and coroutines)
				TextButton(onClick = { println("Copy $item") }, modifier = Modifier.fillMaxWidth()) {
					Text("Copy into list")
				}
				TextButton(onClick = { println("Remove $item") }, modifier = Modifier.fillMaxWidth()) {
					Text("Remove")
				}
				TextButton(onClick = { println("Show $item in explorer") }, modifier = Modifier.fillMaxWidth()) {
					Text("Show in explorer")
				}
			}
		) {
			Row(
				modifier = Modifier.padding(20.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Default.DateRange,
					contentDescription = "Calendar",
					modifier = Modifier.size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
					tint = JetTheme.colors.tintColor
				)
				Text(
					"Previously opened graphs:",
					style = JetTheme.typography.toolbar,
					color = JetTheme.colors.secondaryText
				)
			}
		}
		StaticListWidget(
			modifier = modifierColumn.weight(1f),
			listItems = viewModel.tasks,
			itemWidth = 0.75f
		) {
			Row(
				modifier = Modifier.padding(20.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Default.Task,
					contentDescription = "image-tasks",
					modifier = Modifier.size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
					tint = JetTheme.colors.tintColor
				)
				Text("Tasks:",
					style = JetTheme.typography.toolbar,
					color = JetTheme.colors.secondaryText
				)
			}
		}
	}
	if (viewModel.isOpenDialogOfCreatingNewGraph) {
		CreateNewGraphDialog(CreateNewGraphDialogViewModel(viewModel))
	}
	if (viewModel.isOpenDialogOfLoadingNewGraph) {
		LoadNewGraphDialog(LoadNewGraphDialogViewModel(viewModel))
	}
}
