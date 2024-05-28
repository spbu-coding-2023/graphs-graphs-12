package viewmodels.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import models.VertexID
import models.utils.AlgorithmButton
import themes.JetColors
import themes.JetTheme
import utils.representation.AllCenterPlacementStrategy
import utils.representation.CircularPlacementStrategy
import utils.representation.RandomPlacementStrategy
import utils.representation.RepresentationStrategy
import viewmodels.graphs.GraphViewModel
import windowSizeStart

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
			"Strongly Connected Component",
			{ graphViewModel, _ -> graphViewModel.parseTarjanStrongConnectivityAlgorithm() },
			{ algButton ->
				if (graphViewModel?.graph?.isDirected == true) {
					algButton.isRun.value = true
				}
			}
		),

		AlgorithmButton(
			"Bridges",
			{ graphViewModel, _ -> graphViewModel.parseTarjanBridgeFinding() },
			{ algButton ->
				if (graphViewModel?.graph?.isDirected == false) {
					algButton.isRun.value = true
				}
			}
		),

		AlgorithmButton(
			"Cycles",
			{ graphViewModel, input -> graphViewModel.parseCyclesSearchAlgorithm(input.first()) },
			{ algButton ->
				var vertex by remember { mutableStateOf("") }
				OutlinedTextField(
					value = vertex,
					onValueChange = { vertex = it },
					label = { Text("Vertex") },
					singleLine = true,
					textStyle = JetTheme.typography.mini,
					colors = TextFieldDefaults.textFieldColors(
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					modifier = Modifier.padding(4.dp)
				)
				TextButton(
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
					onClick = {
						if (graphViewModel!!.graph.containsVertex(
								VertexID.vertexIDFromString(
									vertex,
									graphViewModel!!.vertexType
								)
							)
						) {
							algButton.inputs.value = listOf(vertex)
							algButton.isRun.value = true
						}
					}
				) {
					Text("Run it!")
				}
			}
		),

		AlgorithmButton(
			"Minimum Spanning Tree (Kruskal)",
			{ graphViewModel, _ -> graphViewModel.parseKruskalAlgorithm() },
			{ algButton ->
				if (graphViewModel?.graph?.isDirected == false) {
					algButton.isRun.value = true
				}
			}
		),

		AlgorithmButton(
			"Minimum Spanning Tree (Prim)",
			{ graphViewModel, _ -> graphViewModel.parsePrimAlgorithm() },
			{ algButton ->
				if (graphViewModel?.graph?.isDirected == false) {
					algButton.isRun.value = true
				}
			}
		),

		AlgorithmButton(
			"Shortest Path (Bellman)",
			{ graphViewModel, input -> graphViewModel.parseBellmanFordAlgorithm(input.first(), input.last()) },
			{ algButton ->
				var source by remember { mutableStateOf("") }
				var target by remember { mutableStateOf("") }
				OutlinedTextField(
					value = source,
					onValueChange = { source = it },
					label = { Text("Source") },
					singleLine = true,
					textStyle = JetTheme.typography.mini,
					colors = TextFieldDefaults.textFieldColors(
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					modifier = Modifier.padding(4.dp)
				)
				OutlinedTextField(
					value = target,
					onValueChange = { target = it },
					label = { Text("Target") },
					singleLine = true,
					textStyle = JetTheme.typography.mini,
					colors = TextFieldDefaults.textFieldColors(
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					modifier = Modifier.padding(4.dp)
				)
				TextButton(
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
					onClick = {
						if (graphViewModel!!.graph.containsVertex(
								VertexID.vertexIDFromString(
									source,
									graphViewModel!!.vertexType
								)
							) &&
							graphViewModel!!.graph.containsVertex(
								VertexID.vertexIDFromString(
									target,
									graphViewModel!!.vertexType
								)
							)
						) {
							algButton.inputs.value = listOf(source, target)
							algButton.isRun.value = true
						}
					}
				) {
					Text("Run it!")
				}
			}
		),

		AlgorithmButton(
			"Shortest Path (Dijkstra)",
			{ graphViewModel, input -> graphViewModel.parseDijkstraAlgorithm(input.first(), input.last()) },
			{ algButton ->
				var source by remember { mutableStateOf("") }
				var target by remember { mutableStateOf("") }
				OutlinedTextField(
					value = source,
					onValueChange = { source = it },
					label = { Text("Source") },
					singleLine = true,
					textStyle = JetTheme.typography.mini,
					colors = TextFieldDefaults.textFieldColors(
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					modifier = Modifier.padding(4.dp)
				)
				OutlinedTextField(
					value = target,
					onValueChange = { target = it },
					label = { Text("Target") },
					singleLine = true,
					textStyle = JetTheme.typography.mini,
					colors = TextFieldDefaults.textFieldColors(
						focusedIndicatorColor = JetTheme.colors.secondaryText,
						focusedLabelColor = JetTheme.colors.secondaryText,
						cursorColor = JetTheme.colors.tintColor
					),
					modifier = Modifier.padding(4.dp)
				)
				TextButton(
					colors = ButtonDefaults.buttonColors(
						backgroundColor = JetTheme.colors.tertiaryBackground,
						contentColor = JetTheme.colors.secondaryText,
						disabledContentColor = JetTheme.colors.secondaryText,
						disabledBackgroundColor = JetTheme.colors.tertiaryBackground
					),
					onClick = {
						if (graphViewModel!!.graph.containsVertex(
								VertexID.vertexIDFromString(
									source,
									graphViewModel!!.vertexType
								)
							) &&
							graphViewModel!!.graph.containsVertex(
								VertexID.vertexIDFromString(
									target,
									graphViewModel!!.vertexType
								)
							)
						) {
							algButton.inputs.value = listOf(source, target)
							algButton.isRun.value = true
						}
					}
				) {
					Text("Run it!")
				}
			}
		),
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
		representationStrategy.place(
			windowSizeStart.first.toDouble(),
			windowSizeStart.second.toDouble(),
			model.vertices
		)
	}
}
