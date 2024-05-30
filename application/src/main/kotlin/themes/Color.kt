package themes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val whiteCustom = Color(254, 249, 231)
val colorVerticesStart = Color.Gray
val colorEdgesStart = Color.Black

val baseLightPalette = JetColors(
	primaryBackground = Color(218, 228, 205), // greenCustom
	secondaryBackground = Color(175, 218, 252), // blueCustom
	tertiaryBackground = Color(188, 198, 175), // darkGreenCustom
	primaryText = Color(217, 217, 217), // lightGrayCustom
	secondaryText = Color.Black,
	tintColor = Color.Black,
)

val baseDarkPalette = JetColors(
	primaryBackground = Color(99,102,106), // greyCustom
	secondaryBackground = Color(255,76,43), // orangeCustom
	tertiaryBackground = Color(69,72,76), // darkGreyCustom
	primaryText = Color(175, 218, 252), // blueCustom
	secondaryText = Color.White,
	tintColor = Color.White
)

val blackLightPalette = baseLightPalette.copy(
	tintColor = Color.Black
)

val blackDarkPalette = baseDarkPalette.copy(
	tintColor = Color.Black
)

val whiteLightPalette = baseLightPalette.copy(
	tintColor = whiteCustom
)

val whiteDarkPalette = baseDarkPalette.copy(
	tintColor = whiteCustom
)

val purpleLightPalette = baseLightPalette.copy(
	tintColor = Color(83, 55, 122)
)

val purpleDarkPalette = baseDarkPalette.copy(
	tintColor = Color(83, 55, 122)
)

val orangeLightPalette = baseLightPalette.copy(
	tintColor = Color(255,176,46)
)

val orangeDarkPalette = baseDarkPalette.copy(
	tintColor = Color(255,176,46)
)

val pinkLightPalette = baseLightPalette.copy(
	tintColor = Color(255,20,147)
)

val pinkDarkPalette = baseDarkPalette.copy(
	tintColor = Color(255,20,147)
)
