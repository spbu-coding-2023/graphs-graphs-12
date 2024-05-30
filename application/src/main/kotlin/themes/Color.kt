package themes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val whiteCustom = Color(254, 249, 231) // todo(replace)
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

val blueLightPalette = baseLightPalette.copy(
	tintColor = Color(8,37,103)
)

val blueDarkPalette = baseDarkPalette.copy(
	tintColor = Color(8,37,103)
)

val orangeLightPalette = baseLightPalette.copy(
	tintColor = Color(255,76,43)
)

val orangeDarkPalette = baseDarkPalette.copy(
	tintColor = Color(255,76,43)
)
