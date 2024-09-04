package integrations

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import models.JetSettings
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle

@OptIn(ExperimentalTestApi::class)
fun runJetSettingsComposeUiTest(
	context: @Composable (JetSettings) -> (Unit),
	assertions: (ComposeUiTest) -> Unit
) = runComposeUiTest {
	setContent {
		val isDarkModeValue = isSystemInDarkTheme()
		val jetSettings = JetSettings(
			currentStyle = remember { mutableStateOf(JetStyle.Black) },
			currentFontSize = remember { mutableStateOf(JetSize.Medium) },
			currentCornersStyle = remember { mutableStateOf(JetCorners.Rounded) },
			currentFontFamily = remember { mutableStateOf(JetFontFamily.Default) },
			isDarkMode = remember { mutableStateOf(isDarkModeValue) }
		)
		context(jetSettings)
	}
	assertions(this)
}
