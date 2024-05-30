package graphs_lab.algs

import graphs_lab.core.graphs.UnweightedGraph
import graphs_lab.algs.assistants.LeaderRankAssistant
import graphs_lab.core.graphs.WeightedGraph
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestLeaderRank {

	@Test
	@DisplayName("vertex indexing")
	fun testVertexIndexing() {
		val graph = UnweightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		graph.addVertex('A')
		graph.addVertex('B')
		graph.addVertex('C')

		val result = mutableMapOf('A' to 0, 'B' to 1, 'C' to 2)

		val assistant = LeaderRankAssistant(graph)
		assistant.assertVertexIndexing(result)
	}

	@Test
	@DisplayName("create graph matrix of unweighted graph")
	fun testCreateGraphMatrixOfUnweightedGraph() {
		val graph = UnweightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B')
		graph.addEdge('A', 'C')
		graph.addEdge('C', 'B')

		val result = Array(4) { DoubleArray(4) { 0.0 } }
		result[0][1] = 1.0
		result[0][2] = 1.0
		result[2][1] = 1.0
		for (i in 0..2) {
			result[i][3] = 1.0
			result[3][i] = 1.0
		}

		val assistant = LeaderRankAssistant(graph)
		assistant.assertCreateGraphMatrix(result)
	}

	@Test
	@DisplayName("create graph matrix of weighted graph")
	fun testCreateGraphMatrixOfWeightedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B', 5.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 52.0)

		val result = Array(4) { DoubleArray(4) { 0.0 } }
		result[0][1] = 5.0
		result[0][2] = 2.0
		result[2][1] = 52.0
		for (i in 0..2) {
			result[i][3] = 1.0
			result[3][i] = 1.0
		}

		val assistant = LeaderRankAssistant(graph)
		assistant.assertCreateGraphMatrix(result)
	}

	@Test
	@DisplayName("matrix multiplication")
	fun testMatrixMultiplication() {
		val graph = UnweightedGraph<Char>("plug", isDirected = true, isAutoAddVertex = true)

		val matrix1 = Array(4) { DoubleArray(4) { 0.0 } }
		matrix1[0][0] = 1.0
		matrix1[1][2] = 3.0
		matrix1[2][3] = 5.0
		matrix1[3][1] = 7.0
		matrix1[3][2] = 9.0

		val matrix2 = Array(4) { DoubleArray(2) { 0.0 } }
		matrix2[0][0] = 2.0
		matrix2[0][1] = 4.0
		matrix2[2][0] = 6.0
		matrix2[3][1] = 8.0

		val result = Array(4) { DoubleArray(2) { 0.0 } }
		result[0][0] = 2.0
		result[0][1] = 4.0
		result[1][0] = 18.0
		result[2][1] = 40.0
		result[3][0] = 54.0

		val assistant = LeaderRankAssistant(graph)
		assistant.assertMatrixMultiplication(matrix1, matrix2, result)
	}

	@Test
	@DisplayName("transpose graph matrix of unweighted graph")
	fun testTransposeGraphMatrixOfUnweightedGraph() {
		val graph = UnweightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B')
		graph.addEdge('A', 'C')
		graph.addEdge('C', 'B')

		val result = Array(4) { DoubleArray(4) { 0.0 } }
		result[1][0] = 1.0
		result[2][0] = 1.0
		result[1][2] = 1.0
		for (i in 0..2) {
			result[i][3] = 1.0
			result[3][i] = 1.0
		}

		val assistant = LeaderRankAssistant(graph)
		assistant.assertTransposeGraphMatrix(result)
	}

	@Test
	@DisplayName("transpose graph matrix of weighted graph")
	fun testTransposeGraphMatrixOfWeightedGraph() {
		val graph = WeightedGraph<Char>("1", isDirected = true, isAutoAddVertex = true)
		/**
		 * Image of a graph.
		 * A  →  B
		 * ↓  ↗
		 * C
		 */
		graph.addEdge('A', 'B', 5.0)
		graph.addEdge('A', 'C', 2.0)
		graph.addEdge('C', 'B', 52.0)

		val result = Array(4) { DoubleArray(4) { 0.0 } }
		result[1][0] = 5.0
		result[2][0] = 2.0
		result[1][2] = 52.0
		for (i in 0..2) {
			result[i][3] = 1.0
			result[3][i] = 1.0
		}

		val assistant = LeaderRankAssistant(graph)
		assistant.assertTransposeGraphMatrix(result)
	}

	@Test
	@DisplayName("matrix subtraction with modulus")
	fun testMatrixSubtractionWithModulus() {
		val graph = UnweightedGraph<Char>("plug", isDirected = true, isAutoAddVertex = true)

		val matrix1 = Array(4) { DoubleArray(1) { 0.0 } }
		matrix1[0][0] = 1.0
		matrix1[2][0] = 3.0
		matrix1[3][0] = 5.0

		val matrix2 = Array(4) { DoubleArray(1) { 0.0 } }
		matrix2[1][0] = 2.0
		matrix2[2][0] = 4.0
		matrix2[3][0] = 8.0

		val result = Array(4) { DoubleArray(1) { 0.0 } }
		result[0][0] = 1.0
		result[1][0] = 2.0
		result[2][0] = 1.0
		result[3][0] = 3.0

		val assistant = LeaderRankAssistant(graph)
		assistant.assertMatrixSubtractionWithModulus(matrix1, matrix2, result)
	}

	@Test
	@DisplayName("matrix division")
	fun testMatrixDivision() {
		val graph = UnweightedGraph<Char>("plug", isDirected = true, isAutoAddVertex = true)

		val matrix1 = Array(4) { DoubleArray(1) { 0.0 } }
		matrix1[0][0] = 1.0
		matrix1[2][0] = 3.0
		matrix1[3][0] = 5.0

		val matrix2 = Array(4) { DoubleArray(1) { 0.0 } }
		matrix2[1][0] = 2.0
		matrix2[2][0] = 4.0
		matrix2[3][0] = 8.0

		val result = Array(4) { DoubleArray(1) { 0.0 } }
		result[2][0] = 0.75
		result[3][0] = 0.625

		val assistant = LeaderRankAssistant(graph)
		assistant.assertMatrixDivision(matrix1, matrix2, result)
	}

	@Test
	@DisplayName("summation of matrix elements")
	fun testSummationOfMatrixElements() {
		val graph = UnweightedGraph<Char>("plug", isDirected = true, isAutoAddVertex = true)

		val matrix = Array(6) { DoubleArray(1) { 0.0 } }
		matrix[0][0] = 1.0
		matrix[2][0] = 3.0
		matrix[3][0] = -5.0
		matrix[5][0] = 7.0

		val assistant = LeaderRankAssistant(graph)
		assistant.assertSummationOfMatrixElements(matrix, 6.0)
	}
}
