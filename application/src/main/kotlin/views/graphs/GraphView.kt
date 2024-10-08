package views.graphs

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Dispatchers
import models.SettingsModel
import themes.JetTheme
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel

/**
 * This function represents the GraphView composable in Jetpack Compose.
 * It displays a graph with vertices and edges based on the provided ViewModel.
 *
 * @param graphViewModel the ViewModel containing the graph data
 * @param idVerticesInfo a mutable state to hold the selected vertex's ViewModel
 * @param centerBox the initial center position of the graph
 * @param changeCenter a mutable state to indicate if the center position needs to be reset
 * @param settings application settings to depends on actual window size
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun GraphView(
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	centerBox: Offset,
	changeCenter: MutableState<Boolean>,
	settings: SettingsModel
) {
	var zoom by remember { mutableFloatStateOf(0.8f) }
	val zoomAnimated by animateFloatAsState(zoom, tween(200, 0, LinearOutSlowInEasing))
	var center by remember { mutableStateOf(centerBox.div(2f)) }
	if (changeCenter.value) {
		center = centerBox
		zoom = 0.8f
		changeCenter.value = false
	}

	val coroutinePlace = rememberCoroutineScope { Dispatchers.Main }

	Box(
		modifier = Modifier
			.fillMaxSize()
			.clip(JetTheme.shapes.cornerStyle)
			.pointerInput(Unit) {
				detectDragGestures(PointerMatcher.Primary) {
					if (zoom <= 1) {
						center += it * (1 / zoom)
					} else {
						center -= it * (1 / zoom)
					}
				}
			}
			.onPointerEvent(PointerEventType.Scroll) {
				if (it.changes.first().scrollDelta.y > 0 && zoom > 0.2) {
					zoom -= zoom / 100 // todo(10 for mouse)
				} else if (zoom < 5) {
					zoom += zoom / 100 // todo(10 for mouse)
				}
			}
	) {
		graphViewModel.edges.forEach { edgeViewModel ->
			EdgeView(edgeViewModel, center, zoomAnimated, graphViewModel)
		}

		val isKeyboardPressed = remember { mutableStateOf(false) }
		graphViewModel.vertices.forEach { vertexViewModel ->
			VertexView(
				vertexViewModel,
				center,
				zoomAnimated,
				graphViewModel,
				idVerticesInfo,
				coroutinePlace,
				isKeyboardPressed,
				settings
			)
		}
	}
}
