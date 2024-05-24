package views.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.zIndex
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import views.pages.listZoom

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
			.scale(listZoom[indexListZoom])
			.pointerInput(Unit) {
				detectDragGestures { change, dragAmount ->
					change.consume()
					graphViewModel.vertices.forEach { it.onDrag(dragAmount) } // todo(перетаскивается только в центре, мб реально просто у каждого изменять scale)
				}
			}
		) {
			graphViewModel.vertices.forEach { vertexViewModel ->
				drawCircle(
					center = Offset(vertexViewModel.xPos.toPx(), vertexViewModel.yPos.toPx()),
					radius = vertexViewModel.radius.toPx(),
					color = Color(56, 90, 83)
				)
			}
			graphViewModel.edges.forEach { edgeViewModel ->
				drawLine(
					start = Offset(
						edgeViewModel.source.xPos.toPx(),
						edgeViewModel.source.yPos.toPx()
					),
					end = Offset(
						edgeViewModel.target.xPos.toPx(),
						edgeViewModel.target.yPos.toPx()
					),
					color = Color(56, 90, 83)
				)
			}
		}
	} else {
		graphViewModel.vertices.forEach { vertexViewModel ->
			IconButton(
				onClick = {
					if (idVerticesInfo.value == null) {
						idVerticesInfo.value = vertexViewModel
					} else {
						if (idVerticesInfo.value != vertexViewModel) {
							graphViewModel.addEdge(
								idVerticesInfo.value!!.id,
								vertexViewModel.id
							)
						}
						idVerticesInfo.value = null
					}
				},
				modifier = Modifier
					.size(vertexViewModel.radius * 2, vertexViewModel.radius * 2)
					.offset(vertexViewModel.xPos, vertexViewModel.yPos)
					.background(
						color = vertexViewModel.color,
						shape = CircleShape
					)
					.pointerInput(vertexViewModel) {
						detectDragGestures { change, dragAmount ->
							change.consume()
							vertexViewModel.onDrag(dragAmount)
						}
					}
			) {
				Text(
					modifier = Modifier,
					text = vertexViewModel.label,
				)
			}
		}
		Canvas(
			Modifier
				.fillMaxSize()
				.zIndex(-1f)
		) {
			graphViewModel.edges.forEach { edgeViewModel ->
				drawLine(
					start = Offset(
						edgeViewModel.source.xPos.toPx() + edgeViewModel.source.radius.toPx(),
						edgeViewModel.source.yPos.toPx() + edgeViewModel.source.radius.toPx()
					),
					end = Offset(
						edgeViewModel.target.xPos.toPx() + edgeViewModel.target.radius.toPx(),
						edgeViewModel.target.yPos.toPx() + edgeViewModel.target.radius.toPx()
					),
					color = Color(56, 90, 83)
				)
			}
		}
	}
}
