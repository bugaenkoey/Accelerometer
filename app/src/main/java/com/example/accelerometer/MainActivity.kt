package com.example.accelerometer

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var sManager:SensorManager
    private  var magnetyc = FloatArray(9)
    private  var gravyty = FloatArray(9)

    private  var accrs = FloatArray(3)
    private  var magf = FloatArray(3)
    private  var values = FloatArray(3)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//Nahodim elementy tvSensor i lRotation na nashem Viev
        val tvSensor = findViewById<TextView>(R.id.tvSensor)
        val lRotation = findViewById<LinearLayout>(R.id.lRotation)

        sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensor2 = sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        val sListener = object : SensorEventListener{

            override fun onSensorChanged(event: SensorEvent?) {
//                val value = event?.values
//                val sData = "X: ${value?.get(0)}\n" +
//                        "Y: ${value?.get(1)}\n" +
//                        "Z: ${value?.get(2)}"
//                tvSensor.text = sData

                when(event?.sensor?.type){
                    Sensor.TYPE_ACCELEROMETER -> accrs = event.values.clone()
                    Sensor.TYPE_MAGNETIC_FIELD -> magf = event.values.clone()
                }
                SensorManager.getRotationMatrix(gravyty,magnetyc,accrs,magf)
                val  outGravity = FloatArray(9)
                SensorManager.remapCoordinateSystem(gravyty,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                    outGravity
                )
                SensorManager.getOrientation(outGravity,values)
//                Preobrazuem radian v grsdusi
                val degree = values[2] * 57.2958f
                val rorate = 270 + degree
                lRotation.rotation = rorate
                val rData = 90 + degree
                val color = if (rData.toInt()==0){
                    Color.GREEN
                }else{
                    Color.RED
                }
                lRotation.setBackgroundColor(color)
                tvSensor.text = rData.toInt().toString()

            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

        }
        sManager.registerListener(sListener,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        sManager.registerListener(sListener,sensor2,SensorManager.SENSOR_DELAY_NORMAL)
    }
}