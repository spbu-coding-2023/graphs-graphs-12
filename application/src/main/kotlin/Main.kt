import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.*
import models.SettingsModel
import views.MainScreen
import viewmodels.MainScreenViewModel
import java.awt.Dimension

/**
 * Application entry point.
 */
fun main() {
	val windowSizeStart = Pair(1000f, 700f)

	application {
		val settings: SettingsModel = SettingsModel.loadSettings()
		Window(
			onCloseRequest = ::exitApplication,
			title = "AppYMOM",
			state = rememberWindowState(position = WindowPosition(Alignment.Center)),
		) {
			window.minimumSize = Dimension(windowSizeStart.first.toInt(), windowSizeStart.second.toInt())

			MaterialTheme { MainScreen(MainScreenViewModel(settings)) }
		}
	}
}
