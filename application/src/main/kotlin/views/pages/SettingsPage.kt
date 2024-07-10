package views.pages

import JetSettings
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import themes.JetTheme
import utils.MenuItemModel

/**
 * This is the main composable function for the Settings Page.
 *
 * @param jetSettings an object that stores the current customization parameters
 */
@Composable
fun SettingsPage(jetSettings: JetSettings) {
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
					darkTheme = jetSettings.isDarkMode.value,
					onClick = { jetSettings.isDarkMode.value = !jetSettings.isDarkMode.value }
				)
			}

			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)

			MenuItem(
				model = MenuItemModel(
					title = "Font Family",
					currentIndex = when (jetSettings.currentFontFamily.value) {
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
						0 -> jetSettings.currentFontFamily.value = JetFontFamily.Default
						1 -> jetSettings.currentFontFamily.value = JetFontFamily.Monospace
						2 -> jetSettings.currentFontFamily.value = JetFontFamily.Cursive
						3 -> jetSettings.currentFontFamily.value = JetFontFamily.Serif
						4 -> jetSettings.currentFontFamily.value = JetFontFamily.SansSerif
						else -> throw NotImplementedError("No valid value for this font family $it")
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
					currentIndex = when (jetSettings.currentFontSize.value) {
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
						0 -> jetSettings.currentFontSize.value = JetSize.Small
						1 -> jetSettings.currentFontSize.value = JetSize.Medium
						2 -> jetSettings.currentFontSize.value = JetSize.Big
						else -> throw NotImplementedError("No valid value for this change size $it")
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
					currentIndex = when (jetSettings.currentCornersStyle.value) {
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
						0 -> jetSettings.currentCornersStyle.value = JetCorners.Rounded
						1 -> jetSettings.currentCornersStyle.value = JetCorners.Flat
						else -> throw NotImplementedError("No valid value for this corner style $it")
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
					currentIndex = when (jetSettings.currentStyle.value) {
						JetStyle.Black -> 0
						JetStyle.White -> 1
						JetStyle.Purple -> 2
						JetStyle.Orange -> 3
						JetStyle.Pink -> 4
					},
					values = listOf(
						"Black",
						"White",
						"Purple",
						"Orange",
						"Pink"
					)
				),
				onItemSelected = {
					when (it) {
						0 -> jetSettings.currentStyle.value = JetStyle.Black
						1 -> jetSettings.currentStyle.value = JetStyle.White
						2 -> jetSettings.currentStyle.value = JetStyle.Purple
						3 -> jetSettings.currentStyle.value = JetStyle.Orange
						4 -> jetSettings.currentStyle.value = JetStyle.Pink
						else -> throw NotImplementedError("No valid value for this style $it")
					}
				}
			)
		}
	}
}

// TODO(change visability to private of all funcs that down of it write)

/**
 * This is a composable function for a menu item.
 *
 * @param model the menu item model
 * @param onItemSelected a callback function that will be invoked when an item is selected.
 */
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

/**
 * This is a composable function for a theme switcher.
 *
 * @param darkTheme a boolean indicating the current dark mode status
 * @param size the size of the theme switcher
 * @param iconSize the size of the icons inside the theme switcher
 * @param padding the padding around the theme switcher
 * @param shape the shape of the theme switcher
 * @param animationSpec the animation spec for the theme switcher
 * @param onClick a callback function that will be invoked when the theme switcher is clicked
 */
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

	Box(
		modifier = Modifier
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
					tint = if (darkTheme) { Color.White } else { JetTheme.colors.tintColor }
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
					tint = if (darkTheme) { JetTheme.colors.tintColor } else { Color.White }
				)
			}
		}
	}
}
