package views.graphs

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import themes.*
import utils.representation.ForceDirectedPlacementStrategy
import utils.representation.checkAndGetFirst
import utils.representation.checkAndGetSecond
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import windowSizeStart
import kotlin.math.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun GraphView(
	graphViewModel: GraphViewModel,
	idVerticesInfo: MutableState<VertexViewModel?>,
	centerBox: Offset,
	changeCenter: MutableState<Boolean>
) {
	var zoom by remember { mutableStateOf(0.99f) }
	val zoomAnimated by animateFloatAsState(zoom, tween(200, 0, LinearOutSlowInEasing))
	var center by remember { mutableStateOf(centerBox) }
	if (changeCenter.value) {
		center = centerBox
		zoom = 0.99f
		changeCenter.value = false
	}

	Surface(Modifier
//		.clip(JetTheme.shapes.cornerStyle)
		.pointerInput(Unit) {
			detectDragGestures(PointerMatcher.Primary) {
				center += it * (1 / zoom) // todo(в какой-то момент начинает отрицать)
//				println(center)
			}
		}
		.onPointerEvent(PointerEventType.Scroll) {
			println(it)
			if (it.changes.first().scrollDelta.y > 0) {
				zoom -= zoom / 100 // todo(10)
			} else {
				zoom += zoom / 100 // todo(10)
			}
		}
	) {
		Canvas(Modifier
			.fillMaxSize()
			.background(JetTheme.colors.secondaryBackground)
//			.zIndex(-1f)
		) {
			graphViewModel.edges.forEach { edgeViewModel ->
				Line(
					this,
					center,
					Offset(
						center.x + (edgeViewModel.source.xPos - center.x.dp).toPx() * zoomAnimated,
						center.y + (edgeViewModel.source.yPos - center.y.dp).toPx() * zoomAnimated
					),
					Offset(
						center.x + (edgeViewModel.target.xPos - center.x.dp).toPx() * zoomAnimated,
						center.y + (edgeViewModel.target.yPos - center.y.dp).toPx() * zoomAnimated
					),
					edgeViewModel.target.radius,
					20f,
					color = edgeViewModel.color,
					strokeWidth = edgeViewModel.width,
					isArrow = graphViewModel.graph.isDirected
				)

//				drawLine(
//					start = Offset(
//						center.x + center.x + (edgeViewModel.source.xPos - center.x.dp).toPx() * zoomAnimated,
//						center.y + center.y + (edgeViewModel.source.yPos - center.y.dp).toPx() * zoomAnimated
//					),
//					end = Offset(
//						center.x + center.x + (edgeViewModel.target.xPos - center.x.dp).toPx() * zoomAnimated,
//						center.y + center.y + (edgeViewModel.target.yPos - center.y.dp).toPx() * zoomAnimated,
//					),
//					color = Color.Black
//				)
			}
		}

		graphViewModel.edges.forEach { edgeViewModel ->
			if (edgeViewModel.visibility) {
				Box(
					modifier = Modifier
						.size(edgeViewModel.source.radius * 2)
						.offset(
							center.x.dp + (edgeViewModel.source.xPos - center.x.dp) * zoomAnimated -
								(edgeViewModel.source.xPos - edgeViewModel.target.xPos) * zoomAnimated / 2 - edgeViewModel.source.radius,
							center.y.dp + (edgeViewModel.source.yPos - center.y.dp) * zoomAnimated -
								(edgeViewModel.source.yPos - edgeViewModel.target.yPos) * zoomAnimated / 2 - edgeViewModel.source.radius,
						)
						.background(Color(175, 218, 252, 210), CircleShape), // todo(add to jetpack)
					contentAlignment = Alignment.Center
				) {
					Text(text = edgeViewModel.label)
				}
			}
		}

		var isCtrlPressed by remember { mutableStateOf(false) }
		graphViewModel.vertices.forEach { vertexViewModel ->
			IconButton(
				onClick = {
					if (isCtrlPressed) {
						if (idVerticesInfo.value != null) {
							graphViewModel.addEdge(
								idVerticesInfo.value!!.id,
								vertexViewModel.id
							)
						}
						isCtrlPressed = !isCtrlPressed
					} else {
						if (idVerticesInfo.value != vertexViewModel) {
							if (idVerticesInfo.value != null) {
								idVerticesInfo.value!!.color = colorVerticesStart
							}
							idVerticesInfo.value = vertexViewModel
							idVerticesInfo.value!!.color = whiteCustom
						} else {
							idVerticesInfo.value!!.color = colorVerticesStart
							idVerticesInfo.value = null
						}
					}
				},
				modifier = Modifier
					.size(vertexViewModel.radius * 2, vertexViewModel.radius * 2)
					.offset(
						center.x.dp + (vertexViewModel.xPos - center.x.dp) * zoomAnimated - vertexViewModel.radius,
						center.y.dp + (vertexViewModel.yPos - center.y.dp) * zoomAnimated - vertexViewModel.radius
					)
					.background(
						color = vertexViewModel.color,
						shape = CircleShape
					)
					.pointerInput(vertexViewModel) {
						detectDragGestures { change, dragAmount ->
							change.consume()
							vertexViewModel.onDrag(dragAmount)
							ForceDirectedPlacementStrategy(graphViewModel).placeWithoutVertex(
								windowSizeStart.second.toDouble(),
								windowSizeStart.second.toDouble(),
								vertexViewModel
							)
						}
					}
					.onPreviewKeyEvent {
						isCtrlPressed = it.isCtrlPressed // todo(on mac don't work)
						false
					}
			) {
				if (vertexViewModel.visibility) {
					Text(
						modifier = Modifier,
						text = vertexViewModel.label,
					)
				}
			}
		}
	}
}

fun Line(
	scope: DrawScope,
	center: Offset,
	start: Offset,
	end: Offset,
	radiusEnd: Dp,
	arrowLength: Float,
	color: Color = colorEdgesStart,
	strokeWidth: Float = 1f,
	isArrow: Boolean = false
) {
	scope.drawLine(
		start = center + start,
		end = center + end,
		color = color,
		strokeWidth = strokeWidth
	)
	if (isArrow) {
		val alpha = Math.PI / 10
		val radius = scope.toPx(radiusEnd)
		val startEndVector = Offset(end.x - start.x, end.y - start.y)
		val normalizeSE = startEndVector / sqrt(startEndVector.x.pow(2) + startEndVector.y.pow(2))
		val A = end - normalizeSE * radius
		val deltaA = end - normalizeSE * (radius + (arrowLength * cos(alpha).toFloat()))
		val normalizeOrt = Offset(-normalizeSE.y, normalizeSE.x)
		val B = deltaA + normalizeOrt * arrowLength * sin(alpha).toFloat()
		val C = deltaA - normalizeOrt * arrowLength * sin(alpha).toFloat()

		scope.drawLine(
			start = center + A,
			end = center + B,
			color = color,
			strokeWidth = strokeWidth
		)
		scope.drawLine(
			start = center + A,
			end = center + C,
			color = color,
			strokeWidth = strokeWidth
		)
	}
}

fun DrawScope.toPx(dp: Dp): Float {
	return dp.toPx()
}
