package utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import models.VertexID
import models.utils.AlgorithmModel
import models.utils.ListWidgetItem
import themes.JetTheme
import themes.sizeBottom
import viewmodels.graphs.GraphViewModel
import viewmodels.pages.GraphPageViewModel

/**
 * [Composable] Widget of List with elements, which implements [ListWidgetItem].
 *
 * @param modifier to change [LazyListWidget] implementation
 * @param listItems elements to List representation
 * @param textStyle style of main text
 * @param itemWidth width of item in [LazyListWidget]
 * @param dropDownMenuContext [Composable] dropdown menu context for list element, if its `null` menu will not visible
 * @param headlineContext [Composable] elements of header of [LazyListWidget]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LazyListWidget(
	modifier: Modifier = Modifier,
	listItems: List<ListWidgetItem> = listOf(),
	textStyle: TextStyle = JetTheme.typography.toolbar,
	itemWidth: Float = 1.0f,
	dropDownMenuContext: @Composable ((ListWidgetItem) -> (Unit))? = null,
	headlineContext: @Composable () -> (Unit),
) {
	Column(
		modifier = modifier
	) {
		headlineContext()
		LazyColumn(
			modifier = Modifier.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			items(listItems.reversed()) { item: ListWidgetItem ->
				if (item.isHidden) return@items // analog of continue
				Card(
					modifier = Modifier
						.fillMaxWidth(itemWidth)
						.padding(10.dp)
						.clip(JetTheme.shapes.cornerStyle),
					elevation = 5.dp,
					onClick = item.onClick,
					backgroundColor = JetTheme.colors.primaryText
				) {
					Box(
						contentAlignment = item.alignment,
						modifier = Modifier.background(Color.Transparent)
					) {
						Row(
							modifier = Modifier.padding(10.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							if (item.icon != null) {
								Image(
									imageVector = item.icon,
									contentDescription = "image-${item.mainText}",
									contentScale = ContentScale.Crop,
									modifier = Modifier.size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
								)
							}
							Column(
								modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp)
							) {
								Text(
									item.mainText,
									style = textStyle,
									modifier = Modifier.padding(horizontal = 0.dp, vertical = 6.dp)
								)
								if (item.subText != null) {
									Text(
										item.subText,
										style = textStyle.copy(fontSize = textStyle.fontSize / 3 * 2)
									)
								}
							}
							if (dropDownMenuContext != null) {
								Spacer(Modifier.weight(1f))
								var expanded by remember { mutableStateOf(false) }

								Box {
									IconButton(onClick = { expanded = true }) {
										Icon(Icons.Default.MoreVert, contentDescription = "Show menu")
									}
									DropdownMenu(
										expanded = expanded,
										onDismissRequest = { expanded = false },
										content = { dropDownMenuContext(item) }
									)
								}
							}
						}
					}
				}
			}
		}
	}
}

/**
 * [Composable] Widget of List with elements, which implements [StaticListWidget].
 *
 * @param modifier to change [StaticListWidget] implementation
 * @param listItems elements to List representation
 * @param textStyle style of main text
 * @param itemWidth width of item in [StaticListWidget]
 * @param headlineContext [Composable] elements of header of [StaticListWidget]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StaticListWidget(
	modifier: Modifier = Modifier,
	listItems: List<ListWidgetItem> = listOf(),
	textStyle: TextStyle = JetTheme.typography.body,
	itemWidth: Float = 1.0f,
	headlineContext: @Composable () -> (Unit),
) {
	Column(modifier = modifier) {
		headlineContext()
		Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
			listItems.forEach { item: ListWidgetItem ->
				if (item.isHidden) return@forEach
				Card(
					modifier = Modifier
						.fillMaxWidth(itemWidth)
						.padding(10.dp)
						.clip(JetTheme.shapes.cornerStyle),
					elevation = 5.dp,
					onClick = item.onClick,
					backgroundColor = JetTheme.colors.primaryText
				) {
					Box(
						contentAlignment = item.alignment,
						modifier = Modifier.background(Color.Transparent)
					) {
						Row(
							modifier = Modifier.padding(10.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							if (item.icon != null) {
								Image(
									imageVector = item.icon,
									contentDescription = "image-${item.mainText}",
									contentScale = ContentScale.Crop,
									modifier = Modifier.size(sizeBottom).clip(JetTheme.shapes.cornerStyle),
								)
							}
							Column(
								modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp),
								horizontalAlignment = Alignment.CenterHorizontally
							) {
								Text(
									item.mainText,
									style = textStyle,
									modifier = Modifier.padding(horizontal = 0.dp, vertical = 6.dp)
								)
								if (item.subText != null) {
									Text(
										item.subText,
										style = textStyle
									)
								}
							}
						}
					}
				}
			}
		}
	}
}

/**
 * [Composable] Widget of [ComboBox] with element.
 *
 * @param T type of items in [ComboBox]
 * @param items collection of elements
 * @param onItemClick lambda method to run it if item choose, if its `null` method not calling
 * @param modifier to change [ComboBox] implementation
 * @param textAlign alignment of text on widgets
 * @param textStyle style of main text
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> ComboBox(
	items: Array<T>,
	onItemClick: ((T) -> (Unit))? = null,
	modifier: Modifier = Modifier,
	textAlign: TextAlign = TextAlign.Center,
	textStyle: TextStyle = JetTheme.typography.toolbar
) {
	var expanded by remember { mutableStateOf(false) }
	var selectedText by remember { mutableStateOf(items.getOrNull(0)?.toString() ?: "No find elements") }

	ExposedDropdownMenuBox(
		modifier = modifier,
		expanded = expanded,
		onExpandedChange = {
			expanded = !expanded
		}
	) {
		Column {
			OutlinedTextField(
				value = selectedText,
				onValueChange = {},
				readOnly = true,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
				colors = TextFieldDefaults.textFieldColors(
					focusedIndicatorColor = JetTheme.colors.secondaryText,
					focusedLabelColor = JetTheme.colors.secondaryText,
					cursorColor = JetTheme.colors.tintColor
				),
				textStyle = TextStyle(
					textAlign = textAlign,
					fontSize = JetTheme.typography.toolbar.fontSize,
					fontFamily = JetTheme.typography.toolbar.fontFamily,
					fontWeight = JetTheme.typography.toolbar.fontWeight,
					color = JetTheme.colors.secondaryText
				),
				modifier = Modifier.fillMaxWidth()
			)
			ExposedDropdownMenu(
				modifier = Modifier.background(JetTheme.colors.primaryBackground),
				expanded = expanded,
				onDismissRequest = { expanded = false },
			) {
				items.forEach { item ->
					DropdownMenuItem(
						content = {
							Text(
								text = item.toString(),
								modifier = Modifier.fillMaxWidth(),
								textAlign = textAlign,
								style = textStyle,
								color = JetTheme.colors.secondaryText
							)
						},
						onClick = {
							if (onItemClick != null) onItemClick(item)
							selectedText = item.toString()
							expanded = false
						}
					)
					TabRowDefaults.Divider(
						thickness = 0.5.dp,
						color = JetTheme.colors.tertiaryBackground
					)
				}
			}
		}
	}
}

/**
 * [Composable] custom Widget of [RadioButton].
 *
 * @param selected start value of state this [RadioButton]
 * @param text message of
 * @param onClick lambda method to run it if item choose
 * @param modifier to change [CustomRadioButton] implementation
 * @param textAlign alignment of text on widgets
 * @param textStyle style of main text
 * @param verticalAlignment alignment of widgets replacement
 * @param reversed set representation mode: `(x) <text>` or `<text> (x)`, based `(x) <text>`
 */
