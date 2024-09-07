package views.pages

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import models.JetSettings
import mu.KotlinLogging
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import themes.JetTheme
import utils.MenuItemModel

private val logger = KotlinLogging.logger("SettingsPage")

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
			.testTag("settings-page")
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
					currentIndex = jetSettings.currentFontFamily.value.ordinal,
					values = JetFontFamily.entries
				),
				onItemSelected = { order ->
					val newFontFamily = JetFontFamily.entries.getOrElse(order) {
						JetFontFamily.Default
					}
					jetSettings.currentFontFamily.value = newFontFamily
					logger.info { "Change font family to $newFontFamily" }
				}
			)
			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)
			MenuItem(
				model = MenuItemModel(
					title = "Font Size",
					currentIndex = jetSettings.currentFontSize.value.ordinal,
					values = JetSize.entries
				),
				onItemSelected = { order ->
					val newFontSize = JetSize.entries.getOrElse(order) {
						JetSize.Medium
					}
					jetSettings.currentFontSize.value = newFontSize
					logger.info { "Change font size to $newFontSize" }
				}
			)
			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)
			MenuItem(
				model = MenuItemModel(
					title = "Corner Style",
					currentIndex = jetSettings.currentCornersStyle.value.ordinal,
					values = JetCorners.entries
				),
				onItemSelected = { order ->
					val newCornerStyle = JetCorners.entries.getOrElse(order) {
						JetCorners.Rounded
					}
					jetSettings.currentCornersStyle.value = newCornerStyle
					logger.info { "Change corner style to $newCornerStyle" }
				}
			)
			Divider(
				thickness = 0.5.dp,
				color = JetTheme.colors.primaryBackground
			)
			MenuItem(
				model = MenuItemModel(
					title = "Style",
					currentIndex = jetSettings.currentStyle.value.ordinal,
					values = JetStyle.entries
				),
				onItemSelected =
				{ order ->
					val newAppStyle = JetStyle.entries.getOrElse(order) {
						JetStyle.Black
					}
					jetSettings.currentStyle.value = newAppStyle
					logger.info { "Change app style to $newAppStyle" }
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
internal fun <T> MenuItem(
	model: MenuItemModel<T>,
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
				text = model.values[currentPosition.value].toString(),
				style = JetTheme.typography.body,
			)
			DropdownMenu(
				modifier = Modifier
					.align(Alignment.CenterVertically)
					.clip(JetTheme.shapes.cornerStyle),
				expanded = isDropdownOpen.value,
				onDismissRequest = { isDropdownOpen.value = false }
			) {
				model.values.forEachIndexed { index, _ ->
					DropdownMenuItem(
						onClick = {
							onItemSelected?.invoke(index)
							currentPosition.value = index
							isDropdownOpen.value = false
						},
						text = { Text(model.values[index].toString(), style = JetTheme.typography.body) }
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
			.clickable {
				onClick()
				logger.info { "Switch theme to ${if (darkTheme) "DARK" else "LIGHT"}." }
			}
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
