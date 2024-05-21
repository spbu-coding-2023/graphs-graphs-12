package views.pages

import androidx.compose.runtime.Composable
import viewmodels.pages.GraphPageViewModel
import views.graphs.GraphView

@Composable
fun GraphViewPage(graphPageViewModel: GraphPageViewModel) {
//	Text("${graphPageViewModel.graphViewModel?.graph}")
	val graphViewModel = graphPageViewModel.graphViewModel
	if (graphViewModel != null) GraphView(viewModel = graphViewModel)
}