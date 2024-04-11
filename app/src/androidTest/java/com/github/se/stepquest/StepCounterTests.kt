package com.github.se.stepquest

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.github.se.stepquest.services.StepCounterService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StepCounterTests {

  @Mock private lateinit var sensorManager: SensorManager

  @Mock private lateinit var stepSensor: Sensor

  @Mock private lateinit var firebaseAuth: FirebaseAuth

  @Mock private lateinit var database: FirebaseDatabase

  @Mock private lateinit var reference: DatabaseReference

  private lateinit var service: StepCounterService

  @Before
  fun setUp() {
    `when`(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)).thenReturn(stepSensor)
    `when`(firebaseAuth.currentUser).thenReturn(mock(FirebaseAuth::class.java).currentUser)
    `when`(database.reference).thenReturn(reference)
    `when`(reference.child(anyString())).thenReturn(reference)
    service = StepCounterService()
    service.sensorManager = sensorManager
    service.stepSensor = stepSensor
    service.firebaseAuth = firebaseAuth
    service.database = database
  }

  @Test
  fun onCreate_registersSensorListener() {
    service.onCreate()
    verify(sensorManager).registerListener(service, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
  }

  @Test
  fun onDestroy_unregistersSensorListener() {
    service.onDestroy()
    verify(sensorManager).unregisterListener(service)
  }

  @Test
  fun onSensorChanged_savesStepCountToDatabase() {
    val event = mock(SensorEvent::class.java)
    `when`(event.sensor).thenReturn(stepSensor)
    `when`(event.values).thenReturn(floatArrayOf(100f))
    service.onSensorChanged(event)
    verify(database.reference.child("users").child(anyString()).child("steps")).setValue(100)
  }

  /*@Test
  fun saveStepCountToDatabase_updatesStepCount() {
      `when`(reference.getValue(Int::class.java)).thenReturn(mock(DataSnapshot::class.java))
      `when`(reference.getValue(Int::class.java)!!.value).thenReturn(50)
      service.saveStepCountToDatabase(50)
      verify(reference).setValue(100)
  }

  @Test
  fun saveStepCountToDatabase_handlesNullDataSnapshot() {
      `when`(reference.getValue(Int::class.java)).thenReturn(null)
      service.saveStepCountToDatabase(50)
      verify(reference).setValue(50)
  }*/
}
