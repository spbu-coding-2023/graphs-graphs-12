package models.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import models.VertexID
import viewmodels.graphs.GraphViewModel

/**
 * Represents a button for running a specific algorithm in the graph visualization application.
 *
 * @property label the label displayed on the button
 * @property onRun a function that takes a [GraphViewModel] and a list of [VertexID] as parameters and performs the algorithm
 * @property dropDownMenuContext an optional composable function that provides a context menu for the button
 * @property isRun a mutable state indicating whether the algorithm is currently running
 * @property inputs a mutable state containing the input parameters for the algorithm
 * @property directionalRequirement indicates whether the algorithm requires a directional graph
 * @property nonDirectionalRequirement indicates whether the algorithm requires a non-directional graph
 */
data class AlgorithmModel(
	val label: String,
	val onRun: (GraphViewModel, List<VertexID>) -> Unit,
	val dropDownMenuContext: @Composable ((AlgorithmModel) -> Unit)? = null,
	val isRun: MutableState<Boolean> = mutableStateOf(false),
	val inputs: MutableState<List<String>> = mutableStateOf(listOf()),
	val directionalRequirement: Boolean = false,
	val nonDirectionalRequirement: Boolean = false
) {
	/**
	 * Overrides the default toString method to provide a string representation of the [AlgorithmModel].
	 *
	 * @return a string representation of the [AlgorithmModel] in the format "AlgorithmButton(label='[label]')"
	 */
	override fun toString(): String {
		return "AlgorithmButton(label='$label')"
	}
}
