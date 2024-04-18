package com.github.se.stepquest.map

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.stepquest.ui.theme.StepQuestTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.Manifest
import android.app.Application
import android.renderscript.Allocation
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.performClick
import androidx.core.content.PermissionChecker
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.kaspersky.components.kautomator.common.Environment
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.robolectric.shadows.ShadowApplication


@RunWith(AndroidJUnit4::class)
class MapTest {

  @get:Rule val composeTestRule = createComposeRule()
  // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
  @get:Rule val mockkRule = MockKRule(this)

  // Declare vm as a public variable
  private lateinit var vm: LocationViewModel
  private lateinit var locationViewModel: LocationViewModel
  private lateinit var context: Context
  private lateinit var launcherMultiplePermissions: ActivityResultLauncher<Array<String>>
  val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)

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
    composeTestRule.onNodeWithTag("createRouteButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("createRouteButton").assertHasClickAction()
    composeTestRule.onNodeWithTag("createRouteButton").performClick()

  }


  @Test
  fun locationPermission_grated() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns PermissionChecker.PERMISSION_GRANTED

    locationPermission(locationViewModel, context, launcherMultiplePermissions, permissions)

    verify { locationViewModel.startLocationUpdates(any()) }
  }

  @Test
  fun locationPermission_denied() {
    mockkStatic(PermissionChecker::class)
    every { PermissionChecker.checkSelfPermission(any(), any()) } returns PermissionChecker.PERMISSION_DENIED

    locationPermission(locationViewModel, context, launcherMultiplePermissions, permissions)

    verify { locationViewModel wasNot Called }
  }

  @Test
  fun testUpdateMap() {
    // Mock GoogleMap
    val googleMap = mockk<GoogleMap>()

    // Call the function to be tested
    updateMap(googleMap, vm)
  }

}
