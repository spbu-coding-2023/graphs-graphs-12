package viewmodels.pages

import androidx.compose.runtime.mutableStateOf
import utils.representation.RandomPlacementStrategy
import utils.representation.RepresentationStrategy
import viewmodels.graphs.GraphViewModel

class GraphPageViewModel {
	private val _representationStrategy = mutableStateOf<RepresentationStrategy>(RandomPlacementStrategy())
	private val _graph = mutableStateOf<GraphViewModel?>(null)
	var representationStrategy: RepresentationStrategy
		get() = _representationStrategy.value
		set(newStartegy) {
			_representationStrategy.value = newStartegy
			updateGraphRepresentation()
		}
	var graphViewModel: GraphViewModel?
		get() = _graph.value
		set(newModel) {
			_graph.value = newModel
			updateGraphRepresentation()
		}


	private fun updateGraphRepresentation() {
		// TODO(run it by coroutine scope)
		// TODO(chage values of `width` and `height`)
		val model = _graph.value ?: return
		representationStrategy.place(1000.0, 700.0, model.vertices)
	}
}