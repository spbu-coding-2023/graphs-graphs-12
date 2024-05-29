package views.pages

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import themes.*
import viewmodels.pages.SettingsPageViewModel

@Composable
fun SettingsPage(
	viewModel: SettingsPageViewModel,
	isDarkMode: MutableState<Boolean>,
	currentFontSize: MutableState<JetSize>,
	currentStyle: MutableState<JetStyle>,
	currentCornersStyle: MutableState<JetCorners>,
	currentFontFamily: MutableState<JetFontFamily>,
) {
	Column(
		modifier = Modifier
			.padding(4.dp)
			.clip(JetTheme.shapes.cornerStyle)
			.background(JetTheme.colors.primaryBackground)
			.fillMaxSize()
			.verticalScroll(rememberScrollState())
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(70.dp)
				.background(Color.Transparent)
				.align(Alignment.CenterHorizontally),
			horizontalArrangement = Arrangement.Center

		) {
			Text(
				"Settings",
				style = JetTheme.typography.heading,
				modifier = Modifier.align(Alignment.CenterVertically),
				color = JetTheme.colors.secondaryText
			)
		}

		Column(
			modifier = Modifier
				.padding(4.dp)
				.clip(JetTheme.shapes.cornerStyle)
				.background(JetTheme.colors.tertiaryBackground)
		) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(JetTheme.shapes.padding)
			) {
				Text(
					"Dark Theme",
					color = JetTheme.colors.secondaryText,
					style = JetTheme.typography.body,
					modifier = Modifier.align(Alignment.CenterVertically),
				)
				Spacer(modifier = Modifier.weight(0.5f))
				ThemeSwitcher(
					darkTheme = isDarkMode.value,
					onClick = { isDarkMode.value = !isDarkMode.value }
				)
			}

			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)

			MenuItem(
				model = MenuItemModel(
					title = "Font Family",
					currentIndex = when (currentFontFamily.value) {
						JetFontFamily.Default -> 0
						JetFontFamily.Monospace -> 1
						JetFontFamily.Cursive -> 2
						JetFontFamily.Serif -> 3
						JetFontFamily.SansSerif -> 4
					},
					values = listOf(
						"Default",
						"Monospace",
						"Cursive",
						"Serif",
						"SansSerif"
					)
				),
				onItemSelected = {
					when (it) {
						0 -> currentFontFamily.value = JetFontFamily.Default
						1 -> currentFontFamily.value = JetFontFamily.Monospace
						2 -> currentFontFamily.value = JetFontFamily.Cursive
						3 -> currentFontFamily.value = JetFontFamily.Serif
						4 -> currentFontFamily.value = JetFontFamily.SansSerif
						else -> throw NotImplementedError("No valid value for this $it")
					}
				}
			)

			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)

			MenuItem(
				model = MenuItemModel(
					title = "Font Size",
					currentIndex = when (currentFontSize.value) {
						JetSize.Small -> 0
						JetSize.Medium -> 1
						JetSize.Big -> 2
					},
					values = listOf(
						"Small",
						"Medium",
						"Big"
					)
				),
				onItemSelected = {
					when (it) {
						0 -> currentFontSize.value = JetSize.Small
						1 -> currentFontSize.value = JetSize.Medium
						2 -> currentFontSize.value = JetSize.Big
						else -> throw NotImplementedError("No valid value for this $it")
					}
				}
			)

			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)

			MenuItem(
				model = MenuItemModel(
					title = "Corner Style",
					currentIndex = when (currentCornersStyle.value) {
						JetCorners.Rounded -> 0
						JetCorners.Flat -> 1
					},
					values = listOf(
						"Rounded",
						"Flat",
					)
				),
				onItemSelected = {
					when (it) {
						0 -> currentCornersStyle.value = JetCorners.Rounded
						1 -> currentCornersStyle.value = JetCorners.Flat
						else -> throw NotImplementedError("No valid value for this $it")
					}
				}
			)

			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)

			MenuItem(
				model = MenuItemModel(
					title = "Style",
					currentIndex = when (currentStyle.value) {
						JetStyle.Black -> 0
						JetStyle.Blue -> 1
						JetStyle.Orange -> 2
						JetStyle.Purple -> TODO()
						JetStyle.Red -> TODO()
						JetStyle.Green -> TODO()
					},
					values = listOf(
						"Black",
						"Blue",
						"Orange",
					)
				),
				onItemSelected = {
					when (it) {
						0 -> currentStyle.value = JetStyle.Black
						1 -> currentStyle.value = JetStyle.Blue
						2 -> currentStyle.value = JetStyle.Orange
//                        3 -> currentStyle.value = JetSize.Big
						else -> throw NotImplementedError("No valid value for this $it")
					}
				}
			)

		}
	}
}

