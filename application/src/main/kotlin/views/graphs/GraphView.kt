package views.graphs

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.Dispatchers
import themes.*
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import kotlin.math.*

/**
 * This function represents the GraphView composable in Jetpack Compose.
 * It displays a graph with vertices and edges based on the provided ViewModel.
 *
 * @param graphViewModel the ViewModel containing the graph data
 * @param idVerticesInfo a mutable state to hold the selected vertex's ViewModel
 * @param centerBox the initial center position of the graph
 * @param changeCenter a mutable state to indicate if the center position needs to be reset
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun GraphView(
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	centerBox: Offset,
	changeCenter: MutableState<Boolean>
) {
	var zoom by remember { mutableFloatStateOf(0.80f) }
	val zoomAnimated by animateFloatAsState(zoom, tween(200, 0, LinearOutSlowInEasing))
	var center by remember { mutableStateOf(centerBox) }
	if (changeCenter.value) {
		center = centerBox
		zoom = 0.80f
		changeCenter.value = false
	}

	val coroutinePlace = rememberCoroutineScope { Dispatchers.Main }

	Box(Modifier
		.fillMaxSize()
		.clip(JetTheme.shapes.cornerStyle)
		.pointerInput(Unit) {
			detectDragGestures(PointerMatcher.Primary) {
				if (1 / zoom >= 1) {
					center += it * (1 / zoom)
				} else {
					center -= it * (1 / zoom)
				}
			}
		}
		.onPointerEvent(PointerEventType.Scroll) {
			if (it.changes.first().scrollDelta.y > 0) {
				zoom -= zoom / 100 // todo(10 for mouse)
			} else {
				zoom += zoom / 100 // todo(10 for mouse)
			}
		}
	) {
		graphViewModel.edges.forEach { edgeViewModel ->
			EdgeView(edgeViewModel, center, zoomAnimated, graphViewModel)
		}

		val isKeyboardPressed = remember { mutableStateOf(false) }
		graphViewModel.vertices.forEach { vertexViewModel ->
			VertexView(vertexViewModel, center, zoomAnimated, graphViewModel, idVerticesInfo, coroutinePlace, isKeyboardPressed)
		}
	}
}
