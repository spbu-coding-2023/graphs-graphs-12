package models

import models.utils.TabItem
import utils.SideMenuTabType

/**
 * A model representing the side menu in an application.
 * It manages a list of tabs and their visibility.
 *
 * @property tabsItems the list of tabs in the side menu
 * @property tabsCount the number of tabs in the side menu
 */
class SideMenuModel {
	private val _tabsItems = mutableListOf<List<TabItem>?>()
	private val tabsIdentifier = mutableMapOf<SideMenuTabType, TabItem>()
	val tabsItems: List<List<TabItem>?>
		get() = _tabsItems.toList()
	val tabsCount: Int
		get() = _tabsItems.size

	/**
	 * Add tabs to the tabs list as one group.
	 *
	 * @param items the list of tabs to add to the list of tabs
	 */
	fun addTabs(items: List<TabItem>) {
		_tabsItems.add(items)
		items.forEach { tabsIdentifier[SideMenuTabType.valueByTitle(it.title)] = it }
	}

	/**
	 * Add tabs to the tabs list.
	 *
	 * @param items the list of tabs to add to the list of tabs
	 */
	fun addTabs(vararg items: TabItem) {
		addTabs(items.toList())
	}

	/**
	 * Adds a separator to the list of tabs.
	 */
	fun addSeparator() {
		_tabsItems.add(null)
	}

	/**
     * Changes the visibility of a tab.
     *
     * @param tabType the title of the tab to change the visibility of
     * @param isHiddenState the new visibility of the tab
	 */
	fun changeTabVisibility(tabType: SideMenuTabType, isHiddenState: Boolean) {
		val tabItem = tabsIdentifier[tabType] ?: return
		tabItem.isHidden.value = isHiddenState
	}

	override fun toString(): String {
		return "SideMenuModel(tabsCount = $tabsCount)"
	}
}
