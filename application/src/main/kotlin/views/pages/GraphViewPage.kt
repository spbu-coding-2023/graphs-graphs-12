package views.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AspectRatio
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import graphs_lab.core.edges.WeightedEdge
import models.VertexID
import themes.JetTheme
import viewmodels.graphs.GraphViewModel
import viewmodels.graphs.VertexViewModel
import viewmodels.pages.GraphPageViewModel
import views.*
import views.graphs.GraphView

val listZoom = listOf(
	0.1f,
	0.5f,
	1f,
	1.5f,
	2f,
)

@Composable
fun GraphViewPage(graphPageViewModel: GraphPageViewModel) {
	val graphViewModel = graphPageViewModel.graphViewModel

	val abilityZoom = remember { mutableStateOf(false) }

	val isOpenedContextGraph = remember { mutableStateOf(true) }
	val isOpenedContextEdge = remember { mutableStateOf(false) }
	val idVerticesInfo = remember { mutableStateOf<VertexViewModel?>(null) }
	LaunchedEffect(idVerticesInfo.value) {
		if (idVerticesInfo.value == null) {
			isOpenedContextEdge.value = false
		} else {
			isOpenedContextGraph.value = false
			isOpenedContextEdge.value = true
		}
	}

	val indexListZoom = remember { mutableIntStateOf(2) }
	val modifierButtons = Modifier
		.size(sizeBottom)
		.padding(4.dp)
		.clip(RoundedCornerShape(25.dp))
		.background(JetTheme.colors.primaryBackground, RoundedCornerShape(25.dp))

	if (graphViewModel != null) {
		Surface(Modifier
			.padding(4.dp)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.secondaryBackground, JetTheme.shapes.cornerStyle)
		) {
			GraphView(graphViewModel = graphViewModel, abilityZoom.value, indexListZoom.intValue, idVerticesInfo)
			Box(Modifier.fillMaxSize()) {
				if (abilityZoom.value) ColumnZoomButtons(indexListZoom, Modifier.align(Alignment.TopEnd), modifierButtons)
				ColumnChangeAbility(abilityZoom, Modifier.align(Alignment.CenterEnd), modifierButtons)
				ColumnInfoGraph(graphViewModel, Modifier.align(Alignment.BottomEnd), modifierButtons,
					isOpenedContextGraph, isOpenedContextEdge, idVerticesInfo)
			}
		}
	}
}

@Composable
fun ColumnZoomButtons( indexListZoom: MutableIntState, modifierBox: Modifier, modifierButtons: Modifier) {
	Column(modifierBox) {
		IconButton(
			onClick = {
				if (indexListZoom.intValue == 4) indexListZoom.intValue = 2
				else indexListZoom.intValue++
			},
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = "Zoom in",
				)
			}
		)
		IconButton(
			onClick = {
				if (indexListZoom.intValue == 0) indexListZoom.intValue = 2
				else indexListZoom.intValue--
			},
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = Icons.Default.Remove,
					contentDescription = "Zoom out"
				)
			}
		)
	}
}

@Composable
fun ColumnChangeAbility(abilityZoom: MutableState<Boolean>, modifierBox: Modifier, modifierButtons: Modifier) {
	Column(modifierBox) {
		IconButton(
			onClick = { abilityZoom.value = false },
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = if (abilityZoom.value) Icons.Filled.Timeline else Icons.Outlined.Timeline,
					contentDescription = "Ability move",
				)
			}
		)
		IconButton(
			onClick = { abilityZoom.value = true },
			modifier = modifierButtons,
			content = {
				Icon(
					imageVector = if (abilityZoom.value) Icons.Filled.AspectRatio else Icons.Outlined.AspectRatio,
					contentDescription = "Ability zoom"
				)
			}
		)
	}
}

