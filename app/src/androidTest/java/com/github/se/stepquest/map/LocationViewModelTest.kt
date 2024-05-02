package com.github.se.stepquest.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
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
    MockKAnnotations.init(this)
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

  @Test
  fun test_addNewCheckpoint() {
    val c = mutableListOf<Checkpoint>()

    val lvm = mockk<LocationViewModel>(relaxed = true) {
      every { currentLocation } returns mockk() {
        every { value } returns LocationDetails(1.0, 1.0) andThen LocationDetails(2.0, 2.0)
      }
      every { addNewCheckpoint(any()) } re{
        val newCheckpointList = checkpoints.value?.toMutableList() ?: mutableListOf()
        val newCheckpoint = Checkpoint(name, currentLocation.value!!)
        newCheckpointList.add(newCheckpoint)
        checkpoints.value = newCheckpointList
      }
    }

    val list = lvm.addNewCheckpoint("testName", mutableListOf())

    // Verify if the checkpoint was added
    assertEquals(1, list.size)
    assertEquals("testName", list[0].name)
    assertEquals(1.0, list[0].location.latitude, 1e-3)
    assertEquals(1.0, list[0].location.longitude, 1e-3)
  }
}