@Composable
fun CustomRadioButton(
	selected: Boolean,
	text: String,
	onClick: () -> (Unit),
	modifier: Modifier = Modifier,
	textAlign: TextAlign = TextAlign.Start,
	textStyle: TextStyle = JetTheme.typography.toolbar,
	verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
	reversed: Boolean = false
) {
	Box(
		modifier = modifier.background(Color.Transparent)
	) {
		Row(
			verticalAlignment = verticalAlignment,
			modifier = Modifier.align(Alignment.Center)
		) {
			if (!reversed) {
				RadioButton(
					selected = selected,
					onClick = onClick,
					modifier = Modifier.align(Alignment.CenterVertically),
					colors = RadioButtonDefaults.colors(JetTheme.colors.secondaryText)
				)
				TextButton(
					onClick = onClick,
					modifier = Modifier,
					colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
				) {
					Text(text, textAlign = textAlign, style = textStyle, color = JetTheme.colors.secondaryText)
				}
			} else {
				TextButton(
					onClick = onClick,
					colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
				) {
					Text(text, textAlign = textAlign, style = textStyle, color = JetTheme.colors.secondaryText)
				}
				RadioButton(
					selected = selected,
					onClick = onClick,
					colors = RadioButtonDefaults.colors(JetTheme.colors.secondaryText)
				)
			}
		}
	}
}

