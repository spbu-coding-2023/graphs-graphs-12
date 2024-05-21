package utils.representation

import androidx.compose.ui.unit.dp
import viewmodels.graphs.VertexViewModel
import kotlin.random.Random

class RandomPlacementStrategy : RepresentationStrategy {
	override fun place(width: Double, height: Double, vertices: Collection<VertexViewModel>) {
		val randomizer = Random(Random.nextLong())

		for (vertex in vertices) {
			vertex.xPos = randomizer.nextDouble(0.0, width).dp
			vertex.yPos = randomizer.nextDouble(0.0, height).dp
		}
	}
}
