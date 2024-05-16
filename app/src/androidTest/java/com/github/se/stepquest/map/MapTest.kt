package com.github.se.stepquest.map

import android.Manifest
import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.core.content.PermissionChecker
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.github.se.stepquest.ui.theme.StepQuestTheme
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import io.mockk.Called
import io.mockk.Runs
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapTest {

  @get:Rule val composeTestRule = createComposeRule()

  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule val mockkRule = MockKRule(this)
  @get:Rule
  var permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION)
  @get:Rule
  var permissionRule2 = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

  // Declare vm as a public variable
  private lateinit var vm: LocationViewModel
  private lateinit var locationViewModel: LocationViewModel
  private lateinit var context: Context
  private lateinit var launcherMultiplePermissions: ActivityResultLauncher<Array<String>>
  val permissions =
      arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

  @Before
  fun setup() {
    vm = LocationViewModel()
    locationViewModel = mockk(relaxed = true)
    context = ApplicationProvider.getApplicationContext<Context>()
    launcherMultiplePermissions = mockk(relaxed = true)
  }

  @Test
  fun map() {
    composeTestRule.setContent { StepQuestTheme { Map(vm) } }
    composeTestRule.onNodeWithTag("MapScreen").assertIsDisplayed()
    composeTestRule.onNodeWithTag("GoogleMap").assertIsDisplayed()
    composeTestRule.onNodeWithTag("GoogleMap").assertExists()
    composeTestRule.onNodeWithTag("createRouteButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("createRouteButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("stopRouteButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("stopRouteButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("SearchCleanButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SearchCleanButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("SearchCleanButton").performClick()
    composeTestRule.onNodeWithTag("SearchButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SearchButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("SearchButton").performClick()
    composeTestRule.onNodeWithTag("SearchBar").assertIsDisplayed()
    composeTestRule.onNodeWithTag("SearchBarTextField").assertIsDisplayed()
  }

  @Test
  fun locationPermission_grated() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns
        PermissionChecker.PERMISSION_GRANTED

    locationPermission(locationViewModel, context, launcherMultiplePermissions, permissions)

    verify { locationViewModel.startLocationUpdates(any()) }
  }

  @Test
  fun locationPermission_denied() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns
        PermissionChecker.PERMISSION_DENIED

    locationPermission(locationViewModel, context, launcherMultiplePermissions, permissions)

    verify { locationViewModel wasNot Called }
  }

  @Test
  fun testUpdateMap_singlelocation() {
    // Mock the GoogleMap object
    val googleMap = mockk<GoogleMap>()

    // Mock allocation data
    vm._allocations = MutableLiveData(listOf(LocationDetails(0.0, 0.0)))

    // Mock the addMarker method to return a mock object
    every { googleMap.addMarker(any<MarkerOptions>()) } returns mockk()
    // Mock the moveCamera method to return a mock object
    mockkStatic(CameraUpdateFactory::class)
    val cameraUpdateMock = mockk<CameraUpdate>()
    every { CameraUpdateFactory.newLatLngZoom(any(), any()) } returns cameraUpdateMock
    every { googleMap.moveCamera(any()) } returns mockk()

    // Call the function to test
    updateMap(googleMap, vm)

    // Verify that the appropriate GoogleMap methods are called
    verify {
      googleMap.addMarker(any())
      googleMap.moveCamera(any())
    }
  }

  @Test
  fun testUpdateMap_multiplelocation() {
    // Mock the GoogleMap object
    val googleMap = mockk<GoogleMap>()

    // Mock allocation data
    vm._allocations = MutableLiveData(listOf(LocationDetails(0.0, 0.0), LocationDetails(15.0, 3.0)))
    // Mock the addPolyline method to return a mock object
    every { googleMap.addPolyline(any<PolylineOptions>()) } returns mockk()

    // Call the function to test
    updateMap(googleMap, vm)

    // Verify that the appropriate GoogleMap methods are called
    verify { googleMap.addPolyline(any()) }
  }

  @Test
  fun testUpdateMap_multiplelocation_stop() {
    // Mock the GoogleMap object
    val googleMap = mockk<GoogleMap>()

    // Mock allocation data
    vm._allocations = MutableLiveData(listOf(LocationDetails(0.0, 0.0), LocationDetails(15.0, 3.0)))
    // Mock the addPolyline method to return a mock object
    mockkStatic(BitmapDescriptorFactory::class)
    every { BitmapDescriptorFactory.defaultMarker(any()) } returns mockk()

    // Mock the addMarker method to return a mock object
    val mockedMarker = mockk<Marker>()
    every { googleMap.addMarker(any<MarkerOptions>()) } returns mockedMarker

    // Call the function to test
    updateMap(googleMap, vm, true)

    // Verify that the appropriate GoogleMap methods are called
    verify { googleMap.addMarker(any()) }
  }

  @Test
  fun testInitialUIState() {
    composeTestRule.setContent { Map(vm) }

    composeTestRule.onNodeWithTag("GoogleMap").assertExists()
    composeTestRule.onNodeWithContentDescription("Add checkpoint").assertExists()
  }

  @Test
  fun testDialogVisibility() {
    composeTestRule.setContent { Map(vm) }
    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()
    composeTestRule.onNodeWithText("New Checkpoint").assertExists()
  }

  @Test
  fun testDialogContents() {
    composeTestRule.setContent { Map(vm) }

    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()

    composeTestRule.onNodeWithText("New Checkpoint").assertExists()
    composeTestRule.onNodeWithText("Checkpoint name").assertExists()
    composeTestRule.onNodeWithText("Name:").assertExists()
  }

  @Test
  fun testDialogDismissal() {
    composeTestRule.setContent { Map(vm) }

    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()

    composeTestRule.onNodeWithContentDescription("Close").performClick()

    composeTestRule.onNodeWithText("New Checkpoint").assertDoesNotExist()
  }

  @Test
  fun map_displaysEndRouteButton() {
    composeTestRule.setContent { Map(vm) }
    composeTestRule.onNodeWithTag("stopRouteButton").assertExists()
  }

  @Test
  fun map_opensRouteProgression_onStopRouteButtonClick() {
    var showProgression = false
    composeTestRule.setContent { Map(vm).apply { showProgression = true } }
    composeTestRule.onNodeWithTag("stopRouteButton").performClick()
    assertTrue(showProgression)
  }

  @Test
  fun testCleanGoogleMap_withoutrouteEndMarker() {
    val googleMap = mockk<GoogleMap>()
    every { googleMap.clear() } just Runs
    cleanGoogleMap(googleMap)
    verify { googleMap.clear() }
  }

  @Test
  fun testCleanGoogleMap_withrouteEndMarker() {
    val googleMap = mockk<GoogleMap>()
    val routeEndMarker = mockk<Marker>(relaxed = true)
    every { routeEndMarker.remove() } just Runs
    every { googleMap.clear() } just Runs
    cleanGoogleMap(googleMap, routeEndMarker)
    verify { googleMap.clear() }
    verify { routeEndMarker.remove() }
  }

  @Test
  fun TestpressStartCreateRoute() {
    composeTestRule.setContent { StepQuestTheme { Map(vm) } }
    composeTestRule.onNodeWithTag("createRouteButton").performClick()
  }

  @Test
  fun TestpreeStopCreateRoute() {
    composeTestRule.setContent { StepQuestTheme { Map(vm) } }
    composeTestRule.onNodeWithTag("stopRouteButton").performClick()
  }

  @Test
  fun testInitMap() {
    // Mock the GoogleMap object
    val googleMap = mockk<GoogleMap>(relaxed = true)

    // Call the function to test
    initMap(googleMap)

    // Verify that the appropriate GoogleMap methods are called
    verify {
      googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
      googleMap.uiSettings.isZoomControlsEnabled = true
    }
  }

  @Test
  fun testNumCheckpointsIncreasedAfterCreatingCheckpoint() {
    var numCheckpoints = 0
    composeTestRule.setContent { Map(vm).apply { numCheckpoints += 1 } }

    // Simulate the user interaction to create a checkpoint
    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()
    composeTestRule.onNodeWithText("Name:").performTextInput("Test")
    composeTestRule.onNodeWithText("Confirm").performClick()

    // Assert that numCheckpoints is increased by 1
    assertEquals(numCheckpoints, 1)
  }

  @Test
  fun testIsFollowingRouteIsTrueAfterStartingRoute() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns
        PermissionChecker.PERMISSION_GRANTED

    composeTestRule.setContent { Map(vm) }
    composeTestRule.onNodeWithTag("createRouteButton").performClick()
    assertTrue(vm.isFollowingRoute.value!!)
  }

  @Test
  fun testBackButtonIsDisplayed() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns
        PermissionChecker.PERMISSION_GRANTED

    composeTestRule.setContent { Map(vm) }
    composeTestRule.onNodeWithTag("createRouteButton").performClick()
    composeTestRule.onNodeWithTag("gobackbutton").assertIsDisplayed()
  }

  @Test
  fun testBackButtonDisappearsAfterClickingOnIt() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns
        PermissionChecker.PERMISSION_GRANTED

    composeTestRule.setContent { Map(vm) }
    composeTestRule.onNodeWithTag("createRouteButton").performClick()
    composeTestRule.onNodeWithTag("gobackbutton").performClick()
    composeTestRule.onNodeWithTag("gobackbutton").assertDoesNotExist()
  }
}
