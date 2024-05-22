package graphs_lab.algs.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestSetPartition {
	private val elements = mutableSetOf(-2, -1, 0, 1, 2)
	private lateinit var setPartition: SetPartition<Int>

	@BeforeEach
	fun setup() {
		setPartition = SetPartition(-2, -1, 0, 1, 2)
	}

	@Test
	@DisplayName("initialize")
	fun testInitialize() {
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("get partition of set, after init")
	fun testGetPartitionAfterInit() {
		Assertions.assertEquals(
			setOf(
				setOf(-2),
				setOf(-1),
				setOf(0),
				setOf(1),
				setOf(2),
			),
			setPartition.getPartition()
		)
	}

	@Test
	@DisplayName("add element which not exists in partition")
	fun testAdditionNotExistsElement() {
		Assertions.assertTrue(setPartition.addElement(3))
		Assertions.assertEquals(elements.size + 1, setPartition.size)
		Assertions.assertEquals(elements + mutableSetOf(3), setPartition.elements)
	}

	@Test
	@DisplayName("add element which exists in partition")
	fun testAdditionExistsElement() {
		Assertions.assertFalse(setPartition.addElement(0))
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("get partition of set, after adding elements")
	fun testGetPartitionAfterAddingElements() {
		setPartition.addElement(3)
		setPartition.addElement(0)
		Assertions.assertEquals(
			setOf(
				setOf(-2),
				setOf(-1),
				setOf(0),
				setOf(1),
				setOf(2),
				setOf(3),
			),
			setPartition.getPartition()
		)
	}

	@Test
	@DisplayName("remove element which not exists in partition")
	fun testRemoveNotExistsElement() {
		Assertions.assertFalse(setPartition.removeElement(3))
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements - mutableSetOf(3), setPartition.elements)
	}

	@Test
	@DisplayName("remove element which exists in partition")
	fun testRemoveExistsElement() {
		Assertions.assertTrue(setPartition.removeElement(0))
		Assertions.assertEquals(elements.size - 1, setPartition.size)
		Assertions.assertEquals(elements - mutableSetOf(0), setPartition.elements)
	}

	@Test
	@DisplayName("get partition of set, after removing elements")
	fun testGetPartitionAfterRemovingElements() {
		setPartition.removeElement(3)
		setPartition.removeElement(0)
		Assertions.assertEquals(
			setOf(
				setOf(-2),
				setOf(-1),
				setOf(1),
				setOf(2),
			),
			setPartition.getPartition()
		)
	}

	@Test
	@DisplayName("connect element which not exists in partition")
	fun testConnectNotExistsElement() {
		Assertions.assertThrows(IllegalArgumentException::class.java) { setPartition.connectElements(3, 2) }
		Assertions.assertThrows(IllegalArgumentException::class.java) { setPartition.connectElements(2, 3) }
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("connect element which exists in partition")
	fun testConnectExistsElement() {
		setPartition.connectElements(0, 1)
		Assertions.assertEquals(elements.size - 1, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("get partition of set, after connecting elements")
	fun testGetPartitionAfterConnectingElements() {
		setPartition.connectElements(0, 1)
		Assertions.assertEquals(
			setOf(
				setOf(-2),
				setOf(-1),
				setOf(0, 1),
				setOf(2),
			),
			setPartition.getPartition()
		)
	}

	@Test
	@DisplayName("disconnect element which not exists in partition")
	fun testDisconnectNotExistsElement() {
		Assertions.assertThrows(IllegalArgumentException::class.java) { setPartition.disconnectElement(3) }
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("disconnect element which exists in partition")
	fun testDisconnectExistsElement() {
		setPartition.disconnectElement(0)
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("disconnect element, after connect which not exists in partition")
	fun testDisconnectExistsElementAfterConnect() {
		setPartition.connectElements(0, 1)
		setPartition.disconnectElement(0)
		Assertions.assertEquals(elements.size, setPartition.size)
		Assertions.assertEquals(elements, setPartition.elements)
	}

	@Test
	@DisplayName("get element's set")
	fun testGetElementSet() {
		Assertions.assertEquals(setOf(0), setPartition.getElementSet(0))
	}

	@Test
	@DisplayName("get element's set, element is not exists in partition")
	fun testGetNotExistsElementSet() {
		Assertions.assertThrows(IllegalArgumentException::class.java) { setPartition.getElementSet(3) }
	}

	@Test
	@DisplayName("chack connection of elements")
	fun testConnectionOfElements() {
		Assertions.assertFalse(setPartition.isConnected(0, 1))
	}

	@Test
	@DisplayName("chack connection of elements after connection")
	fun testConnectionOfElementsAfterConnection() {
		setPartition.connectElements(1, 0)
		Assertions.assertTrue(setPartition.isConnected(0, 1))
	}

	@Test
	@DisplayName("chack connection of elements after connection")
	fun testConnectionOfElementsWhichNotExists() {
		Assertions.assertThrows(IllegalArgumentException::class.java) { setPartition.isConnected(3, 1) }
		Assertions.assertThrows(IllegalArgumentException::class.java) { setPartition.isConnected(1, 3) }
	}

	@Test
	@DisplayName("partition to string")
	fun testPartitionToString() {
		val partition = setOf(
			setOf(-2),
			setOf(-1),
			setOf(0),
			setOf(1),
			setOf(2),
		)
		Assertions.assertEquals(
			"SetPartition(partition=$partition)",
			setPartition.toString()
		)
	}
}
