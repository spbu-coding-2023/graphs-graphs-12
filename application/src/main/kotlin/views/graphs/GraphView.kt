package views.graphs

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
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
				Line(
					this,
					edgeViewModel.source,
					edgeViewModel.target,
					20f,
					color = edgeViewModel.color,
					strokeWidth = edgeViewModel.size,
					isArrow = graphViewModel.graph.isDirected
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
				Line(
					this,
					edgeViewModel.source,
					edgeViewModel.target,
					20f,
					color = edgeViewModel.color,
					strokeWidth = edgeViewModel.size,
					isArrow = graphViewModel.graph.isDirected
				)
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
					.offset(
						vertexViewModel.xPos - vertexViewModel.radius,
						vertexViewModel.yPos - vertexViewModel.radius)
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

fun Line(
	scope: DrawScope,
	source: VertexViewModel,
	target: VertexViewModel,
	arrowLength: Float,
	color: Color = Color.Black,
	strokeWidth: Float = 1f,
	isArrow: Boolean = false
) {
	val start = Offset(scope.toPx(source.xPos), scope.toPx(source.yPos))
	val end = Offset(scope.toPx(target.xPos), scope.toPx(target.yPos))
	scope.drawLine(
		start = start,
		end = end,
		color = color,
		strokeWidth = strokeWidth
	)
	if (isArrow) {
		val alpha = Math.PI / 10
		val radius = scope.toPx(target.radius)
		val startEndVector = Offset(end.x - start.x, end.y - start.y)
		val normalizeSE = startEndVector / sqrt(startEndVector.x.pow(2) + startEndVector.y.pow(2))
		val A = end - normalizeSE * radius
		val deltaA = end - normalizeSE * (radius + (arrowLength * cos(alpha).toFloat()))
		val normalizeOrt = Offset(-normalizeSE.y, normalizeSE.x)
		val B = deltaA + normalizeOrt * arrowLength * sin(alpha).toFloat()
		val C = deltaA - normalizeOrt * arrowLength * sin(alpha).toFloat()

		scope.drawLine(
			start = A,
			end = B,
			color = color,
			strokeWidth = strokeWidth
		)
		scope.drawLine(
			start = A,
			end = C,
			color = color,
			strokeWidth = strokeWidth
		)
	}
}

fun DrawScope.toPx(dp: Dp): Float {
	return dp.toPx()
}
