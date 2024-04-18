package com.github.se.stepquest.map

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.core.content.PermissionChecker
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.stepquest.ui.theme.StepQuestTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import io.mockk.Called
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationViewModelTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
    @get:Rule
    val mockkRule = MockKRule(this)

    // Declare vm as a public variable
    private lateinit var locationViewModel: LocationViewModel

    @Before
    fun setup() {
        locationViewModel = LocationViewModel()
    }

    @Test
    fun appendCurrentLocationToAllocationsTest_withupdate(){
        val allocations =listOf(LocationDetails(10.0, 20.0))
        val currentLocation = LocationDetails(40.0, 50.0)
        val locationUpdated = false

        val result = locationViewModel.appendCurrentLocationToAllocations(allocations, currentLocation, locationUpdated)

        // Then
        assertNotNull(result)
        assertEquals(allocations + currentLocation, result!!.first)
        assertEquals(true, result.second)
    }

    @Test
    fun appendCurrentLocationToAllocationsTest_withoutupdate(){
        val allocations = listOf(LocationDetails(10.0, 20.0))
        val currentLocation = LocationDetails(15.0, 25.0) // Close to the last location
        val locationUpdated = true

        val result = locationViewModel.appendCurrentLocationToAllocations(allocations, currentLocation, locationUpdated)

        // Then
        assertNull(result)
    }

    @Test
    fun startLocationUpdatesTest() {

    }

}