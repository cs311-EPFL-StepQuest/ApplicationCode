package com.github.se.stepquest.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.text.format.DateFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class StepCounterService() : Service(), SensorEventListener {

  lateinit var sensorManager: SensorManager
  var stepSensor: Sensor? = null
  private var stepCount = 0
  lateinit var firebaseAuth: FirebaseAuth
  lateinit var database: FirebaseDatabase

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()

    sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    firebaseAuth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance()

    startCountingSteps()
  }

  private fun startCountingSteps() {
    stepSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
  }

  override fun onDestroy() {
    super.onDestroy()
    sensorManager.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent?) {
    event?.let {
      if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
        stepCount = it.values[0].toInt()
        saveStepCountToDatabase(stepCount)
      }
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    TODO("Not yet implemented")
  }

  fun saveStepCountToDatabase(newSteps: Int) {
    val userId = firebaseAuth.currentUser?.uid
    if (userId != null) {
      val stepsRefTotal = database.reference.child("users").child(userId).child("totalSteps")
      stepsRefTotal.addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              val currentSteps = dataSnapshot.getValue(Int::class.java) ?: 0
              val totalSteps = currentSteps + newSteps
              stepsRefTotal.setValue(totalSteps)
            }

            override fun onCancelled(databaseError: DatabaseError) {
              // add code when failing to access database
            }
          })

      val d = Date()
      val s: CharSequence = DateFormat.format("MMMM d, yyyy ", d.getTime())
      val stepsRef = database.reference.child("users").child(userId).child("dailySteps $s")
      stepsRef.addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              val currentSteps = dataSnapshot.getValue(Int::class.java) ?: 0
              val totalSteps = currentSteps + newSteps
              stepsRef.setValue(totalSteps)
            }

            override fun onCancelled(databaseError: DatabaseError) {
              // add code when failing to access database
            }
          })
    }
  }
}
