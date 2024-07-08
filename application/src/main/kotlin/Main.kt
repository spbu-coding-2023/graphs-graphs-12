import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import models.SettingsModel
import themes.JetCorners
import themes.JetFontFamily
import themes.JetSize
import themes.JetStyle
import views.MainScreen
import viewmodels.MainScreenViewModel
import java.awt.Dimension
import java.io.File

val windowSizeStart = Pair(1000f, 700f)

data class JetSettings(
	val currentStyle: MutableState<JetStyle>,
	val currentFontSize: MutableState<JetSize>,
	val currentCornersStyle: MutableState<JetCorners>,
	val currentFontFamily: MutableState<JetFontFamily>,
	val isDarkMode: MutableState<Boolean>
)

/**
 * Application entry point.
 */
fun main() {
	application {
		val settings: SettingsModel = SettingsModel.loadSettings()

		val isDarkModeValue = isSystemInDarkTheme()
		val jetSettings = JetSettings(
			currentStyle = remember { mutableStateOf(JetStyle.Black) },
			currentFontSize = remember { mutableStateOf(JetSize.Medium) },
			currentCornersStyle = remember { mutableStateOf(JetCorners.Rounded) },
			currentFontFamily = remember { mutableStateOf(JetFontFamily.Default) },
			isDarkMode = remember { mutableStateOf(isDarkModeValue) }
		)

		val file = File("../settings")
		findOrCreateSettings(file)

		val converters = arrayOf<(String) -> Any>(
			{ string -> JetStyle.valueOf(string) },
			{ string -> JetSize.valueOf(string) },
			{ string -> JetCorners.valueOf(string) },
			{ string -> JetFontFamily.valueOf(string) }
		)

		file.readLines().withIndex().map { indexedValue -> indexedValue.index to indexedValue.value }
			.forEach { (index, setting) ->
				try {
					when (index) {
						0 -> jetSettings.currentStyle.value = converters[index](setting) as JetStyle
						1 -> jetSettings.currentFontSize.value = converters[index](setting) as JetSize
						2 -> jetSettings.currentCornersStyle.value = converters[index](setting) as JetCorners
						3 -> jetSettings.currentFontFamily.value = converters[index](setting) as JetFontFamily
						4 -> jetSettings.isDarkMode.value = setting.toBoolean()
					}
				} catch (e: IllegalArgumentException) {
					println("The element '$setting' in line $index does not match any element of the corresponding enumeration.")
				}
			}

		Window(
			onCloseRequest = {
				save(jetSettings)
				exitApplication()
			},
			title = "YMOM",
			state = rememberWindowState(position = WindowPosition(Alignment.Center)),
		) {
			window.minimumSize = Dimension(windowSizeStart.first.toInt(), windowSizeStart.second.toInt())

			MaterialTheme { MainScreen(MainScreenViewModel(settings), jetSettings) }
		}
	}
}

fun findOrCreateSettings(file: File) {
	if (!file.exists()) {
		if (file.createNewFile()) {
			println("The 'settings' file has been successfully created.")
		} else {
			println("Failed to create file 'settings'.")
		}
	}
}

fun save(jetSettings: JetSettings) {
	val file = File("../settings")
	findOrCreateSettings(file)

	file.writeText(
		"${jetSettings.currentStyle.value}\n" +
			"${jetSettings.currentFontSize.value}\n" +
			"${jetSettings.currentCornersStyle.value}\n" +
			"${jetSettings.currentFontFamily.value}\n" +
			"${jetSettings.isDarkMode.value}"
	)
}
