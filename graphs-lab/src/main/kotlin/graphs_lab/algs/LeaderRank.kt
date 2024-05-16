package graphs_lab.algs

import graphs_lab.core.edges.Edge
import graphs_lab.core.graphs.Graph
import graphs_lab.algs.utils.getEdgeWeight
import kotlin.math.abs

/**
 * LeaderRank algorithm
 *
 * LeaderRank is an algorithm for identifying key vertices in a graph. The essence of the algorithm: given a graph
 * consisting of N vertices and M directed edges, a ground vertex connected with every vertex by a bidirectional edge is
 * added. Then, the graph becomes strongly connected and consists of N+1 vertices and M+2N edges (a bidirectional edges
 * is counted as two edges with inverse directions). LeaderRank directly applies the standard random walk process to
 * determine the score of every vertex.
 *
 * @references https://journals.plos.org/plosone/article?id=10.1371/journal.pone.0021202
 *
 * @param I the type of the vertex identifiers
 * @property graph the input graph
 */
open class LeaderRank<I, E : Edge<I>>(val graph: Graph<I, E>) {
	protected var countOfVertices = 0
	protected val indexedVertices = mutableMapOf<I, Int>()
	protected var graphMatrix: Array<DoubleArray> = Array(countOfVertices) { DoubleArray(countOfVertices) }

	/**
	 * Get the score of each vertex in the graph.
	 *
	 * @return a map where keys - the id of vertex, values - the score of this vertex.
	 */
	fun getVerticesScores(): Map<I, Double> {
		countOfVertices = graph.size + 1 // add ground vertex
		graphMatrix = Array(countOfVertices) { DoubleArray(countOfVertices) { 0.0 } }

		vertexIndexing()
		createGraphMatrix()

		val auxiliaryMatrix = Array(countOfVertices) { DoubleArray(countOfVertices) { 0.0 } }
		for (i in 0 until countOfVertices) {
			auxiliaryMatrix[i][i] = 1.0 / graphMatrix[i].sum()
		}
		graphMatrix = matrixMultiplication(auxiliaryMatrix, graphMatrix, countOfVertices)

		// Diffusion to stable state
		var scoringMatrix = Array(countOfVertices) { DoubleArray(1) { 1.0 } }
		scoringMatrix[countOfVertices - 1][0] = 0.0
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
				) / (countOfVertices - 1)
				)
		}

		for (i in 0 until countOfVertices - 1) {
			scoringMatrix[i][0] += scoringMatrix[countOfVertices - 1][0] / (countOfVertices - 1)
		}

		val result = mutableMapOf<I, Double>()
		var index = 0
		graph.idVertices.forEach {
			result[it] = scoringMatrix[index][0]
			index++
		}
		return result
	}

	/**
	 * Mapping an index to each vertex.
	 */
	protected fun vertexIndexing() {
		var index = 0
		graph.idVertices.forEach { vertex ->
			indexedVertices[vertex] = index
			index++
		}
	}

	/**
	 * Creates an adjacency matrix for a graph based on its vertices and edges.
	 */
	protected fun createGraphMatrix() {
		graph.idVertices.forEach { vertex ->
			graph.vertexEdges(vertex).forEach { edge ->
				graphMatrix[indexedVertices[edge.idSource]!!][indexedVertices[edge.idTarget]!!] =
					getEdgeWeight(edge, 1.0)
			}
		}

		for (i in 0 until countOfVertices - 1) {
			graphMatrix[i][countOfVertices - 1] = 1.0
			graphMatrix[countOfVertices - 1][i] = 1.0
		}
	}

	/**
	 * Multiplies the two matrices in their given order.
	 *
	 * @param matrix1 first matrix
	 * @param matrix2 second matrix
	 * @param numberOfColumns number columns in result matrix
	 * @return a matrix that is the result of matrix multiplication.
	 */
	protected fun matrixMultiplication(
		matrix1: Array<DoubleArray>,
		matrix2: Array<DoubleArray>,
		numberOfColumns: Int
	): Array<DoubleArray> {
		val resultMatrix = Array(countOfVertices) { DoubleArray(numberOfColumns) { 0.0 } }

		for (i in 0 until countOfVertices) {
			for (j in 0 until numberOfColumns) {
				var sum = 0.0
				for (k in 0 until countOfVertices) {
					sum += matrix1[i][k] * matrix2[k][j]
				}
				resultMatrix[i][j] = sum
			}
		}

		return resultMatrix
	}

	/**
	 * Transposes a graphMatrix.
	 */
	protected fun transposeGraphMatrix() {
		val transposedMatrix = Array(countOfVertices) { DoubleArray(countOfVertices) { 0.0 } }

		for (i in 0 until countOfVertices) {
			for (j in 0 until countOfVertices) {
				transposedMatrix[j][i] = graphMatrix[i][j]
			}
		}

		graphMatrix = transposedMatrix
	}

	/**
	 * Performs matrix subtraction with modulus.
	 *
	 * @param matrix1 first matrix
	 * @param matrix2 second matrix
	 * @return a matrix that is the result of modulo subtraction
	 */
	protected fun matrixSubtractionWithModulus(
		matrix1: Array<DoubleArray>,
		matrix2: Array<DoubleArray>
	): Array<DoubleArray> {
		val resultMatrix = Array(countOfVertices) { DoubleArray(1) { 0.0 } }

		for (i in 0 until countOfVertices) {
			resultMatrix[i][0] = abs(matrix1[i][0] - matrix2[i][0])
		}

		return resultMatrix
	}

	/**
	 * Performs division of one matrix by another.
	 *
	 * @param matrix1 Divisible matrix
	 * @param matrix2 Divider
	 * @return a matrix that is the result of division
	 */
	protected fun matrixDivision(matrix1: Array<DoubleArray>, matrix2: Array<DoubleArray>): Array<DoubleArray> {
		val resultMatrix = Array(countOfVertices) { DoubleArray(1) { 0.0 } }

		for (i in 0 until countOfVertices) {
			if (matrix2[i][0] != 0.0) {
				resultMatrix[i][0] = matrix1[i][0] / matrix2[i][0]
			}
		}

		return resultMatrix
	}

	/**
	 * Calculates the sum of matrix elements.
	 *
	 * @param matrix on which actions will be performed
	 * @return sum of all matrix elements
	 */
	protected fun summationOfMatrixElements(matrix: Array<DoubleArray>): Double {
		var resultOfSum = 0.0

		for (i in 0 until countOfVertices) {
			resultOfSum += matrix[i][0]
		}

		return resultOfSum
	}
}
