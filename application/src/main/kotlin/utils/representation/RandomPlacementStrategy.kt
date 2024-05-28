package utils.representation

import androidx.compose.ui.unit.dp
import viewmodels.graphs.VertexViewModel
import views.radiusStart
import kotlin.random.Random

class RandomPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		val randomizer = Random(Random.nextLong())

		for (vertex in vertices) {
			vertex.xPos = randomizer.nextDouble(2 * radiusStart.value.toDouble(), width - 2 * radiusStart.value).dp
			vertex.yPos = randomizer.nextDouble(2 * radiusStart.value.toDouble(), height - 2 * radiusStart.value).dp
		}
	}
}
