package views.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import themes.JetTheme
import utils.representation.ForceDirectedPlacementStrategy
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import viewmodels.graphs.colorVertexStart
import views.pages.listZoom
import views.whiteCustom
import windowSizeStart
import kotlin.math.*

@Composable
fun GraphView(
	graphViewModel: GraphViewModel,
	abilityZoom: Boolean,
	indexListZoom: Int,
	idVerticesInfo: MutableState<VertexViewModel?>
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
			}
		}
		graphViewModel.vertices.forEach { vertexViewModel ->
			IconButton(
				onClick = {
					if (idVerticesInfo.value == null) {
						idVerticesInfo.value = vertexViewModel
						idVerticesInfo.value!!.color = whiteCustom
					} else {
						if (idVerticesInfo.value != vertexViewModel) {
							graphViewModel.addEdge(
								idVerticesInfo.value!!.id, // todo(!!)
								vertexViewModel.id
							)
						}
						idVerticesInfo.value!!.color = colorVertexStart
						idVerticesInfo.value = null
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
							ForceDirectedPlacementStrategy(graphViewModel).placeWithoutVertex(
								windowSizeStart.second.toDouble(),
								windowSizeStart.second.toDouble(),
								vertexViewModel
							)
						}
					}
			)
			{
				if (vertexViewModel.visibility) {
					Text(
						modifier = Modifier,
						text = vertexViewModel.label,
					)
				} else {
					Text("")
				}
			}
		}
	}
}
