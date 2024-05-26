package utils.representation

import androidx.compose.ui.unit.dp
import viewmodels.graphs.VertexViewModel

// TODO(create logger implementation)
class AllCenterPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		if (width < 0.0 || height < 0.0) return

		val xCenter = width / 2
		val yCenter = height / 2
		for (vertex in vertices) {
			vertex.xPos = xCenter.dp
			vertex.yPos = yCenter.dp
		}
	}
}
