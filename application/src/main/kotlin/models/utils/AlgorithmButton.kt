package models.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import models.VertexID
import viewmodels.graphs.GraphViewModel

/**
 * Represents a button for running a specific algorithm in the graph visualization application.
 *
 * @param label The label displayed on the button.
 * @param onRun A function that takes a [GraphViewModel] and a list of [VertexID] as parameters and performs the algorithm.
 * @param dropDownMenuContext An optional composable function that provides a context menu for the button.
 * @param isRun A mutable state indicating whether the algorithm is currently running.
 * @param inputs A mutable state containing the input parameters for the algorithm.
 * @param directionalRequirement Indicates whether the algorithm requires a directional graph.
 * @param nonDirectionalRequirement Indicates whether the algorithm requires a non-directional graph.
 *
 * @return An instance of [AlgorithmButton].
 */
data class AlgorithmButton(
	val label: String,
	val onRun: (GraphViewModel, List<VertexID>) -> Unit,
	val dropDownMenuContext: @Composable ((AlgorithmButton) -> Unit)? = null,
	val isRun: MutableState<Boolean> = mutableStateOf(false),
	val inputs: MutableState<List<String>> = mutableStateOf(listOf()),
	val directionalRequirement: Boolean = false,
	val nonDirectionalRequirement: Boolean = false
) {
	/**
	 * Overrides the default toString method to provide a string representation of the [AlgorithmButton].
	 *
	 * @return A string representation of the [AlgorithmButton] in the format "AlgorithmButton(label='[label]')".
	 */
	override fun toString(): String {
		return "AlgorithmButton(label='$label')"
	}
}
