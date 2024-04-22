package com.github.se.stepquest.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.junit4.MockKRule
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

  @Before
  fun setup() {
    locationViewModel = LocationViewModel()
  }

  @Test
  fun appendCurrentLocationToAllocationsTest_withupdate() {
    val allocations = listOf(LocationDetails(10.0, 20.0))
    val currentLocation = LocationDetails(40.0, 50.0)
    val locationUpdated = false

    val result =
        locationViewModel.appendCurrentLocationToAllocations(
            allocations, currentLocation, locationUpdated)

    // Then
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

    // Then
    assertNull(result)
  }

  @Test fun startLocationUpdatesTest() {}
}
