package views.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import themes.colorVerticesStart
import themes.whiteCustom
import utils.placement_strategy.ForceDirectedPlacementStrategy
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import windowSizeStart

private var curVertexColor: Color = colorVerticesStart
internal val colorChangeFlag: MutableState<Boolean> = mutableStateOf(false)

@Composable
fun VertexView(
	vertexViewModel: VertexViewModel,
	center: Offset,
	zoomAnimated: Float,
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	coroutinePlace: CoroutineScope,
	isKeyboardPressed: MutableState<Boolean>
) {
	Canvas(Modifier) {
		drawCircle(
			center = Offset(
				(center.x.dp + (vertexViewModel.xPos - center.x.dp) * zoomAnimated).toPx(),
				(center.y.dp + (vertexViewModel.yPos - center.y.dp) * zoomAnimated).toPx()
			),
			radius = vertexViewModel.radius.toPx() * zoomAnimated,
			color = vertexViewModel.color
		)
	}
	IconButton(
		onClick = {
			if (idVerticesInfo.value == null) {
				curVertexColor = vertexViewModel.color
			}

			if (isKeyboardPressed.value) {
				if (idVerticesInfo.value != null) {
					val vertexSource = idVerticesInfo.value!!
					idVerticesInfo.value = null
					graphViewModel.addEdge(vertexSource.id, vertexViewModel.id)
					idVerticesInfo.value = vertexSource
				}
				isKeyboardPressed.value = !isKeyboardPressed.value
			} else {
				if (idVerticesInfo.value != vertexViewModel) {
					if (idVerticesInfo.value != null && !colorChangeFlag.value) {
						idVerticesInfo.value!!.color = curVertexColor
					}
					colorChangeFlag.value = false
					idVerticesInfo.value = vertexViewModel
					curVertexColor = vertexViewModel.color
					idVerticesInfo.value!!.color = whiteCustom
				} else {
					if (!colorChangeFlag.value) {
						idVerticesInfo.value!!.color = curVertexColor
					}
					colorChangeFlag.value = false
					idVerticesInfo.value = null
				}
			}
		},
		modifier = Modifier
			.size(vertexViewModel.radius * zoomAnimated * 2, vertexViewModel.radius * zoomAnimated * 2)
			.offset(
				center.x.dp + (vertexViewModel.xPos - center.x.dp) * zoomAnimated - vertexViewModel.radius * zoomAnimated,
				center.y.dp + (vertexViewModel.yPos - center.y.dp) * zoomAnimated - vertexViewModel.radius * zoomAnimated
			)
			.pointerInput(vertexViewModel) {
				detectDragGestures { change, dragAmount ->
					change.consume()
					vertexViewModel.onDrag(dragAmount)
					coroutinePlace.launch {
						ForceDirectedPlacementStrategy(graphViewModel).placeWithoutVertex(
							windowSizeStart.second.toDouble(),
							windowSizeStart.second.toDouble(),
							vertexViewModel
						)
					}
				}
			}
			.onPreviewKeyEvent {
//				isKeyboardPressed = it.key == Key.CtrlLeft || it.key == Key.CtrlRight || it.isCtrlPressed
				isKeyboardPressed.value = it.isShiftPressed
				false
			},
		content = {
			if (vertexViewModel.visibility && (vertexViewModel.radius * zoomAnimated * 2).value > 22) {
				Text(text = vertexViewModel.label, maxLines = 1)
			}
		}
	)
}
