package views.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import models.SettingsModel
import themes.colorVerticesStart
import themes.whiteCustom
import utils.placement_strategy.ForceDirectedPlacementStrategy
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel

// TODO(move its params to SettingsModel)
private var curVertexColor: Color = colorVerticesStart
internal val colorChangeFlag: MutableState<Boolean> = mutableStateOf(false)

/**
 * This function represents a composable view for a vertex in a graph.
 *
 * @param vertexViewModel the ViewModel representing the vertex
 * @param center the center position of the graph
 * @param zoomAnimated the current zoom level of the graph
 * @param graphViewModel the ViewModel representing the entire graph
 * @param idVerticesInfo a mutable state holding the currently selected vertex
 * @param coroutinePlace a coroutine scope for running placement algorithms
 * @param isKeyboardPressed a mutable state indicating whether the shift key is pressed
 * @param settings application settings to depends on actual window size
 */
@Composable
fun VertexView(
	vertexViewModel: VertexViewModel,
	center: Offset,
	zoomAnimated: Float,
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	coroutinePlace: CoroutineScope,
	isKeyboardPressed: MutableState<Boolean>,
	settings: SettingsModel
) {
	BoxWithConstraints(
		modifier = Modifier
			.size(vertexViewModel.radius * zoomAnimated * 2, vertexViewModel.radius * zoomAnimated * 2)
			.offset(
				center.x.dp - (center.x.dp - vertexViewModel.xPos) * zoomAnimated - vertexViewModel.radius * zoomAnimated,
				center.y.dp - (center.y.dp - vertexViewModel.yPos) * zoomAnimated - vertexViewModel.radius * zoomAnimated
			)
			.background(vertexViewModel.color, CircleShape)
			.aspectRatio(1f)
			.pointerInput(vertexViewModel) {
				detectDragGestures { change, dragAmount ->
					change.consume()
					vertexViewModel.onDrag(dragAmount)
					coroutinePlace.launch {
						ForceDirectedPlacementStrategy(graphViewModel).placeWithoutVertex(
							settings.actualWindowSize.width.toDouble(),
							settings.actualWindowSize.height.toDouble(),
							vertexViewModel
						)
					}
				}
			}
			.pointerInput(vertexViewModel) {
				detectTapGestures {
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
				}
			}
			.onPreviewKeyEvent {
				// isKeyboardPressed = it.key == Key.CtrlLeft || it.key == Key.CtrlRight || it.isCtrlPressed
				isKeyboardPressed.value = it.isShiftPressed
				false
			},
		content = {
			if (vertexViewModel.visibility && (vertexViewModel.radius * zoomAnimated * 2).value > 22) {
				Text(
					text = vertexViewModel.label,
					modifier = Modifier.align(Alignment.Center),
					maxLines = 1,
					fontSize = 10.sp * zoomAnimated
				)
			}
		}
	)
}
