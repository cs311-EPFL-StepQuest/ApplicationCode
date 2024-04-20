package com.github.se.stepquest.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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
  fun testInitialUIState() {
    composeTestRule.setContent { Map() }

    composeTestRule.onNodeWithTag("GoogleMap").assertExists()
    composeTestRule.onNodeWithContentDescription("Add checkpoint").assertExists()
  }

  @Test
  fun testDialogVisibility() {
    composeTestRule.setContent { Map() }

    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()

    composeTestRule.onNodeWithText("New Checkpoint").assertExists()
  }

  @Test
  fun testDialogContents() {
    composeTestRule.setContent { Map() }

    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()

    composeTestRule.onNodeWithText("New Checkpoint").assertExists()
    composeTestRule.onNodeWithText("Checkpoint name").assertExists()
    composeTestRule.onNodeWithText("Title").assertExists()
  }

  @Test
  fun testDialogDismissal() {
    composeTestRule.setContent { Map() }

    composeTestRule.onNodeWithContentDescription("Add checkpoint").performClick()

    composeTestRule.onNodeWithContentDescription("Close").performClick()

    composeTestRule.onNodeWithText("New Checkpoint").assertDoesNotExist()
  }
}
