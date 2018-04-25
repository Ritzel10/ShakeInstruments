package pl.edu.pwr.nr238367.shakeinstruments


fun calculateAcceleration(values:FloatArray):Double{
    return Math.sqrt(values.map { it * it }.sum().toDouble())
}