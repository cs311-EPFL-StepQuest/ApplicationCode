package com.github.se.stepquest.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.text.format.DateFormat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class StepCounterService(
    private var sensorManager: SensorManager? = null,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val userId: String? = firebaseAuth.currentUser?.uid
) : Service(), SensorEventListener {

  private var stepSensor: Sensor? = null

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()

    if (sensorManager == null) {
      sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    stepSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

    if (sensorManager != null) {
      sensorManager!!.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    if (userId != null) {
      cleanUpOldSteps(userId)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    sensorManager!!.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
      saveStepCountToDatabase(1)
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

  private fun saveStepCountToDatabase(newSteps: Int) {

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
              Log.e("StepCounterService", "Database error: ${databaseError.message}")
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
              Log.e("StepCounterService", "Database error: ${databaseError.message}")
            }
          })
    }
  }

  fun cleanUpOldSteps(userId: String) {
    // clean up old daily steps
    val userRef = database.reference.child("users").child(userId)
    val d = Date()
    val s: CharSequence = DateFormat.format("MMMM d, yyyy ", d.getTime())

    userRef.addListenerForSingleValueEvent(
        object : ValueEventListener {
          override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (child in dataSnapshot.children) {
              val nodeName = child.key
              if (nodeName != null &&
                  nodeName.contains("dailySteps") &&
                  nodeName != "dailySteps $s") {
                userRef.child(nodeName).removeValue()
              }
            }
          }

          override fun onCancelled(databaseError: DatabaseError) {
            // Handle error
          }
        })
  }
}
