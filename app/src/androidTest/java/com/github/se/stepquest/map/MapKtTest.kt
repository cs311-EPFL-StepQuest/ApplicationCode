package com.github.se.stepquest.map

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.stepquest.MainActivity
import com.github.se.stepquest.R
import com.github.se.stepquest.ui.theme.StepQuestTheme
import io.mockk.junit4.MockKRule
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapKtTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  // Declare vm as a public variable
  private lateinit var vm: LocationViewModel

  @Before
  fun setup() {
    vm = LocationViewModel()
  }


  @Test
  fun testPermissionGranted() {
    composeTestRule.setContent { StepQuestTheme { Map(vm) } }
    composeTestRule.onNodeWithTag("createRouteButton").assertIsDisplayed()
    composeTestRule.onNodeWithTag("createRouteButton").assertHasClickAction()
  }
}

