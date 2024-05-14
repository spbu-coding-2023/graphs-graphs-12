package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import graphs_lab.algs.utils.getEdgeWeight
import kotlin.math.abs

class LeaderRank<I, E : Edge<I>>(val graph: Graph<I, E>) {
	private var countOfVertices = 0
	private val indexedVertices = mutableMapOf<I, Int>()
	private var graphMatrix: Array<DoubleArray> = Array(countOfVertices + 1) { DoubleArray(countOfVertices + 1) }

	fun getVerticesScores(): Map<I, Double> {
		countOfVertices = graph.size
		graphMatrix = Array(countOfVertices + 1) { DoubleArray(countOfVertices + 1) { 0.0 } }

		vertexIndexing()
		createGraphMatrix()

		val auxiliaryMatrix = Array(countOfVertices + 1) { DoubleArray(countOfVertices + 1) { 0.0 } }
		for (i in 0..countOfVertices) {
			auxiliaryMatrix[i][i] = 1.0 / graphMatrix[i].sum()
		}
		graphMatrix = matrixMultiplication(auxiliaryMatrix, graphMatrix, countOfVertices + 1)

		// Diffusion to stable state
		var scoringMatrix = Array(countOfVertices + 1) { DoubleArray(1) { 1.0 } }
		scoringMatrix[countOfVertices][0] = 0.0
		var error = 10_000.0 // Assign initial resource error is the average error of nodes’ scores
		val errorThreshold = 0.00002 // It is a tunable parameter controlling the error tolerance
		transposeGraphMatrix()

		while (error > errorThreshold) {
			val tempScoringMatrix = scoringMatrix
			scoringMatrix = matrixMultiplication(graphMatrix, scoringMatrix, 1)
			error = (
				summationOfMatrixElements(
					matrixDivision(
						matrixSubtractionWithModulus(scoringMatrix, tempScoringMatrix),
						tempScoringMatrix
					)
				) / (countOfVertices + 1)
				)
		}

		for (i in 0 until countOfVertices) {
			scoringMatrix[i][0] += scoringMatrix[countOfVertices][0] / countOfVertices
		}

		val result = mutableMapOf<I, Double>()
		var index = 0
		graph.idVertices.forEach {
			result[it] = scoringMatrix[index][0]
			index++
		}
		return result
	}

	private fun vertexIndexing() {
		var index = 0
		graph.idVertices.forEach { vertex ->
			indexedVertices[vertex] = index
			index++
		}
	}

	private fun createGraphMatrix() {
		graph.idVertices.forEach { vertex ->
			graph.vertexEdges(vertex).forEach { edge ->
				graphMatrix[indexedVertices[edge.idSource]!!][indexedVertices[edge.idTarget]!!] =
					getEdgeWeight(edge, 1.0)
			}
		}

		for (i in 0 until countOfVertices) {
			graphMatrix[i][countOfVertices] = 1.0
			graphMatrix[countOfVertices][i] = 1.0
		}
	}

	private fun matrixMultiplication(
		matrix1: Array<DoubleArray>,
		matrix2: Array<DoubleArray>,
		numberOfColumns: Int
	): Array<DoubleArray> {
		val resultMatrix = Array(countOfVertices + 1) { DoubleArray(numberOfColumns) { 0.0 } }

		for (i in 0..countOfVertices) {
			for (j in 0 until numberOfColumns) {
				var sum = 0.0
				for (k in 0..countOfVertices) {
					sum += matrix1[i][k] * matrix2[k][j]
				}
				resultMatrix[i][j] = sum
			}
		}

		return resultMatrix
	}

	private fun transposeGraphMatrix() {
		val transposedMatrix = Array(countOfVertices + 1) { DoubleArray(countOfVertices + 1) { 0.0 } }

		for (i in 0..countOfVertices) {
			for (j in 0..countOfVertices) {
				transposedMatrix[j][i] = graphMatrix[i][j]
			}
		}

		graphMatrix = transposedMatrix
	}

	private fun matrixSubtractionWithModulus(
		matrix1: Array<DoubleArray>,
		matrix2: Array<DoubleArray>
	): Array<DoubleArray> {
		val resultMatrix = Array(countOfVertices + 1) { DoubleArray(1) { 0.0 } }

		for (i in 0..countOfVertices) {
			resultMatrix[i][0] = abs(matrix1[i][0] - matrix2[i][0])
		}

		return resultMatrix
	}

	private fun matrixDivision(matrix1: Array<DoubleArray>, matrix2: Array<DoubleArray>): Array<DoubleArray> {
		val resultMatrix = Array(countOfVertices + 1) { DoubleArray(1) { 0.0 } }

		for (i in 0..countOfVertices) {
			if (matrix2[i][0] != 0.0) {
				resultMatrix[i][0] = matrix1[i][0] / matrix2[i][0]
			}
		}

		return resultMatrix
	}

	private fun summationOfMatrixElements(matrix: Array<DoubleArray>): Double {
		var resultOfSum = 0.0

		for (i in 0..countOfVertices) {
			resultOfSum += matrix[i][0]
		}

		return resultOfSum
	}
}
