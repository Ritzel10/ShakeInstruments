package pl.edu.pwr.nr238367.shakeinstruments

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

const val COVERED_DISTANCE = 0.5
abstract class OnCoverListener : SensorEventListener{
    private var wasCovered = false
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val proximityValue = sensorEvent.values[0]
        val isCoveredNow = proximityValue <= COVERED_DISTANCE
        if(wasCovered && !isCoveredNow){
            onUncover(sensorEvent)
            wasCovered = false
        }
        else if (!wasCovered && isCoveredNow){
            onCover(sensorEvent)
            wasCovered = true
        }
    }
    abstract fun onCover(sensorEvent: SensorEvent)
    abstract fun onUncover(sensorEvent: SensorEvent)

    companion object {
        fun createOnCoverListener(onCover:(()->Unit)? = null, onUncover:(()->Unit)? = null):OnCoverListener{
            return object : OnCoverListener(){
                override fun onCover(sensorEvent: SensorEvent) {
                    onCover?.invoke()
                }

                override fun onUncover(sensorEvent: SensorEvent) {
                    onUncover?.invoke()
                }
            }
        }
    }
}