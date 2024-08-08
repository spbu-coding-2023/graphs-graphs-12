package utils

/**
 * Represents the different types of tabs in the side menu.
 *
 * @property title the title of the tab
 *
 */
enum class SideMenuTabType(val title: String) {
	HOME("Home"),
	GRAPH_VIEW("GraphView"),
	SETTINGS("Settings"),
	ALGORITHMS("Algorithms"),
	REPRESENTATION("Representation"),
	SAVE("Save");

	companion object {
		/**
		 * This function is used to find a [SideMenuTabType] by its title.
		 *
		 * @param title the title of the tab to find
		 * @return the [SideMenuTabType] with the given title,
		 * 			or throws an [IllegalArgumentException] if no such tab exists
		 *
		 * @throws IllegalArgumentException if no [SideMenuTabType] with the given title is found
		 */
		fun valueByTitle(title: String): SideMenuTabType = entries.find {
			it.title == title
		} ?: throw IllegalArgumentException("Invalid tab title: $title")
	}
}
