package models.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import utils.GraphSavingType
import java.nio.file.Path
import java.time.LocalDateTime

class GraphInfo(
	name: String,
	val folderPath: String,
	val savingType: GraphSavingType,
	val previouslyOpenDateTime: LocalDateTime = LocalDateTime.now(),
	isHidden: Boolean = false,
	onClick: ((String, String, GraphSavingType) -> (Unit))? = null
) : ListWidgetItem(
	mainText = name,
	subText = if (savingType == GraphSavingType.NEO4J_DB) "Neo4j" else folderPath,
	isHidden = isHidden,
	onClick = if (onClick != null) {
		{ onClick(name, folderPath, savingType) }
	} else {
		{ println("Click on graph info item $name") }
	},
	icon = Icons.Default.AutoGraph
	// TODO(Choose item icon as random from files)
), Comparable<GraphInfo> {
	override fun compareTo(other: GraphInfo): Int {
		return previouslyOpenDateTime.compareTo(other.previouslyOpenDateTime)
	}

	override fun toString(): String {
		return "GraphInfo(name = $mainText, folder = $folderPath, previouslyOpenDateTime = $previouslyOpenDateTime)"
	}
}
