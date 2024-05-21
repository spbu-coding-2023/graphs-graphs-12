package utils

enum class SaveType(val label: String) {
	LOCAL_FILE("Local file"),
	SQLITE_DB("SQLite DB"),
	NEO4J_DB("Neo4j DB"),;

	override fun toString(): String {
		return label
	}
}
