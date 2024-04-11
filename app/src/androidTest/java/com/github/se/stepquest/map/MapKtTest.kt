package com.github.se.stepquest.map

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
}
