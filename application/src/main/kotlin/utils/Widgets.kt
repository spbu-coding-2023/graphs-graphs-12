package utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import models.utils.ListWidgetItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListWidget(
	modifier: Modifier = Modifier,
	listItems: List<ListWidgetItem> = listOf(),
	fontSize: TextUnit = 24.sp,
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
					modifier = Modifier.fillMaxWidth().padding(10.dp),
					shape = RoundedCornerShape(10.dp),
					elevation = 5.dp,
					onClick = item.onClick
				) {
					Box(
						contentAlignment = item.alignment
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
									modifier = Modifier.size(52.dp, 52.dp).clip(CircleShape)
								)
							}
							Column(
								modifier = Modifier.padding(horizontal = 15.dp, vertical = 0.dp)
							) {
								Text(
									item.mainText,
									fontSize = fontSize,
									modifier = Modifier.padding(horizontal = 0.dp, vertical = 6.dp)
								)
								if (item.subText != null) {
									Text(
										item.subText,
										fontSize = fontSize / 2
									)
								}
							}
							if (dropDownMenuContext != null) {
								Spacer(Modifier.weight(1f))
								var expanded by remember { mutableStateOf(false) }

								Box {
									IconButton(onClick = { expanded = true }) {
										Icon(Icons.Default.MoreVert, contentDescription = "Показать меню")
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> ComboBox(
	items: Array<T>,
	onItemClick: ((T) -> (Unit))? = null,
	modifier: Modifier = Modifier,
	textAlign: TextAlign = TextAlign.Center,
	textFontSize: TextUnit = 16.sp
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
				textStyle = TextStyle(textAlign = textAlign, fontSize = textFontSize)
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
								fontSize = textFontSize
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

@Composable
fun CustomRadioButton(
	selected: Boolean,
	text: String,
	onClick: () -> (Unit),
	modifier: Modifier = Modifier,
	textAlign: TextAlign = TextAlign.Start,
	textFontSize: TextUnit = 16.sp,
	verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
	reversed: Boolean = false
) {
	Box(
		modifier =  modifier
	) {
		Row(
			verticalAlignment = verticalAlignment,
			modifier = Modifier.align(Alignment.Center)
		) {
			if (!reversed) {
				RadioButton(
					selected = selected,
					onClick = onClick,
					modifier = Modifier.align(Alignment.CenterVertically)
				)
				TextButton(
					onClick = onClick,
					modifier = Modifier
				) {
					Text(text, textAlign = textAlign, fontSize = textFontSize)
				}
			} else {
				TextButton(
					onClick = onClick,
				) {
					Text(text, textAlign = textAlign, fontSize = textFontSize)
				}
				RadioButton(
					selected = selected,
					onClick = onClick,
				)
			}
		}
	}
}
