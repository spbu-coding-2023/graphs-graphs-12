package viewmodels.pages

import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import models.utils.AlgorithmButton
import utils.representation.AllCenterPlacementStrategy
import utils.representation.CircularPlacementStrategy
import utils.representation.RandomPlacementStrategy
import utils.representation.RepresentationStrategy
import viewmodels.graphs.GraphViewModel
import views.displayMax

class GraphPageViewModel {
	private val _representationStrategy = mutableStateOf<RepresentationStrategy>(RandomPlacementStrategy())
	private val _graph = mutableStateOf<GraphViewModel?>(null)
	var representationStrategy: RepresentationStrategy
		get() = _representationStrategy.value
		set(newStrategy) {
			_representationStrategy.value = newStrategy
			updateGraphRepresentation()
		}
	var graphViewModel: GraphViewModel?
		get() = _graph.value
		set(newModel) {
			_graph.value = newModel
			updateGraphRepresentation()
		}
	val algorithms = setOf<AlgorithmButton>(
		AlgorithmButton(
			"Finding SP (Dijkstra)",
			{ graphViewModel, input -> graphViewModel.parseDijkstraAlgorithm(input.first(), input.last()) },
			{ algButton ->
				var source by remember { mutableStateOf("") }
				var target by remember { mutableStateOf("") }
				TextField(source, onValueChange = { source = it })
				TextField(target, onValueChange = { target = it })
				TextButton(
					onClick = {
						algButton.inputs.value = listOf(source, target)
						algButton.isRun.value = true
					}
				) {
					Text("Run it!")
				}
			}
		),
		AlgorithmButton(
			"Finding MST",
			{ graphViewModel, input -> println(input) }
		)
	)
	val mapRepresentationModes: Map<String, (GraphPageViewModel) -> Unit> = mapOf(
		"Central" to { graphPageViewModel -> graphPageViewModel.representationStrategy = AllCenterPlacementStrategy() },
		"Random" to { graphPageViewModel -> graphPageViewModel.representationStrategy = RandomPlacementStrategy() },
		"Circular" to { graphPageViewModel -> graphPageViewModel.representationStrategy = CircularPlacementStrategy() },
	)


	private fun updateGraphRepresentation() {
		// TODO(run it by coroutine scope)
		// TODO(change values of `width` and `height`)
		val model = _graph.value ?: return
		representationStrategy.place(displayMax.toDouble(), displayMax.toDouble(), model.vertices, null)
	}
}
