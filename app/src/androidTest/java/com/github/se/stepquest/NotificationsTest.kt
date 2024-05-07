package com.github.se.stepquest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.github.se.stepquest.screens.NotificationScreen
import org.junit.Rule
import org.junit.Test

class NotificationsTest {
  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun everythingIsDisplayed() {
    composeTestRule.setContent { NotificationScreen(TestUserRepository1()) }
    composeTestRule.onNodeWithTag("Notifications title").assertIsDisplayed()
  }
}
