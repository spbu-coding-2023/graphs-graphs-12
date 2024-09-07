package views.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Task
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import themes.JetTheme
import themes.paddingCustom
import themes.sizeBottom
import utils.LazyListWidget
import utils.StaticListWidget
import viewmodels.dialogs.CreateNewGraphDialogViewModel
import viewmodels.dialogs.LoadNewGraphDialogViewModel
import viewmodels.pages.HomePageViewModel
import views.dialogs.CreateNewGraphDialog
import views.dialogs.LoadNewGraphDialog

/**
 * This function represents the HomePage composable in the application.
 * It displays two lists: one for previously loaded graphs and another for tasks.
 * It also handles the opening of two dialogs: one for creating a new graph and another for loading a new graph.
 *
 * @param viewModel the view model for the HomePage. It provides data and functionality for the composable
 */
@Composable
fun HomePage(viewModel: HomePageViewModel) {
	val modifierColumn = Modifier
		.fillMaxHeight()
		.padding(paddingCustom)
		.clip(JetTheme.shapes.cornerStyle)
		.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle)

	Row(
		modifier = Modifier.testTag("home-page")
	) {
		LazyListWidget(
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
				Text(
					"Tasks:",
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
