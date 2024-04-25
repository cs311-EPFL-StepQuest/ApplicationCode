package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.stepquest.ui.navigation.NavigationActions
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseLoadingScreenTest {

  private lateinit var navigationActions: NavigationActions
  private lateinit var context: Context

  @get:Rule val composeTestRule = createComposeRule()

  @Before
  fun setup() {
    navigationActions = mockk(relaxed = true)
    context = mockk(relaxed = true)
  }

  @Test
  fun displayWaitingForDatabase() {

    composeTestRule.setContent { DatabaseLoadingScreen(navigationActions, context) }

    composeTestRule.onNodeWithText("Waiting for database...").assertIsDisplayed()
  }

  /*@Test
  fun navigationIfNewPlayer() {
      composeTestRule.setContent {
          DatabaseLoadingScreen(navigationActions, context)
      }
  }*/
}