@Composable
fun ColumnInfoGraph(
	graphViewModel: GraphViewModel,
	modifierBox: Modifier,
	modifierButtons: Modifier,
	isOpenedContextGraph: MutableState<Boolean>,
	isOpenedContextEdge: MutableState<Boolean>,
	idVerticesInfo: MutableState<VertexViewModel?>
) {
	Column(modifierBox.padding(4.dp)) {
		IconButton(
			onClick = { isOpenedContextGraph.value = !isOpenedContextGraph.value },
			modifier = modifierButtons
				.align(Alignment.End)
				.pointerHoverIcon(PointerIcon.Hand),
			content = {
				Icon(
					imageVector = if (isOpenedContextGraph.value) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
					contentDescription = "Show menu"
				)
			}
		)

		AnimatedVisibility(
			visible = isOpenedContextGraph.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle) // fix double greenCustom
		) {
			showAddItem(graphViewModel)
		}

		AnimatedVisibility(
			visible = isOpenedContextEdge.value,
			modifier = Modifier
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.primaryBackground, JetTheme.shapes.cornerStyle) // fix double greenCustom
		) {
			showEditItem(graphViewModel, idVerticesInfo)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun showAddItem(graphViewModel: GraphViewModel) {
	val idFastAdd = remember { mutableIntStateOf(graphViewModel.vertices.size + 1) }

	val statePager = rememberPagerState { 2 }

	var isShownWeigh by remember { mutableStateOf(false) }
	LaunchedEffect(isShownWeigh) {
		graphViewModel.edges.forEach {
			it.visibility = isShownWeigh
		}
	}
	var isShownId by remember { mutableStateOf(true) }
	LaunchedEffect(isShownId) {
		graphViewModel.vertices.forEach {
			it.visibility = isShownId
		}
	}

	val addingVertex = {
		graphViewModel.addVertex(VertexID(checkAndGet(graphViewModel, idFastAdd), graphViewModel.vertexType))
	}
	val addingEdge = {
		val vertexSource = VertexID(checkAndGet(graphViewModel, idFastAdd), graphViewModel.vertexType)
		graphViewModel.addVertex(vertexSource)
		val vertexTarget = VertexID(checkAndGet(graphViewModel, idFastAdd), graphViewModel.vertexType)
		graphViewModel.addVertex(vertexTarget)
		graphViewModel.addEdge(vertexSource, vertexTarget, 1.0)
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(2.dp)
			.background(JetTheme.colors.primaryBackground, RoundedCornerShape(10.dp)),
		horizontalArrangement = Arrangement.spacedBy(1.dp),
	) {
		Column(Modifier.weight(1f)) {
			Text("Graph name: ${graphViewModel.graph.label}", fontSize = 20.sp, modifier = Modifier.padding(4.dp)) // todo(test)
			Row {
				Column(Modifier.weight(1f)) {
					if (graphViewModel.graph.isDirected) Text("Directed", fontSize = 14.sp, modifier = Modifier.padding(4.dp))
					if (!graphViewModel.isUnweighted) {
						Text("Weighted", fontSize = 14.sp, modifier = Modifier.padding(4.dp))
						Row {
							Text("Show weight", fontSize = 14.sp, modifier = Modifier.padding(4.dp))
							Checkbox(
								checked = isShownWeigh,
								onCheckedChange = { isShownWeigh = !isShownWeigh },
								modifier = Modifier.size(10.dp)
							)
						}
					}
					Row {
						Text("Show id", fontSize = 14.sp, modifier = Modifier.padding(4.dp))
						Checkbox(
							checked = isShownId,
							onCheckedChange = { isShownId = !isShownId },
							modifier = Modifier.size(10.dp)
						)
					}
				}
				Column(Modifier.weight(2f)) {
					BoxAddItem("Vertices:", graphViewModel.vertices.size, addingVertex)
					BoxAddItem("Edges:", graphViewModel.countEdges, addingEdge)
				}

			}
		}
		Column(Modifier.weight(1f).height(152.dp)) { // todo(change size in dark theme or big size)
			HorizontalPager( // todo(how to fix double show pagers?)
				modifier = Modifier
					.fillMaxSize()
					.padding(2.dp)
					.clip(JetTheme.shapes.cornerStyle)
					.background(whiteCustom, JetTheme.shapes.cornerStyle),
				state = statePager,
				userScrollEnabled = true
			) { index ->
				when (index) {
					0 -> showMenuVertex(graphViewModel)
					1 -> showMenuEdge(graphViewModel)
				}
			}
		}
	}
}

@Composable
fun BoxAddItem(text: String, count: Int, action: () -> Unit) {
	Box(modifier = Modifier
		.fillMaxWidth()
		.padding(2.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(Color.White, JetTheme.shapes.cornerStyle)
	) {
		Text(text, fontSize = 16.sp, modifier = Modifier.padding(15.dp).align(Alignment.CenterStart))
		Row(Modifier
			.padding(4.dp)
			.align(Alignment.TopEnd)
		) {
			Text("$count", fontSize = 16.sp, modifier = Modifier.padding(15.dp))
			IconButton(
				onClick = action,
				enabled = true,
				modifier = Modifier.width(50.dp),
				content = {
					Icon(
						imageVector = Icons.Default.Add,
						contentDescription = "Add item",
					)
				}
			)
		}
	}
}

fun checkAndGet(graphViewModel: GraphViewModel, idFastAdd: MutableIntState): Int {
	while (graphViewModel.graph.idVertices.contains(VertexID(idFastAdd.intValue, graphViewModel.vertexType))) {
		idFastAdd.intValue++
	}
	return idFastAdd.intValue
}

@Composable
fun showEditItem(graphViewModel: GraphViewModel, idVerticesInfo: MutableState<VertexViewModel?>) {
	val setEdges by remember { mutableStateOf(graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id)) }

	val removingVertexSource: () -> Unit = {
		graphViewModel.removeVertex(idVerticesInfo.value!!.id) // todo(!!)
		idVerticesInfo.value = null
	}
	val removingVertexTarget: (WeightedEdge<VertexID>) -> Unit = {
		graphViewModel.removeVertex(it.idTarget)
	}
	val removingEdge: (WeightedEdge<VertexID>) -> Unit = {
		graphViewModel.removeEdge(it.idSource, it.idTarget)
	}

	val stringVertex: (WeightedEdge<VertexID>) -> String = {
		"Vertex: ${it.idTarget.valueToString()}"
	}
	val stringEdge: (WeightedEdge<VertexID>) -> String = {
		"Edge: ${it.idSource.valueToString()} -> ${it.idTarget.valueToString()}"
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(4.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp)
	) {
		Column(Modifier.weight(1f)) {
			if (idVerticesInfo.value != null) { // todo(test)
				Text("Vertex: ${idVerticesInfo.value!!.id.valueToString()}") // todo(!!)
				Text("Count edges: ${graphViewModel.graph.vertexEdges(idVerticesInfo.value!!.id).size}")
				ButtonCustom(removingVertexSource, "Delete", Modifier)
			}
		}
		ColumnBoxEditItem(
			setEdges,
			idVerticesInfo,
			Modifier.weight(2f),
			removingVertexTarget,
			"Doesn't have adjacent vertices",
			stringVertex
		)
		ColumnBoxEditItem(
			setEdges,
			idVerticesInfo,
			Modifier.weight(2f),
			removingEdge,
			"Doesn't have edges",
			stringEdge
		)
	}
}

@Composable
fun ColumnBoxEditItem(
	setEdges: Set<WeightedEdge<VertexID>>,
	idVerticesInfo: MutableState<VertexViewModel?>,
	modifierRow: Modifier,
	action: (WeightedEdge<VertexID>) -> Unit,
	message: String,
	string: (WeightedEdge<VertexID>) -> String
) {
	Column(modifierRow
		.heightIn(0.dp, 180.dp)
		.clip(JetTheme.shapes.cornerStyle)
		.background(whiteCustom, JetTheme.shapes.cornerStyle)
		.verticalScroll(rememberScrollState())
	) {
		if (setEdges.isEmpty()) {
			Text(message, Modifier.padding(4.dp))
		}
		setEdges.forEach {
			BoxEditItem(it, idVerticesInfo, action, string)
		}
	}
}

@Composable
fun BoxEditItem(
	edge: WeightedEdge<VertexID>,
	idVerticesInfo: MutableState<VertexViewModel?>,
	action: (WeightedEdge<VertexID>) -> Unit,
	string: (WeightedEdge<VertexID>) -> String
) {
	Box(Modifier.fillMaxWidth()) {
		Text(string(edge), Modifier.padding(4.dp).align(Alignment.CenterStart))
		IconButton(
			onClick = {
				action(edge)
				idVerticesInfo.value = null // todo(maybe can to better)
					  },
			enabled = true,
			modifier = Modifier
				.width(sizeBottom)
				.padding(4.dp)
				.align(Alignment.TopEnd),
			content = {
				Icon(
					imageVector = Icons.Default.Delete,
					contentDescription = "Remove item"
				)
			}
		)
	}
}

@Composable
fun showMenuVertex(graphViewModel: GraphViewModel) {
	val idVertex = remember { mutableStateOf("") }

	val creationVertex = {
		if (idVertex.value != "") {
			graphViewModel.addVertex(VertexID(idVertex.value, graphViewModel.vertexType))
		}
	}

	Column {
		Row(
			modifier = Modifier.padding(4.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			TextFieldItem(idVertex, "Vertex", Modifier.weight(1f))
			Spacer(Modifier.weight(1f))
		}
		ButtonCustom(creationVertex, "Create", Modifier.padding(4.dp))
	}
}

@Composable
fun showMenuEdge(graphViewModel: GraphViewModel) {
	val idVertexSource = remember { mutableStateOf("") }
	val idVertexTarget = remember { mutableStateOf("") }
	val weight = remember { mutableStateOf("") }

	val creationEdge = {
		if (idVertexSource.value != "" && idVertexTarget.value != "" && idVertexSource.value != idVertexTarget.value
			&& weight.value.toDoubleOrNull() != null) {
			graphViewModel.addEdge(
				VertexID(idVertexSource.value, graphViewModel.vertexType),
				VertexID(idVertexTarget.value, graphViewModel.vertexType),
				weight.value.toDouble()
			)
		}
	}

	Column {
		Row(
			modifier = Modifier.padding(4.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp)
		) {
			TextFieldItem(idVertexSource, "Source vertex", Modifier.weight(1f))
			TextFieldItem(idVertexTarget, "Target vertex", Modifier.weight(1f))
		}

		Row(Modifier.padding(4.dp)) {
			Box(Modifier.weight(1f)) {
				Spacer(Modifier.fillMaxWidth())
				ButtonCustom(creationEdge, "Create", Modifier.align(Alignment.CenterStart))
			}
			TextFieldItem(weight, "Weight", Modifier.weight(1f))
		}
	}
}

@Composable
fun ButtonCustom(action: () -> Unit, message: String, modifier: Modifier) {
	Button(
		onClick = action,
		modifier = modifier,
		colors = ButtonDefaults.buttonColors(Color.White)
	) {
		Text(message)
	}
}

@Composable
fun TextFieldItem(string: MutableState<String>, label: String, modifier: Modifier) {
	OutlinedTextField(
		value = string.value,
		onValueChange = { string.value = it },
		label = { Text(label) },
		modifier = modifier
	)
}
