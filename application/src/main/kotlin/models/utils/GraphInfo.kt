package models.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import java.nio.file.Path
import java.time.LocalDateTime

class GraphInfo(
	name: String,
	val pathTo: Path,
	val previouslyOpenDateTime: LocalDateTime = LocalDateTime.now(),
	isHidden: Boolean = false,
	onClick: ((Path) -> (Unit))? = null
) : ListWidgetItem(
	mainText = name,
	subText = pathTo.toString(),
	isHidden = isHidden,
	onClick = if (onClick != null) {
		{ onClick(pathTo) }
	} else {
		{ println("Click on graph info item ${name}") }
	},
	icon = Icons.Default.AutoGraph
	// TODO(Choose item icon as random from files)
), Comparable<GraphInfo> {
	override fun compareTo(other: GraphInfo): Int {
		return previouslyOpenDateTime.compareTo(other.previouslyOpenDateTime)
	}

	override fun toString(): String {
		return "GraphInfo(pathTo = $pathTo, previouslyOpenDateTime = $previouslyOpenDateTime)"
	}
}