/**
 * [Composable] function to display an algorithm button with dropdown menu.
 *
 * @param graphViewModel the [GraphViewModel] instance to interact with the graph
 * @param algButton the [AlgorithmModel] instance to be displayed
 * @param modifier the [Modifier] to apply to this layout node
 */
@Composable
fun AlgorithmTextButton(
	graphViewModel: GraphViewModel?,
	algButton: AlgorithmModel,
	modifier: Modifier = Modifier
) {
	val coroutineScope = rememberCoroutineScope()
	val expanded = mutableStateOf(false)
	val dropContext = algButton.dropDownMenuContext

	TextButton(
		modifier = modifier,
		onClick = {
			if (dropContext != null) { expanded.value = true } else { algButton.isRun.value = true }
		},
		colors = ButtonDefaults.buttonColors(
			backgroundColor = Color.Transparent
		)
	) {
		Text(algButton.label, color = JetTheme.colors.secondaryText, style = JetTheme.typography.mini)

		if (dropContext != null) {
			Box {
				DropdownMenu(
					modifier = Modifier
						.background(JetTheme.colors.primaryBackground)
						.clip(JetTheme.shapes.cornerStyle),
					expanded = expanded.value,
					onDismissRequest = { expanded.value = false },
					content = {
						Column(horizontalAlignment = Alignment.CenterHorizontally) {
							dropContext(algButton)
						}
					}
				)
			}
		}
	}
	if (graphViewModel != null && algButton.isRun.value) {
		expanded.value = false
		coroutineScope.launch {
			algButton.onRun(
				graphViewModel,
				algButton.inputs.value.stream().map {
					VertexID.vertexIDFromString(it, graphViewModel.vertexType)
				}.toList()
			)
			algButton.isRun.value = false
		}
	}
	Divider(
		thickness = 0.5.dp,
		color = JetTheme.colors.primaryBackground
	)
}

/**
 * [Composable] function to display a button with a specific action.
 *
 * @param graphPageViewModel the [GraphPageViewModel] instance to interact with the graph page
 * @param actionEntry a [Map.Entry] containing the action label and its corresponding function
 * @param modifier the [Modifier] to apply to this layout node
 */
@Composable
fun ActionTextButton(
	graphPageViewModel: GraphPageViewModel,
	actionEntry: Map.Entry<String, (GraphPageViewModel) -> Unit>,
	modifier: Modifier
) {
	TextButton(
		onClick = { actionEntry.value(graphPageViewModel) },
		modifier = modifier,
		colors = ButtonDefaults.buttonColors(
			backgroundColor = Color.Transparent
		)
	) {
		Text(actionEntry.key, color = JetTheme.colors.secondaryText, style = JetTheme.typography.mini)
	}
}
