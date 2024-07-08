import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import models.SettingsModel
import views.MainScreen
import viewmodels.MainScreenViewModel
import java.awt.Dimension

/**
 * A pair representing the initial size of the application window.
 */
val windowSizeStart = Pair(1000f, 700f)

/**
 * Application entry point.
 */
fun main() {
	application {
		val settings: SettingsModel = SettingsModel.loadSettings()
		Window(
			onCloseRequest = ::exitApplication,
			title = "YMOM",
			state = rememberWindowState(position = WindowPosition(Alignment.Center)),
		) {
			window.minimumSize = Dimension(windowSizeStart.first.toInt(), windowSizeStart.second.toInt())

			MaterialTheme { MainScreen(MainScreenViewModel(settings)) }
		}
	}
}
