package utils.representation

import androidx.compose.ui.unit.dp
import viewmodels.graphs.VertexViewModel
import views.radiusStart
import kotlin.random.Random

class RandomPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>, vertex: VertexViewModel?) {
		val randomizer = Random(Random.nextLong())

		for (vertex in vertices) {
			vertex.xPos = randomizer.nextDouble(0.0 + radiusStart.value, width - radiusStart.value).dp
			vertex.yPos = randomizer.nextDouble(0.0 + radiusStart.value, height - radiusStart.value).dp
		}
	}
}
