package com.github.se.stepquest.map

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.stepquest.R
import org.junit.Assert.*
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun map() {
        composeTestRule.setContent {
            Map()
        }
        // Verify that the GoogleMap composable is present
        composeTestRule.onNodeWithTag("GoogleMap").assertExists()
        // Check if GoogleMap composable is displayed
        composeTestRule.onNodeWithTag("GoogleMap").assertIsDisplayed()

    }
}