data class MenuItemModel(
	val title: String,
	val currentIndex: Int = 0,
	val values: List<String>
)

@Composable
internal fun MenuItem(
	model: MenuItemModel,
	onItemSelected: ((Int) -> Unit)? = null
) {
	val isDropdownOpen = remember { mutableStateOf(false) }
	val currentPosition = remember { mutableStateOf(model.currentIndex) }

	Row(
		Modifier
			.padding(JetTheme.shapes.padding)
			.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			modifier = Modifier.padding(end = JetTheme.shapes.padding),
			text = model.title,
			style = JetTheme.typography.body,
			color = JetTheme.colors.secondaryText
		)

		Spacer(modifier = Modifier.weight(0.5f))

		Button(
			modifier = Modifier
				.width(200.dp)
				.height(75.dp)
				.align(Alignment.CenterVertically),
			onClick = {
				isDropdownOpen.value = true
			},
			colors = ButtonDefaults.buttonColors(JetTheme.colors.primaryText),
			shape = JetTheme.shapes.cornerStyle,

		) {
			Text(
				text = model.values[currentPosition.value],
				style = JetTheme.typography.body,
			)
			DropdownMenu(
				modifier = Modifier
					.align(Alignment.CenterVertically)
					.clip(JetTheme.shapes.cornerStyle),
				expanded = isDropdownOpen.value,
				onDismissRequest = { isDropdownOpen.value = false }
			) {
				model.values.forEachIndexed { index, value ->
					DropdownMenuItem(
						onClick = {
							onItemSelected?.invoke(index)
							currentPosition.value = index
							isDropdownOpen.value = false
						},
						text = { Text(model.values[index], style = JetTheme.typography.body) }
					)
				}
			}
		}
	}

}

@Composable
fun ThemeSwitcher(
	darkTheme: Boolean,
	size: Dp = 100.dp,
	iconSize: Dp = size / 3,
	padding: Dp = 10.dp,
	shape: Shape = CircleShape,
	animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
	onClick: () -> Unit
) {
	val offsetState = animateDpAsState(
		targetValue = if (darkTheme) 0.dp else size,
		animationSpec = animationSpec
	)
	val offset by remember { offsetState }

	Box(modifier = Modifier
		.width(size * 2)
		.height(size)
		.clip(shape = shape)
		.clickable { onClick() }
		.background(JetTheme.colors.primaryText)
	) {
		Box(
			modifier = Modifier
				.size(size)
				.offset(x = offset)
				.padding(all = padding)
				.clip(shape = shape)
				.background(JetTheme.colors.secondaryBackground),
		) {}
		Row {
			Box(
				modifier = Modifier.size(size),
				contentAlignment = Alignment.Center
			) {
				Icon(
					modifier = Modifier.size(iconSize),
					imageVector = Icons.Default.Nightlight,
					contentDescription = "Theme Icon",
					tint = if (darkTheme) Color.White
					else JetTheme.colors.tintColor
				)
			}
			Box(
				modifier = Modifier.size(size),
				contentAlignment = Alignment.Center
			) {
				Icon(
					modifier = Modifier.size(iconSize),
					imageVector = Icons.Default.LightMode,
					contentDescription = "Theme Icon",
					tint = if (darkTheme) JetTheme.colors.tintColor
					else Color.White
				)
			}
		}
	}
}
