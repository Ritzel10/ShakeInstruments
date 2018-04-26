package pl.edu.pwr.nr238367.shakeinstruments

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.instrument_entry.view.*

const val INSTRUMENTS_JSON_FILE: String = "instruments.json"

class MainActivity : AppCompatActivity() {
    private lateinit var onShakeListener: OnShakeListener
    private lateinit var onCoverListener: OnCoverListener
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var proximitySensor: Sensor
    private lateinit var mediaPlayers: List<MediaPlayer>
    private lateinit var audioManager: AudioManager
    private var currentInstrumentIndex: Int = 0
    private lateinit var shakeAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initialize instrument list
        InstrumentList.initializeList(assets.open(INSTRUMENTS_JSON_FILE))
        //initialize sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        //create audio manager
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        //create a media player for each instrument in list
        mediaPlayers = InstrumentList.instrumentList.map { MediaPlayer.create(applicationContext, resources.getIdentifier(it.soundName, "raw", packageName)) }
        //attach view pager adapter
        viewPager.adapter = ImagePagerAdapter(applicationContext, InstrumentList.instrumentList)
        //change index when swapping instruments
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                currentInstrumentIndex = position
            }
        })
        //initialize listeners
        onShakeListener = OnShakeListener.createOnShakeListener(this::onShake)
        onCoverListener = OnCoverListener.createOnCoverListener(this::onCover)

        //create shake animation
        shakeAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        setSupportActionBar(toolbar)
    }
    //swipe card when proximity sensor is on
    private fun onCover() {
        val nextItem = (currentInstrumentIndex + 1) % InstrumentList.instrumentList.size
        viewPager.setCurrentItem(nextItem, true)
    }

    private fun playSound() {
        mediaPlayers[currentInstrumentIndex].start()

    }
    private fun onShake(){
        //animate only if sound is not playing
        if(mediaPlayers[currentInstrumentIndex].isPlaying){
            val drawable = viewPager?.findViewWithTag<View>(currentInstrumentIndex)?.instrumentImage
            //shakeAnimation.duration = mediaPlayers[currentInstrumentIndex].duration.toLong()
            drawable?.startAnimation(shakeAnimation)
        }
        playSound()
    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(onShakeListener, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(onCoverListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(onShakeListener)
        sensorManager.unregisterListener(onCoverListener)
    }


}
