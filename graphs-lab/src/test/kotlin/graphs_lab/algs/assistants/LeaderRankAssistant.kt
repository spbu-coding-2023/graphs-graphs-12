package graphs_lab.algs.assistants

import graphs_lab.algs.LeaderRank
import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph

class LeaderRankAssistant<I, E : Edge<I>>(graph: Graph<I, E>) : LeaderRank<I, E>(graph) {

	fun assertVertexIndexing(result: MutableMap<Char, Int>) {
		vertexIndexing()

		if (indexedVertices != result) {
			throw AssertionError()
		}
	}

	fun assertCreateGraphMatrix(result: Array<DoubleArray>) {
		countOfVertices = graph.size + 1
		graphMatrix = Array(countOfVertices) { DoubleArray(countOfVertices) { 0.0 } }

		vertexIndexing()
		createGraphMatrix()

		if (!graphMatrix.contentDeepEquals(result)) {
			throw AssertionError()
		}
	}

	fun assertMatrixMultiplication(
		matrix1: Array<DoubleArray>,
		matrix2: Array<DoubleArray>,
		result: Array<DoubleArray>
	) {
		countOfVertices = 4

		if (!result.contentDeepEquals(matrixMultiplication(matrix1, matrix2, 2))) {
			throw AssertionError()
		}
	}

	fun assertTransposeGraphMatrix(result: Array<DoubleArray>) {
		countOfVertices = graph.size + 1
		graphMatrix = Array(countOfVertices) { DoubleArray(countOfVertices) { 0.0 } }

		vertexIndexing()
		createGraphMatrix()
		transposeGraphMatrix()

		if (!result.contentDeepEquals(graphMatrix)) {
			throw AssertionError()
		}
	}

	fun assertMatrixSubtractionWithModulus(
		matrix1: Array<DoubleArray>,
		matrix2: Array<DoubleArray>,
		result: Array<DoubleArray>
	) {
		countOfVertices = 4

		if (!result.contentDeepEquals(matrixSubtractionWithModulus(matrix1, matrix2))) {
			throw AssertionError()
		}
	}

	fun assertMatrixDivision(matrix1: Array<DoubleArray>, matrix2: Array<DoubleArray>, result: Array<DoubleArray>) {
		countOfVertices = 4

		if (!result.contentDeepEquals(matrixDivision(matrix1, matrix2))) {
			throw AssertionError()
		}
	}

	fun assertSummationOfMatrixElements(matrix: Array<DoubleArray>, result: Double) {
		countOfVertices = 6

		if (result != summationOfMatrixElements(matrix)) {
			throw AssertionError()
		}
	}
}
