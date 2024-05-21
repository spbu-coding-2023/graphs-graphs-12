package views.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import utils.ListWidget
import viewmodels.dialogs.CreateNewGraphDialogViewModel
import viewmodels.pages.HomePageViewModel
import views.dialogs.CreateNewGraphDialog

@Composable
fun HomePage(viewModel: HomePageViewModel) {
	Row {
		ListWidget(
			modifier = viewModel.modifierColumn.weight(1f),
			listItems = viewModel.previouslyLoadedGraph,
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
					modifier = Modifier.size(52.dp, 52.dp).clip(CircleShape)
				)
				Text("Previously opened graphs:", fontSize = 20.sp)
			}
		}
		ListWidget(
			modifier = viewModel.modifierColumn.weight(1f),
			listItems = viewModel.tasks
		) {
			Row(
				modifier = Modifier.padding(20.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Default.Task,
					contentDescription = "image-tasks",
					modifier = Modifier.size(52.dp, 52.dp).clip(CircleShape)
				)
				Text("Tasks:", fontSize = 20.sp)
			}
		}
	}
	if (viewModel.isOpenDialogOfCreatingNewGraph) {
		CreateNewGraphDialog(CreateNewGraphDialogViewModel(viewModel))
	}
}
