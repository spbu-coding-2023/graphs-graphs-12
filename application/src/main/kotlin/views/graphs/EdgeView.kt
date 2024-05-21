package views.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import viewmodels.graphs.EdgeViewModel

@Composable
fun EdgeView(
	viewModel: EdgeViewModel,
	modifier: Modifier = Modifier,
) {
	Canvas(modifier = modifier.fillMaxSize()) {
		drawLine(
			start = Offset(
				viewModel.source.xPos.toPx() + viewModel.source.radius.toPx(),
				viewModel.source.yPos.toPx() + viewModel.source.radius.toPx(),
			),
			end = Offset(
				viewModel.target.xPos.toPx() + viewModel.target.radius.toPx(),
				viewModel.target.yPos.toPx() + viewModel.target.radius.toPx(),
			),
			color = Color.Black
		)
	}
	Text(
		modifier = Modifier
			.offset(
				viewModel.source.xPos + (viewModel.target.xPos - viewModel.source.xPos) / 2,
				viewModel.source.yPos + (viewModel.target.yPos - viewModel.source.yPos) / 2
			),
		text = viewModel.label,
	)
}
