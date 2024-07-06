package utils

/**
 * Enum of types to save graph objects in application.
 *
 * @property label simple string of name element
 * @constructor create graph saving type with custom label
 */
enum class GraphSavingType(val label: String) {
	LOCAL_FILE("Local file"),
	SQLITE_DB("SQLite DB"),
	NEO4J_DB("Neo4j DB"),;

	override fun toString(): String {
		return label
	}
}
