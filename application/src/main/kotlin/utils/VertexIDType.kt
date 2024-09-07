package utils

/**
 * Vertex identifier type on application representation.
 *
 * __Important:__ It's needed to valid execution of application and using on graphs objects.
 *
 * @property title simple string of name element
 * @constructor create Vertex ID type with custom label
 */
enum class VertexIDType(val title: String) {
	INT_TYPE(Int::class.simpleName.toString()),
	STRING_TYPE(String::class.simpleName.toString());
	override fun toString(): String {
		return title
	}
}
