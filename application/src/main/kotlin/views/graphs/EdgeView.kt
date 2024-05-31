package views.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import viewmodels.graphs.EdgeViewModel
import viewmodels.graphs.GraphViewModel
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun EdgeView(edgeViewModel: EdgeViewModel, center: Offset, zoomAnimated: Float, graphViewModel: GraphViewModel) {
	Canvas(Modifier) {
		val startX = center.x.dp + (edgeViewModel.source.xPos - center.x.dp) * zoomAnimated
		val startY = center.y.dp + (edgeViewModel.source.yPos - center.y.dp) * zoomAnimated
		val endX = center.x.dp + (edgeViewModel.target.xPos - center.x.dp) * zoomAnimated
		val endY = center.y.dp + (edgeViewModel.target.yPos - center.y.dp) * zoomAnimated

		drawLine(
			start = Offset(startX.toPx(), startY.toPx()),
			end = Offset(endX.toPx(), endY.toPx()),
			color = edgeViewModel.color,
			strokeWidth = edgeViewModel.width
		)

		if (graphViewModel.graph.isDirected) {
			val angle = Math.PI / 10
			val lengthArrow = 20f * zoomAnimated
			val radius = edgeViewModel.target.radius * zoomAnimated

			val vectorSEX = endX - startX
			val vectorSEY = endY - startY

			val lengthSE = sqrt(vectorSEX.toPx().pow(2) + vectorSEY.toPx().pow(2))
			val normalizeSEX = vectorSEX / lengthSE
			val normalizeSEY = vectorSEY / lengthSE

			val AX = endX - normalizeSEX * radius.toPx()
			val AY = endY - normalizeSEY * radius.toPx()
			val deltaAX = endX - normalizeSEX * (radius.toPx() + lengthArrow * cos(angle).toFloat())
			val deltaAY = endY - normalizeSEY * (radius.toPx() + lengthArrow * cos(angle).toFloat())

			val normalizeOrtX = -normalizeSEY
			val normalizeOrtY = normalizeSEX

			val BX = deltaAX + normalizeOrtX * lengthArrow * sin(angle).toFloat()
			val BY = deltaAY + normalizeOrtY * lengthArrow * sin(angle).toFloat()
			val CX = deltaAX - normalizeOrtX * lengthArrow * sin(angle).toFloat()
			val CY = deltaAY - normalizeOrtY * lengthArrow * sin(angle).toFloat()

			drawLine(
				start = Offset(AX.toPx(), AY.toPx()),
				end = Offset(BX.toPx(), BY.toPx()),
				color = edgeViewModel.color,
				strokeWidth = edgeViewModel.width
			)
			drawLine(
				start = Offset(AX.toPx(), AY.toPx()),
				end = Offset(CX.toPx(), CY.toPx()),
				color = edgeViewModel.color,
				strokeWidth = edgeViewModel.width
			)
		}
	}
	if (edgeViewModel.visibility) {
		Box(
			modifier = Modifier
				.size(edgeViewModel.source.radius * zoomAnimated * 2)
				.offset(
					center.x.dp + (edgeViewModel.source.xPos - center.x.dp) * zoomAnimated -
						(edgeViewModel.source.xPos - edgeViewModel.target.xPos) * zoomAnimated / 2 - edgeViewModel.source.radius * zoomAnimated,
					center.y.dp + (edgeViewModel.source.yPos - center.y.dp) * zoomAnimated -
						(edgeViewModel.source.yPos - edgeViewModel.target.yPos) * zoomAnimated / 2 - edgeViewModel.source.radius * zoomAnimated,
				)
				.background(Color(175, 218, 252, 210), CircleShape),
			contentAlignment = Alignment.Center
		) {
			Text(text = edgeViewModel.label, maxLines = 1)
		}
	}
}
