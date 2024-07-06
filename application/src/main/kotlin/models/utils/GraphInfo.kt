package models.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import utils.GraphSavingType
import java.nio.file.Path
import java.time.LocalDateTime

/**
 * Represents a graph information item in the application.
 *
 * @param name the name of the graph
 * @property folderPath the path to the folder where the graph is stored
 * @property savingType the type of graph saving (e.g., Neo4j DB, CSV, etc.)
 * @property previouslyOpenDateTime the date and time when the graph was last opened
 * @param isHidden indicates whether the graph info item is hidden
 * @param onClick a callback function to be invoked when the graph info item is clicked
 */
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
	icon = Icons.Default.AutoGraph // TODO(Choose item icon as random from files)
), Comparable<GraphInfo> {
	override fun compareTo(other: GraphInfo): Int {
		return previouslyOpenDateTime.compareTo(other.previouslyOpenDateTime)
	}

	override fun toString(): String {
		return "GraphInfo(name = $mainText, folder = $folderPath, previouslyOpenDateTime = $previouslyOpenDateTime)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as GraphInfo

		if (mainText != other.mainText) return false
		return true
	}

	override fun hashCode(): Int {
		return mainText.hashCode()
	}
}
