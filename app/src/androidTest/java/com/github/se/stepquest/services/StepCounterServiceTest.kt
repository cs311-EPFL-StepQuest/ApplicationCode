package com.github.se.stepquest.services

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StepCounterServiceTest {

  @get:Rule val mockkRule = MockKRule(this)

  private lateinit var stepCounterService: StepCounterService
  private lateinit var sensorManager: SensorManager
  private lateinit var stepSensor: Sensor
  private lateinit var firebaseAuth: FirebaseAuth
  private lateinit var database: FirebaseDatabase
  private lateinit var stepsRefTotal: DatabaseReference
  private lateinit var stepsRefDaily: DatabaseReference
  private lateinit var event: SensorEvent
  private lateinit var userRef: DatabaseReference

  private fun mockSensorEvent(): SensorEvent {
    val event = mockk<SensorEvent>()
    val constructor = SensorEvent::class.java.getDeclaredConstructor(Int::class.java)
    constructor.isAccessible = true
    constructor.newInstance(3)
    return event
  }

  @Before
  fun setUp() {
    sensorManager = mockk(relaxed = true)
    stepSensor = mockk(relaxed = true)
    firebaseAuth = mockk()
    database = mockk(relaxed = true)
    stepsRefTotal = mockk(relaxed = true)
    stepsRefDaily = mockk(relaxed = true)
    event = mockSensorEvent()
    event.sensor = stepSensor
      userRef = mockk(relaxed = true)

    every { sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) } returns stepSensor
    every { stepSensor.type } returns Sensor.TYPE_STEP_DETECTOR

    stepCounterService = StepCounterService(sensorManager, firebaseAuth, database, "testUserId")

      every { database.reference } returns
              mockk {
                  every { child("users") } returns
                          mockk {
                              every { child("testUserId") } returns
                                      userRef
                          }
                  every { child(any()) } returns
                          mockk {
                              every { child(any()) } returns
                                      mockk { every { child(any()) } returns stepsRefTotal andThen stepsRefDaily }
                          }
              }
    every { stepsRefTotal.setValue(any()) } returns mockk()
    every { stepsRefDaily.setValue(any()) } returns mockk()
  }

  @Test
  fun testOnCreate() {
      stepCounterService.onCreate()
      every { userRef.addListenerForSingleValueEvent(any()) } answers {
          val listener = arg<ValueEventListener>(0)
          listener.onDataChange(mockk())
      }
    verify {
      sensorManager.registerListener(
          stepCounterService, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
  }

  @Test
  fun testOnDestroy() {
    stepCounterService.onDestroy()
    verify { sensorManager.unregisterListener(stepCounterService) }
  }

  @Test
  fun testOnSensorChangedWithSuccess() {
    every { stepsRefTotal.addListenerForSingleValueEvent(any()) } answers
        {
          val listener = arg<ValueEventListener>(0)
          listener.onDataChange(mockk { every { getValue(Int::class.java) } returns 0 })
        }
    every { stepsRefDaily.addListenerForSingleValueEvent(any()) } answers
        {
          val listener = arg<ValueEventListener>(0)
          listener.onDataChange(mockk { every { getValue(Int::class.java) } returns 0 })
        }

    stepCounterService.onSensorChanged(event)

    verify(exactly = 1) { stepsRefTotal.setValue(any()) }
    verify(exactly = 1) { stepsRefDaily.setValue(any()) }
  }

  @Test
  fun testOnSensorChangedWithFailure() {
    every { stepsRefTotal.addListenerForSingleValueEvent(any()) } answers
        {
          val listener = arg<ValueEventListener>(0)
          listener.onCancelled(mockk { every { message } returns "testError" })
        }
    every { stepsRefDaily.addListenerForSingleValueEvent(any()) } answers
        {
          val listener = arg<ValueEventListener>(0)
          listener.onCancelled(mockk { every { message } returns "testError" })
        }

    mockkStatic(Log::class)

    every { Log.e(any(), any()) } returns 0

    stepCounterService.onSensorChanged(event)

    verify { Log.e(eq("StepCounterService"), eq("Database error: testError")) }
    verify { Log.e(eq("StepCounterService"), eq("Database error: testError")) }
  }

  @Test
  fun testOnBind() {
    assertNull(stepCounterService.onBind(null))
  }
}
