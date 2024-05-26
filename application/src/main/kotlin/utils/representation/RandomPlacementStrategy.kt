package utils.representation

import androidx.compose.ui.unit.dp
import viewmodels.graphs.VertexViewModel
import views.radiusStart
import kotlin.random.Random

class RandomPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		val randomizer = Random(Random.nextLong())

		for (vertex in vertices) {
			vertex.xPos = randomizer.nextDouble(radiusStart.value.toDouble(), width - radiusStart.value).dp
			vertex.yPos = randomizer.nextDouble(radiusStart.value.toDouble(), height - radiusStart.value).dp
		}
	}
}
