package com.github.se.stepquest.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class RouteProgressionKtTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun routeProgression_displaysCorrectRouteLength() {
    val routeLength = 10f
    composeTestRule.setContent { RouteProgression({}, routeLength, 0) }

    composeTestRule.onNodeWithText("Route length: $routeLength km").assertExists()
  }

  @Test
  fun routeProgression_displaysCorrectNumberOfCheckpoints() {
    val numCheckpoints = 5
    composeTestRule.setContent { RouteProgression({}, 0f, numCheckpoints) }

    composeTestRule.onNodeWithText("Number of checkpoints: $numCheckpoints").assertExists()
  }

  @Test
  fun routeProgression_displaysCorrectReward() {
    val routeLength = 10f
    val reward = (routeLength * 100).toInt()
    composeTestRule.setContent { RouteProgression({}, routeLength, 0) }

    composeTestRule.onNodeWithText("Reward: $reward points").assertExists()
  }

  @Test
  fun routeProgression_displayCloseButton() {
    composeTestRule.setContent { RouteProgression({}, 0f, 0) }

    composeTestRule.onNodeWithContentDescription("Close").assertExists()
  }

  @Test
  fun routeProgression_displayFinishButton() {
    composeTestRule.setContent { RouteProgression({}, 0f, 0) }

    composeTestRule.onNodeWithText("Finish").assertExists()
  }

  @Test
  fun routeProgression_dismissesDialog_onCloseButtonClick() {
    var dialogDismissed = true
    composeTestRule.setContent { RouteProgression({ dialogDismissed = false }, 0f, 0) }

    composeTestRule.onNodeWithContentDescription("Close").performClick()

    assert(!dialogDismissed)
  }

  @Test
  fun routeProgression_dismissesDialog_onFinishButtonClick() {
    var dialogDismissed = true
    composeTestRule.setContent { RouteProgression({ dialogDismissed = false }, 0f, 0) }

    composeTestRule.onNodeWithText("Finish").performClick()

    assert(!dialogDismissed)
  }
}
