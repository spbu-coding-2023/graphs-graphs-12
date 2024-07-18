package utils

/**
 * Enum of types to save graph objects in application.
 *
 * @property label simple string of name element
 * @constructor create graph saving type with custom label
 */
enum class GraphSavingType(val label: String, val filenamePatterns: Regex? = null) {
	LOCAL_FILE(
		"Local file",
		Regex("[a-zA-Z][a-zA-Z0-9_-]*.json")
	),
	SQLITE_DB(
		"SQLite DB",
		Regex("[a-zA-Z][a-zA-Z0-9_-]*.db")
	),
	NEO4J_DB("Neo4j DB");

	override fun toString(): String {
		return label
	}
}
