package pl.edu.pwr.nr238367.shakeinstruments

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


abstract class OnShakeListener( private val timeBetweenShake:Long = MIN_TIME_BETWEEN_SHAKE_MS_SHORT,  private val shakeThreshold:Double = SHAKE_THRESHOLD_NORMAL) : SensorEventListener{
    private var lastShakeTime:Long = 0
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastShakeTime >= timeBetweenShake){
            lastShakeTime = currentTime
            val acceleration = calculateAcceleration(sensorEvent.values)
            if(acceleration >= shakeThreshold){
                onShake(sensorEvent)
            }
        }
    }
    private fun calculateAcceleration(values:FloatArray):Double{
        return Math.sqrt(values.map { it * it }.sum().toDouble()) - SensorManager.GRAVITY_EARTH
    }
    abstract fun onShake(sensorEvent: SensorEvent)

    companion object {
        val MIN_TIME_BETWEEN_SHAKE_MS_SHORT:Long =50
        val MIN_TIME_BETWEEN_SHAKE_MS_NORMAL:Long = 200
        val MIN_TIME_BETWEEN_SHAKE_MS_LONG:Long = 500

        val SHAKE_THRESHOLD_SENSITIVE = 2.0
        val SHAKE_THRESHOLD_NORMAL = 3.5
        val SHAKE_THRESHOLD_INSENSITIVE = 5.0

        fun createOnShakeListener(onShake:()->Unit): OnShakeListener {
            return object : OnShakeListener(){
                override fun onShake(sensorEvent: SensorEvent) {
                    onShake()
                }
            }
        }
    }
}