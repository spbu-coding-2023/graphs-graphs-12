package models.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import models.VertexID
import viewmodels.graphs.GraphViewModel

data class AlgorithmButton(
	val label: String,
	val onRun: (GraphViewModel, List<VertexID>) -> Unit,
	val dropDownMenuContext: @Composable ((AlgorithmButton) -> Unit)? = null,
	val isRun: MutableState<Boolean> = mutableStateOf(false),
	val inputs: MutableState<List<String>> = mutableStateOf(listOf())
) {
	override fun toString(): String {
		return "AlgorithmButton(label='$label')"
	}
}
