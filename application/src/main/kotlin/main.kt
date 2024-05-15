import androidx.compose.material.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import application.core.app
import java.awt.Dimension

fun main() = application {
	Window(
		onCloseRequest = ::exitApplication,
		title = "AppYMOM",
	) {
		window.minimumSize = Dimension(1000, 700)

		MaterialTheme {
			app()
		}
	}
}
