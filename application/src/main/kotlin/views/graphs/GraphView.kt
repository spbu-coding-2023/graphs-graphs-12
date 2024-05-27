package views.graphs

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import themes.JetTheme
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import views.pages.listZoom
import kotlin.math.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GraphView(
	graphViewModel: GraphViewModel,
	abilityZoom: Boolean,
	indexListZoom: Int,
	idVerticesInfo: MutableState<VertexViewModel?>,
	isAltPressed: MutableState<Boolean> = mutableStateOf(false)
) {
	// todo(maybe add edgeview and vertexview)
	if (abilityZoom) {
		Canvas(Modifier
			.fillMaxSize()
			.background(JetTheme.colors.secondaryBackground)
			.scale(listZoom[indexListZoom])
			.pointerInput(Unit) {
				detectDragGestures { change, dragAmount ->
					change.consume()
					graphViewModel.vertices.forEach { it.onDrag(dragAmount) } // todo(перетаскивается только в центре, мб реально просто у каждого изменять scale)
				}
			}
		) {
			graphViewModel.edges.forEach { edgeViewModel ->
				drawLine(
					start = Offset(edgeViewModel.source.xPos.toPx(), edgeViewModel.source.yPos.toPx()),
					end = Offset(edgeViewModel.target.xPos.toPx(), edgeViewModel.target.yPos.toPx()),
					color = edgeViewModel.color,
					strokeWidth = edgeViewModel.size
				)
			}
			graphViewModel.vertices.forEach { vertexViewModel ->
				drawCircle(
					center = Offset(vertexViewModel.xPos.toPx(), vertexViewModel.yPos.toPx()),
					radius = vertexViewModel.radius.toPx(),
					color = vertexViewModel.color
				)
			}
		}
	} else {
		Canvas(Modifier
			.fillMaxSize()
			.background(JetTheme.colors.secondaryBackground)
			.zIndex(-1f)
		) {
			graphViewModel.edges.forEach { edgeViewModel ->
				drawLine(
					start = Offset(edgeViewModel.source.xPos.toPx(), edgeViewModel.source.yPos.toPx()),
					end = Offset(edgeViewModel.target.xPos.toPx(), edgeViewModel.target.yPos.toPx()),
					color = edgeViewModel.color,
					strokeWidth = edgeViewModel.size
				)
//				drawLine(
//					start = Offset(edgeViewModel.target.xPos.toPx(), edgeViewModel.target.yPos.toPx()),
//					end = getArrowOne(
//						Offset(edgeViewModel.source.xPos.toPx(), edgeViewModel.source.yPos.toPx()),
//						Offset(edgeViewModel.target.xPos.toPx(), edgeViewModel.target.yPos.toPx())
//					),
//					color = edgeViewModel.color,
//					strokeWidth = edgeViewModel.size
//				)
//				drawLine(
//					start = Offset(edgeViewModel.target.xPos.toPx(), edgeViewModel.target.yPos.toPx()),
//					end = getArrowTwo(
//						Offset(edgeViewModel.source.xPos.toPx(), edgeViewModel.source.yPos.toPx()),
//						Offset(edgeViewModel.target.xPos.toPx(), edgeViewModel.target.yPos.toPx())
//					),
//					color = edgeViewModel.color,
//					strokeWidth = edgeViewModel.size
//				)
			}
		}
		var isCtrlPressed by remember { mutableStateOf(false) }
		graphViewModel.vertices.forEach { vertexViewModel ->
			IconButton(
				onClick = {
					if (isCtrlPressed) {
						println("on ctrl double tap")
						if (idVerticesInfo.value != null) {
							graphViewModel.addEdge(
								idVerticesInfo.value!!.id, // todo(!!)
								vertexViewModel.id
							)
						}
						isCtrlPressed = !isCtrlPressed
					} else {
						println("on click")
						if (idVerticesInfo.value != vertexViewModel) {
							idVerticesInfo.value = vertexViewModel
						} else {
							idVerticesInfo.value = null
						}
					}
				},
				modifier = Modifier
					.size(vertexViewModel.radius * 2, vertexViewModel.radius * 2)
					.offset(vertexViewModel.xPos - vertexViewModel.radius, vertexViewModel.yPos - vertexViewModel.radius)
					.background(
						color = vertexViewModel.color,
						shape = CircleShape
					)
					.pointerInput(vertexViewModel) {
						detectDragGestures { change, dragAmount ->
							change.consume()
							vertexViewModel.onDrag(dragAmount)
//							ForceDirectedPlacementStrategy(graphViewModel).placeVertex(displayMax.toDouble(), displayMax.toDouble(), vertexViewModel)
						}
					}.onPreviewKeyEvent {
						isCtrlPressed = it.isCtrlPressed
						isAltPressed.value = it.isAltPressed
						false
					}
			) {
				Text(
					modifier = Modifier,
					text = vertexViewModel.label,
				)
			}

		}
	}
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GraphViewTest(
	graphViewModel: GraphViewModel,
	abilityZoom: Boolean,
	indexListZoom: Int,
	idVerticesInfo: MutableState<VertexViewModel?>
) {
	var zoom by remember { mutableFloatStateOf(1f) }

	var offset by remember { mutableStateOf(Offset.Zero) }

	BoxWithConstraints(Modifier.fillMaxSize()) {
		Canvas(
			modifier = Modifier
				.fillMaxSize()
				.onPointerEvent(PointerEventType.Scroll) {
					println(this)
					println(it)
				}
				.onPointerEvent(PointerEventType.Move) {
				}
		) {

		}
	}
}

fun getCoefficient(pointStart: Offset, pointEnd: Offset): Float { // y = kx + b
	var k = 0f
	if (pointEnd.x - pointStart.x != 0f) {
		k = (pointEnd.y - pointStart.y) / (pointEnd.x - pointStart.x)
	}
	return k
}

fun getArrowOne(pointStart: Offset, pointEnd: Offset): Offset { // y = kx + b
	val k = getCoefficient(pointStart, pointEnd)
	val kNew = (k - tan(30f)) / (1 + k * tan(30f))
	val b = pointEnd.y - kNew * pointEnd.x
	val length = 5f
	val x = root(
		1 + kNew.pow(2),
		-2 * pointEnd.x - 2 * pointEnd.y * kNew + 2 * kNew * b,
		- length.pow(2) + pointEnd.x.pow(2) + pointEnd.y.pow(2) - 2 * pointEnd.y * b + b.pow(2)
	)
	val y = kNew * x + b
	return Offset(x, y)
}

fun getArrowTwo(pointStart: Offset, pointEnd: Offset): Offset { // y = kx + b
	val k = getCoefficient(pointStart, pointEnd)
	val kNew = (k + tan(60f)) / (1 - k * tan(60f))
	val b = pointEnd.y - kNew * pointEnd.x
	val length = 5f
	val x = root(
		1 + kNew.pow(2),
		-2 * pointEnd.x - 2 * pointEnd.y * kNew + 2 * kNew * b,
		- length.pow(2) + pointEnd.x.pow(2) + pointEnd.y.pow(2) - 2 * pointEnd.y * b + b.pow(2)
	)
	val y = kNew * x + b
	return Offset(x, y)
}

fun root(a: Float, b:Float, c: Float): Float {
	val discriminant = b.pow(2) - 4 * a * c
	println(discriminant)
	return (-b + sqrt(discriminant)) / 2 * a
}
