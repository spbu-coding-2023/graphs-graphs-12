package themes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Custom color for white.
 */
val whiteCustom = Color(254, 249, 231)

/**
 * Color for the start of vertices.
 */
val colorVerticesStart = Color.Gray

/**
 * Color for the start of edges.
 */
val colorEdgesStart = Color.Black

/**
 * Colors configuration for the base Light application theme.
 */
val baseLightPalette = JetColors(
	primaryBackground = Color(218, 228, 205), // greenCustom
	secondaryBackground = Color(175, 218, 252), // blueCustom
	tertiaryBackground = Color(188, 198, 175), // darkGreenCustom
	primaryText = Color(217, 217, 217), // lightGrayCustom
	secondaryText = Color.Black,
	tintColor = Color.Black,
)

/**
 * Colors configuration for the base Dark application theme.
 */
val baseDarkPalette = JetColors(
	primaryBackground = Color(99,102,106), // greyCustom
	secondaryBackground = Color(255,76,43), // orangeCustom
	tertiaryBackground = Color(69,72,76), // darkGreyCustom
	primaryText = Color(175, 218, 252), // blueCustom
	secondaryText = Color.White,
	tintColor = Color.White
)

/**
 * Colors configuration for the Light application theme with [Color.Black] tint color.
 */
val blackLightPalette = baseLightPalette.copy(
	tintColor = Color.Black
)

/**
 * Colors configuration for the Dark application theme with [Color.Black] tint color.
 */
val blackDarkPalette = baseDarkPalette.copy(
	tintColor = Color.Black
)

/**
 * Colors configuration for the Light application theme with [whiteCustom] tint color.
 */
val whiteLightPalette = baseLightPalette.copy(
	tintColor = whiteCustom
)

/**
 * Colors configuration for the Dark application theme with [whiteCustom] tint color.
 */
val whiteDarkPalette = baseDarkPalette.copy(
	tintColor = whiteCustom
)

/**
 * Colors configuration for the Light application theme with custom purple tint color.
 */
val purpleLightPalette = baseLightPalette.copy(
	tintColor = Color(83, 55, 122)
)

/**
 * Colors configuration for the Dark application theme with custom purple tint color.
 */
val purpleDarkPalette = baseDarkPalette.copy(
	tintColor = Color(83, 55, 122)
)

/**
 * Colors configuration for the Light application theme with custom orange tint color.
 */
val orangeLightPalette = baseLightPalette.copy(
	tintColor = Color(255,176,46)
)

/**
 * Colors configuration for the Dark application theme with custom orange tint color.
 */
val orangeDarkPalette = baseDarkPalette.copy(
	tintColor = Color(255,176,46)
)

/**
 * Colors configuration for the Light application theme with custom pink tint color.
 */
val pinkLightPalette = baseLightPalette.copy(
	tintColor = Color(255,20,147)
)

/**
 * Colors configuration for the Dark application theme with custom pink tint color.
 */
val pinkDarkPalette = baseDarkPalette.copy(
	tintColor = Color(255,20,147)
)
