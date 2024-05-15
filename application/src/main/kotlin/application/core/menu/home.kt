package application.core.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.core.blueCustom
import application.core.greenCustom
import application.core.roundedCustom

data class StyleBottom(
	val modifier: Modifier,
	val border: BorderStroke,
	val colors: ButtonColors,
)

data class BottomItem(
	val title: String,
	val icon: ImageVector
)

@Composable
@Preview
fun showHome(indexSelectedTab: MutableState<Int>) {
	Row {
		val modifierColumn = Modifier
			.weight(1f)
			.fillMaxHeight()
			.padding(4.dp)
			.clip(roundedCustom)
			.background(
				greenCustom,
				roundedCustom
			)

		Column(modifierColumn) {
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

		Column(modifierColumn) {
			val listBottom = listOf(
				remember { mutableStateOf(false) },
				remember { mutableStateOf(false) },
				remember { mutableStateOf(false) }
			)

			val styleBottom = StyleBottom(
				Modifier
					.fillMaxWidth()
					.padding(20.dp),
				BorderStroke(1.dp, blueCustom),
				ButtonDefaults.buttonColors(Color.White)
			)

			val listItem = listOf(
				BottomItem("Add", Icons.Default.Add),
				BottomItem("Download", Icons.Default.Download),
				BottomItem("Delete", Icons.Default.Delete)
			)

			listItem.forEachIndexed { index, item ->
				Button(
					onClick = { chooseButton(listBottom, index) },
					modifier = styleBottom.modifier,
					border = styleBottom.border,
					colors = styleBottom.colors
				) {
					Icon(
						imageVector = item.icon,
						contentDescription = item.title
					)
					Text(" Graph")
				}
			}

			AnimatedVisibility( // add delay for non-fast switching between buttons
				visible = listBottom[0].value || listBottom[1].value || listBottom[2].value,
				modifier = Modifier
					.padding(20.dp)
					.align(Alignment.CenterHorizontally),
			) {
				Column(
					modifier = Modifier.animateContentSize()
				) {
					if (listBottom[0].value) {
						fillInfoGraph(indexSelectedTab, listBottom[0])
					} else if (listBottom[1].value) {
						downloadGraph(listBottom[1])
					} else {
						deleteGraph(listBottom[2])
					}
				}
			}
		}
	}
}

fun chooseButton(listBottom: List<MutableState<Boolean>>, index: Int) {
	when (index) {
		0 -> {
			listBottom[0].value = !listBottom[0].value
			listBottom[1].value = false
			listBottom[2].value = false
		}
		1 -> {
			listBottom[0].value = false
			listBottom[1].value = !listBottom[1].value
			listBottom[2].value = false
		}
		2 -> {
			listBottom[0].value = false
			listBottom[1].value = false
			listBottom[2].value = !listBottom[2].value
		}
	}
}

@Composable
fun fillInfoGraph(indexSelectedTab: MutableState<Int>, isOpenedCreation: MutableState<Boolean>) {
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
					border = BorderStroke(1.dp, blueCustom),
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
