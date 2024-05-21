package models.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

data class TabItem(
	val title: String,
	val iconSelected: ImageVector,
	val iconUnselected: ImageVector,
	val isHide: MutableState<Boolean> = mutableStateOf(false),
	val isPageSelectable: Boolean = false,
	val dropDownMenuContext: @Composable ((TabItem) -> (Unit))? = null,
	private val onItemClick: ((TabItem) -> (Unit))? = null,
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as TabItem

		return title == other.title
	}

	override fun hashCode(): Int {
		return title.hashCode()
	}

	override fun toString(): String {
		return "TabItem(title='$title', isHide=${isHide.value})"
	}

	fun onClick(): (() -> (Unit))? {
		val onItemClick = this.onItemClick ?: return null
		return { onItemClick(this) }
	}
}
