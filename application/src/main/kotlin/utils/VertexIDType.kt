package utils

enum class VertexIDType(val title: String) {
	INT_TYPE(Int::class.simpleName.toString()),
	STRING_TYPE(String::class.simpleName.toString());
	override fun toString(): String {
		return title
	}
}