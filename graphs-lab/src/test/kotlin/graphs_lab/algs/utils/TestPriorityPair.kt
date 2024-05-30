package graphs_lab.algs.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class TestPriorityPair {
	private lateinit var priorityPair: PriorityPair<Int, String?>

	@BeforeEach
	fun setup() {
		priorityPair = PriorityPair(1, "one")
	}

	@Test
	@DisplayName("initialize priority pair")
	fun testInitPriorityPair() {
		Assertions.assertEquals(1, priorityPair.priority)
		Assertions.assertEquals("one", priorityPair.value)
	}

	@Test
	@DisplayName("compare priority pair with itself")
	fun testComparePriorityPairWithItself() {
		Assertions.assertEquals(
			0,
			priorityPair.compareTo(priorityPair)
		)
	}

	@Test
	@DisplayName("compare priority pair with copy of it")
	fun testComparePriorityPairWithCopyOfIt() {
		Assertions.assertEquals(
			0,
			priorityPair.compareTo(priorityPair.copy())
		)
	}

	@Test
	@DisplayName("compare priority pair with pair which priority lower")
	fun testComparePriorityPairWithPairWhichPriorityLower() {
		Assertions.assertEquals(
			1,
			priorityPair.compareTo(PriorityPair(-1, "one"))
		)
	}

	@Test
	@DisplayName("compare priority pair with pair which priority lower and other data")
	fun testComparePriorityPairWithPairWhichPriorityLowerAndOtherData() {
		Assertions.assertEquals(
			1,
			priorityPair.compareTo(PriorityPair(-1, "-one"))
		)
	}

	@Test
	@DisplayName("compare priority pair with pair which priority bigger")
	fun testComparePriorityPairWithPairWhichPriorityBigger() {
		Assertions.assertEquals(
			-1,
			priorityPair.compareTo(PriorityPair(3, "one"))
		)
	}

	@Test
	@DisplayName("compare priority pair with pair which priority bigger and other data")
	fun testComparePriorityPairWithPairWhichPriorityBiggerAndOtherData() {
		Assertions.assertEquals(
			-1,
			priorityPair.compareTo(PriorityPair(3, "-one"))
		)
	}

	@Test
	@DisplayName("priority pair to string method")
	fun testPriorityPairToStringMethod() {
		Assertions.assertEquals(
			"PriorityPair(priority=1, value=one)",
			priorityPair.toString()
		)
	}

	@Test
	@DisplayName("is priority pair equals itself")
	fun testPriorityEqualsCase1() {
		Assertions.assertTrue(priorityPair.equals(priorityPair))
	}

	@Test
	@DisplayName("is priority pair equals copy of it")
	fun testPriorityEqualsCase2() {
		Assertions.assertTrue(priorityPair.equals(priorityPair.copy()))
	}

	@Test
	@DisplayName("is priority pair not equals null")
	fun testPriorityEqualsCase3() {
		Assertions.assertFalse(priorityPair.equals(null))
	}

	@Test
	@DisplayName("is priority pair not equals with object of another type")
	fun testPriorityEqualsCase4() {
		Assertions.assertFalse(priorityPair.equals(10))
	}

	@Test
	@DisplayName("is priority pair not equals with pair wich diferent by value")
	fun testPriorityEqualsCase5() {
		Assertions.assertFalse(priorityPair.equals(priorityPair.copy(value = "another")))
	}

	@Test
	@DisplayName("is priority pair not equals with pair wich diferent by priority")
	fun testPriorityEqualsCase6() {
		Assertions.assertFalse(priorityPair.equals(priorityPair.copy(priority = -10)))
	}

	@Test
	@DisplayName("priority pair hash code with null value")
	fun testPriorityHashCodeCase1() {
		Assertions.assertEquals(
			31 * 31 * priorityPair.javaClass.name.hashCode() + 31 * priorityPair.priority.hashCode(),
			priorityPair.copy(value = null).hashCode()
		)
	}

	@Test
	@DisplayName("priority pair hash code with null value and zero priority")
	fun testPriorityHashCodeCase2() {
		Assertions.assertEquals(
			31 * 31 * priorityPair.javaClass.name.hashCode(),
			priorityPair.copy(priority = 0, value = null).hashCode()
		)
	}

	@Test
	@DisplayName("priority pair hash code without null value")
	fun testPriorityHashCodeCase3() {
		val priorityHash = priorityPair.priority.hashCode()
		val valueHash = priorityPair.value.hashCode()
		Assertions.assertEquals(
			31 * 31 * priorityPair.javaClass.name.hashCode() + 31 * priorityHash + valueHash,
			priorityPair.hashCode()
		)
	}
}
