package application.core.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun showHome(indexSelectedTab: MutableState<Int>) {
	Row {
		Column(
			modifier = Modifier
			.weight(1f)
			.fillMaxHeight()
			.padding(4.dp)
			.clip(RoundedCornerShape(14.dp))
			.background(Color(254, 249, 231), RoundedCornerShape(14.dp))
		) {
			Row(
				modifier = Modifier.padding(20.dp)
			) {
				Icon(
					imageVector = Icons.Default.DateRange,
					contentDescription = "Calendar"
				)
				Text(" Graphs:", fontSize = 20.sp)
			}
		}

		val isOpenedCreation = remember { mutableStateOf(false) }
		val isOpenedLoading = remember { mutableStateOf(false) }
		val isOpenedDeletion = remember { mutableStateOf(false) }

		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxHeight()
				.padding(4.dp)
				.clip(RoundedCornerShape(14.dp))
				.background(Color(254, 249, 231), RoundedCornerShape(14.dp))
		) {
			Button(
				onClick = {
					isOpenedCreation.value = !isOpenedCreation.value
					isOpenedLoading.value = false
					isOpenedDeletion.value = false
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp),
				border = BorderStroke(1.dp, Color(175, 218, 252)),
				colors = ButtonDefaults.buttonColors(Color.White)
			) {
				Icon(
					imageVector = Icons.Default.Add,
					contentDescription = "Add"
				)
				Text(" Graph")
			}
			Button(
				onClick = {
					isOpenedLoading.value = !isOpenedLoading.value
					isOpenedCreation.value = false
					isOpenedDeletion.value = false
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp),
				border = BorderStroke(1.dp, Color(175, 218, 252)),
				colors = ButtonDefaults.buttonColors(Color.White)
			) {
				Icon(
					imageVector = Icons.Default.Download,
					contentDescription = "Load"
				)
				Text(" Graph")
			}
			Button(
				onClick = {
					isOpenedDeletion.value = !isOpenedDeletion.value
					isOpenedCreation.value = false
					isOpenedLoading.value = false
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(20.dp),
				border = BorderStroke(1.dp, Color(175, 218, 252)),
				colors = ButtonDefaults.buttonColors(Color.White)
			) {
				Icon(
					imageVector = Icons.Default.Delete,
					contentDescription = "Delete"
				)
				Text(" Graph")
			}

			AnimatedVisibility( // add delay for non-fast switching between buttons
				visible = isOpenedCreation.value || isOpenedLoading.value || isOpenedDeletion.value,
				modifier = Modifier
					.padding(20.dp)
					.align(Alignment.CenterHorizontally),
			) {
				Column(
					modifier = Modifier.animateContentSize()
				) {
					if (isOpenedCreation.value) {
						fullInfoGraph(indexSelectedTab, isOpenedCreation)
					} else if (isOpenedLoading.value) {
						downloadGraph(isOpenedLoading)
					} else {
						deleteGraph(isOpenedDeletion)
					}
				}
			}
		}
	}
}

@Composable
fun fullInfoGraph(indexSelectedTab: MutableState<Int>, isOpenedCreation: MutableState<Boolean>) {
	var labelGraph by remember { mutableStateOf("") }
	val listVariantGraph = listOf(
		"Undirected unweighted",
		"Directed unweighted",
		"Undirected weighted",
		"Directed weighted"
	)

	OutlinedTextField(
		value = labelGraph,
		onValueChange = { labelGraph = it },
		label = { Text("The graph label") },
	)

	AnimatedVisibility(
		visible = labelGraph != "",
		modifier = Modifier.padding(30.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth()
		) {
			listVariantGraph.forEach { item ->
				Button(
					onClick = {
						indexSelectedTab.value = 1
						isOpenedCreation.value = false
						// creation graph
					},
					modifier = Modifier.fillMaxWidth(),
					border = BorderStroke(1.dp, Color(175, 218, 252)),
					colors = ButtonDefaults.buttonColors(Color.White)
				) {
					Text(item, fontSize = 16.sp)
				}
			}
		}
	}
}

@Composable
fun downloadGraph(isOpenedLoading: MutableState<Boolean>) {
	AnimatedVisibility(
		visible = true,
		modifier = Modifier
			.padding(20.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth()
		) {
			Text("TODO")
		}
	}
}


@Composable
fun deleteGraph(isOpenedDeletion: MutableState<Boolean>) {
	var labelGraph by remember { mutableStateOf("") }

	OutlinedTextField(
		value = labelGraph,
		onValueChange = { labelGraph = it },
		label = { Text("The graph label") },
	)

	AnimatedVisibility(
		visible = labelGraph != "",
		modifier = Modifier
			.padding(20.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth()
		) {
			Text("TODO")
		}
	}
}
