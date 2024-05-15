package com.github.se.stepquest.map

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import io.mockk.every
import io.mockk.mockk
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FollowRouteTest {

  @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

  private lateinit var followRoute: FollowRoute
  private val testDispatcher = TestCoroutineDispatcher()

  @Before
  fun setUp() {
    Dispatchers.setMain(testDispatcher)
    followRoute = FollowRoute()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    testDispatcher.cleanupTestCoroutines()
  }

  @Test
  fun TestcheckIfOnRoute_condition1() = runBlockingTest {
    // Mock dependencies
    val locationViewModel = mockk<LocationViewModel>(relaxed = true)
    val trackpoints =
        listOf(
            LocationDetails(0.0, 0.0), LocationDetails(0.0, 21.00001), LocationDetails(0.0, 40.0))
    val latch = CountDownLatch(1)
    val currentLocationLiveData = MutableLiveData<LocationDetails>()
    every { locationViewModel.currentLocation } returns currentLocationLiveData

    // Set the initial location
    currentLocationLiveData.postValue(LocationDetails(0.0, 0.0))

    // Start the function
    followRoute.checkIfOnRoute(locationViewModel, trackpoints)

    latch.await(1, TimeUnit.SECONDS)
    // Verify the state of userOnRoute
    assertEquals(true, followRoute.userOnRoute.value)

    // Stop the coroutine
    followRoute.stopCheckIfOnRoute()
  }

  @Test
  fun TestcheckIfOnRoute_condition2() = runBlockingTest {
    // Mock dependencies
    val locationViewModel = mockk<LocationViewModel>(relaxed = true)
    val trackpoints = listOf(LocationDetails(0.0, 21.00001), LocationDetails(0.0, 40.0))
    val latch = CountDownLatch(1)
    val currentLocationLiveData = MutableLiveData<LocationDetails>()
    every { locationViewModel.currentLocation } returns currentLocationLiveData

    // Start the function
    followRoute.checkIfOnRoute(locationViewModel, trackpoints)

    // Change location to simulate moving closer to the next point
    currentLocationLiveData.value = LocationDetails(0.0, 21.0)

    latch.await(1, TimeUnit.SECONDS)
    // Verify the state of userOnRoute
    assertEquals(true, followRoute.userOnRoute.value)

    // Stop the coroutine
    followRoute.stopCheckIfOnRoute()
  }

  @Test
  fun TestcheckIfOnRoute_condition3() = runBlockingTest {
    // Mock dependencies
    val locationViewModel = mockk<LocationViewModel>(relaxed = true)
    val trackpoints =
        listOf(
            LocationDetails(0.0, 0.0), LocationDetails(0.0, 21.00001), LocationDetails(0.0, 40.0))
    val latch = CountDownLatch(1)
    val currentLocationLiveData = MutableLiveData<LocationDetails>()
    every { locationViewModel.currentLocation } returns currentLocationLiveData

    // Start the function
    followRoute.checkIfOnRoute(locationViewModel, trackpoints)

    // Change location to simulate moving off the route
    currentLocationLiveData.value = LocationDetails(0.0, 100.0)

    latch.await(1, TimeUnit.SECONDS)
    // Verify the state of userOnRoute
    assertEquals(false, followRoute.userOnRoute.value)

    // Stop the coroutine
    followRoute.stopCheckIfOnRoute()
  }

  @Test
  fun TestcreateTrackpoint() {
    val route =
        listOf(LocationDetails(0.0, 0.0), LocationDetails(0.0, 1.0), LocationDetails(0.0, 2.0))
    val followRoute = FollowRoute()
    val trackpoints = followRoute.createTrackpoint(route, 1.0)
    assert(trackpoints.size == 2)
    assert(trackpoints[0] == LocationDetails(0.0, 1.0))
    assert(trackpoints[1] == LocationDetails(0.0, 2.0))
  }
}
