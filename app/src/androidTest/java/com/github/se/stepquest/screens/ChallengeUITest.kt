package com.github.se.stepquest.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.se.stepquest.Routes
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.data.model.ChallengeProgression
import com.github.se.stepquest.data.model.ChallengeType
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.navigation.TopLevelDestination
import com.github.se.stepquest.viewModels.ChallengesState
import com.github.se.stepquest.viewModels.ChallengesViewModel
import com.google.common.base.Verify.verify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@RunWith(AndroidJUnit4::class)
class ChallengesUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockNavigationActions = mockk<NavigationActions>(relaxed = true)
    private val mockViewModel = mockk<ChallengesViewModel>(relaxed = true)

    @Test
    fun testChallengesScreenDisplaysCorrectly() {
        // Arrange
        val challenges = listOf(
            ChallengeData(
                uuid = "1",
                type = ChallengeType.REGULAR_STEP_CHALLENGE,
                stepsToMake = 1000,
                dateTime = "May 28, 2024",
                challengedUsername = "Bob",
                senderUsername = "Alice",
                challengedProgress = ChallengeProgression("user1", 500, 0),
                senderProgress = ChallengeProgression("user2", 300, 0)
            )
        )
        val state = MutableStateFlow(
            ChallengesState(
                challenges = challenges,
                challengeText = "Walk 1000 steps!"
            )
        )
        every { mockViewModel.state } returns state

        // Act
        composeTestRule.setContent {
            ChallengesScreen(
                userId = "testUser",
                navigationActions = mockNavigationActions,
                viewModel = mockViewModel
            )
        }

        // Assert
        composeTestRule.onNodeWithText("Back").assertIsDisplayed()
        composeTestRule.onNodeWithText("Challenges").assertIsDisplayed()
        composeTestRule.onNodeWithText("Challengers: Alice and Bob").assertIsDisplayed()
        composeTestRule.onNodeWithText("Challenge: Walk 1000 steps!").assertIsDisplayed()
        composeTestRule.onNodeWithText("End Date: May 28, 2024").assertIsDisplayed()
    }
}
