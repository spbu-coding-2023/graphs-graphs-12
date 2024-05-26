package utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import models.utils.ListWidgetItem
import themes.JetTheme

/**
 * [Composable] Widget of List with elements, which implements [ListWidgetItem].
 *
 * @param modifier to change [ListWidget] implementation
 * @param listItems elements to List representation
 * @param textStyle style of main text
 * @param dropDownMenuContext [Composable] dropdown menu context for list element, if its `null` menu will not visible
 * @param headlineContext [Composable] elements of header of [ListWidget]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListWidget(
	modifier: Modifier = Modifier,
	listItems: List<ListWidgetItem> = listOf(),
	textStyle: TextStyle = JetTheme.typography.body,
	dropDownMenuContext: @Composable ((ListWidgetItem) -> (Unit))? = null,
	headlineContext: @Composable () -> (Unit),
) {
	Column(
		modifier = modifier
	) {
		headlineContext()
		LazyColumn {
			items(listItems) { item: ListWidgetItem ->
				if (item.isHidden) return@items // analog of continue
				Card(
					modifier = Modifier
						.fillMaxWidth()
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
									modifier = Modifier.size(52.dp, 52.dp).clip(JetTheme.shapes.cornerStyle),
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
										style = textStyle
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
 * [Composable] Widget of [ComboBox] with element.
 *
 * @param T type of items in [ComboBox]
 * @param items collection of elements
 * @param onItemClick lambda method to run it if item choose, if its `null` method not calling
 * @param modifier to change [ComboBox] implementation
 * @param textAlign alignment of text on widgets
 * @param textFontSize size of text font
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
			TextField(
				value = selectedText,
				onValueChange = {},
				readOnly = true,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
				modifier = Modifier.fillMaxWidth(),
				textStyle = TextStyle(
					textAlign = textAlign,
					fontSize = JetTheme.typography.toolbar.fontSize,
					fontFamily = JetTheme.typography.toolbar.fontFamily,
					fontWeight = JetTheme.typography.toolbar.fontWeight
				)
			)
			ExposedDropdownMenu(
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
								style = textStyle
							)
						},
						onClick = {
							if (onItemClick != null) onItemClick(item)
							selectedText = item.toString()
							expanded = false
						}
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
 * @param textFontSize size of text font
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
					colors = RadioButtonDefaults.colors(JetTheme.colors.tintColor)
				)
				TextButton(
					onClick = onClick,
					modifier = Modifier,
				) {
					Text(text, textAlign = textAlign, style = textStyle, color = Color.Black)
				}
			} else {
				TextButton(
					onClick = onClick,
				) {
					Text(text, textAlign = textAlign, style = textStyle, color = Color.Black)
				}
				RadioButton(
					selected = selected,
					onClick = onClick,
					colors = RadioButtonDefaults.colors(JetTheme.colors.tintColor)
				)
			}
		}
	}
}
