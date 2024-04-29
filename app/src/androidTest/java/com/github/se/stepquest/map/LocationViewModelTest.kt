package com.github.se.stepquest.map

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.location.*
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationViewModelTest {

  @get:Rule val composeTestRule = createComposeRule()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule val mockkRule = MockKRule(this)

  // Declare vm as a public variable
  private lateinit var locationViewModel: LocationViewModel
  private lateinit var context: Context

  @Before
  fun setup() {
    locationViewModel = LocationViewModel()
    context = ApplicationProvider.getApplicationContext<Context>()
  }

  @Test
  fun appendCurrentLocationToAllocationsTest_withupdate() {
    val allocations = listOf(LocationDetails(10.0, 20.0))
    val currentLocation = LocationDetails(40.0, 50.0)
    val locationUpdated = false

    val result =
        locationViewModel.appendCurrentLocationToAllocations(
            allocations, currentLocation, locationUpdated)
    assertNotNull(result)
    assertEquals(allocations + currentLocation, result!!.first)
    assertEquals(true, result.second)
  }

  @Test
  fun appendCurrentLocationToAllocationsTest_withoutupdate() {
    val allocations = listOf(LocationDetails(10.0, 20.0))
    val currentLocation = LocationDetails(15.0, 25.0) // Close to the last location
    val locationUpdated = true

    val result =
        locationViewModel.appendCurrentLocationToAllocations(
            allocations, currentLocation, locationUpdated)

    assertNull(result)
  }

  @Test
  fun cleanAllocationsTest() {
    locationViewModel._allocations = MutableLiveData(listOf(LocationDetails(10.0, 20.0)))
    locationViewModel.cleanAllocations()
    runBlocking {
      delay(1000) // Wait for 1 second (adjust time as needed)
      val alllocation = locationViewModel.getAllocations()
      assertNull(alllocation)
    }
  }

  @Test
  fun onPauseTest() {
    locationViewModel.locationUpdated.postValue(true)
    val locationCallback = mockk<LocationCallback>(relaxed = true)
    var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    locationViewModel.fusedLocationClient = fusedLocationClient
    locationViewModel.locationCallback = locationCallback
    locationViewModel.onPause()
    assert(locationViewModel.locationUpdated.value == false)
  }
}
