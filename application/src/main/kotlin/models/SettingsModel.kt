package models

import graphs_lab.core.graphs.WeightedGraph
import utils.SaveType
import utils.VertexIDType

class SettingsModel {
	// TODO(Implements save graph)
	fun saveGraph(graph: WeightedGraph<VertexID>, vertexIDType: VertexIDType, value: SaveType) {
		println(graph)
	}

	companion object {
		@JvmStatic
		fun loadSettings(): SettingsModel {
			// TODO(valid load of settings from configuration)
			return SettingsModel()
		}
	}
}