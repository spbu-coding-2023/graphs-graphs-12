package views.graphs


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewmodels.graphs.GraphViewModel

@Composable
fun GraphView(
	viewModel: GraphViewModel,
) {
	Box(modifier = Modifier
		.fillMaxSize()
	) {
		viewModel.vertices.forEach { v ->
			VertexView(v, Modifier)
		}
		viewModel.edges.forEach { e ->
			EdgeView(e, Modifier)
		}
	}
}