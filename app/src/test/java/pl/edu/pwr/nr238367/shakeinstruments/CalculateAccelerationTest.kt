package pl.edu.pwr.nr238367.shakeinstruments

import org.junit.Test

import org.junit.Assert.*


class CalculateAccelerationTest {
    @Test
    fun forCorrectValuesShouldGiveCorrectAnswer() {
        assertEquals(3.0, calculateAcceleration(floatArrayOf(3f)),0.0001)
        assertEquals(3.0, calculateAcceleration(floatArrayOf(1f, 2f, 2f)),0.0001)
    }
}
