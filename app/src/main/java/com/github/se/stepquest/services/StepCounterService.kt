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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class StepCounterService() : Service(), SensorEventListener {

  private lateinit var sensorManager: SensorManager
  private var stepSensor: Sensor? = null
  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var database: FirebaseDatabase

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()

    sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
    stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    firebaseAuth = FirebaseAuth.getInstance()
    database = FirebaseDatabase.getInstance()

    sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
  }

  override fun onDestroy() {
    super.onDestroy()
    sensorManager.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event?.sensor?.type == Sensor.TYPE_STEP_DETECTOR) {
      saveStepCountToDatabase(1)
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

  private fun saveStepCountToDatabase(newSteps: Int) {
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

      // Store weekly steps
      val calendar = Calendar.getInstance()
      // Set the calendar to the current date
      calendar.time = d
      // Find the start of the week (Monday)
      calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
      val startOfWeek = calendar.time
      // Find the end of the week (Sunday)
      calendar.add(Calendar.DAY_OF_WEEK, 6)
      val endOfWeek = calendar.time
      // Format the dates
      val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
      val startFormatted = dateFormat.format(startOfWeek)
      val endFormatted = dateFormat.format(endOfWeek)
      // Combine formatted dates
      val current_period = "$startFormatted - $endFormatted"

      val stepsRefWeek = database.reference.child("users").child(userId).child("weeklySteps")
      stepsRefWeek.addListenerForSingleValueEvent(
          object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
              if (!dataSnapshot.hasChild(current_period)) {
                stepsRefWeek.child(current_period).setValue(newSteps)
                // Clean all other weeks data
                for (child in dataSnapshot.children) {
                  if (child.key != current_period) {
                    stepsRefWeek.child(child.key!!).removeValue()
                  }
                }
              } else {
                val currentSteps = dataSnapshot.child(current_period).getValue(Int::class.java) ?: 0
                val totalSteps = currentSteps + newSteps
                stepsRefWeek.child(current_period).setValue(totalSteps)
              }
            }

            override fun onCancelled(databaseError: DatabaseError) {
              // add code when failing to access database
            }
          })
    }
  }
}
