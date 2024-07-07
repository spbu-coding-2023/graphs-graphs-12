package models

import utils.VertexIDType

/**
 * Class representing a VertexID with a specific type.
 *
 * @property value the value of the VertexID. It can be of type [Int] or [String]
 * @property type the type of the VertexID. It can be either [VertexIDType.INT_TYPE] or [VertexIDType.STRING_TYPE]
 */
data class VertexID(private val value: Any, val type: VertexIDType) {
	/**
	 * Converts the value of the VertexID to an [Int].
	 *
	 * @return the value of the VertexID as an [Int]
	 * @throws NumberFormatException if the value cannot be converted to an [Int]
	 */
	fun valueToInt(): Int = value.toString().toInt()
	/**
	 * Converts the value of the VertexID to a [String].
	 *
	 * @return the value of the VertexID as a [String]
	 */
	fun valueToString(): String = value.toString()

	companion object {
		/**
		 * Creates a new [VertexID] instance from a [String] value and a [VertexIDType].
		 *
		 * @param value the value of the VertexID as a [String]
		 * @param type the type of the VertexID. It can be either [VertexIDType.INT_TYPE] or [VertexIDType.STRING_TYPE]
		 * @return a new [VertexID] instance
		 */
		@JvmStatic
		fun vertexIDFromString(value: String, type: VertexIDType): VertexID = when (type) {
			VertexIDType.INT_TYPE -> VertexID(value.toInt(), type)
			VertexIDType.STRING_TYPE -> VertexID(value, type)
		}
	}
}
