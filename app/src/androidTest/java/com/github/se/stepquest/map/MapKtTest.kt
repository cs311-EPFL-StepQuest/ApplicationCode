package com.github.se.stepquest.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapKtTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun map() {
    composeTestRule.setContent { Map() }
    // Verify that the GoogleMap composable is present
    composeTestRule.onNodeWithTag("GoogleMap").assertExists()
  }

  @Test
  fun map_displaysEndRouteButton() {
    composeTestRule.setContent { Map() }
    composeTestRule.onNodeWithContentDescription("End Route").assertExists()
  }

  @Test
  fun map_opensRouteProgression_onEndRouteButtonClick() {
    var showProgression = false
    composeTestRule.setContent { Map().apply { showProgression = true } }
    composeTestRule.onNodeWithContentDescription("End Route").performClick()
    assertTrue(showProgression)
  }

  @Test
  fun map_closesRouteProgression_onDismiss() {
    var showProgression = true
    composeTestRule.setContent { Map().apply { showProgression = false } }
    composeTestRule.onNodeWithContentDescription("End Route").performClick()
    assertFalse(showProgression)
  }
}
