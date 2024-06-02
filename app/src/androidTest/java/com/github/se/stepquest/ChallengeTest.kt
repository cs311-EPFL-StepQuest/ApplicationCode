package com.github.se.stepquest

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.github.se.stepquest.data.model.ChallengeData
import com.github.se.stepquest.data.model.ChallengeProgression
import com.github.se.stepquest.data.model.ChallengeType
import com.github.se.stepquest.screens.HomeScreen
import com.github.se.stepquest.services.createChallengeItem
import com.github.se.stepquest.services.someChallengesCompleted
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.github.se.stepquest.ui.theme.StepQuestTheme
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChallengeTest {

  @Test
  fun challengeDataIsCorrect() {
    val challenge =
        ChallengeData(
            "testUuid",
            ChallengeType.REGULAR_STEP_CHALLENGE,
            1000,
            10,
            7,
            "2021-01-01",
            "testChallengedUsername",
            "testChallengedUserUuid",
            "testSenderUsername",
            "testSenderUserUuid",
            ChallengeProgression("testChallengedUserUuid", 0, 0),
            ChallengeProgression("testSenderUserUuid", 0, 0))

    assertEquals("testUuid", challenge._uuid)
    assertEquals(ChallengeType.REGULAR_STEP_CHALLENGE, challenge._type)
    assertEquals(1000, challenge._stepsToMake)
    assertEquals(10, challenge._kilometersToWalk)
    assertEquals(7, challenge._daysToComplete)
    assertEquals("2021-01-01", challenge._dateTime)
    assertEquals("testChallengedUsername", challenge._challengedUsername)
    assertEquals("testChallengedUserUuid", challenge._challengedUserUuid)
    assertEquals("testSenderUsername", challenge._senderUsername)
    assertEquals("testSenderUserUuid", challenge._senderUserUuid)
  }

  @Test
  fun testCompletionFunctions() {
    var challenge =
        createChallengeItem(
            "testUserId",
            "testUserName",
            "testUserId",
            "testUserName",
            ChallengeType.REGULAR_STEP_CHALLENGE)
    var result = ChallengeType.REGULAR_STEP_CHALLENGE.completionFunction(challenge)
    assertEquals(false, result)
    challenge =
        createChallengeItem(
            "testUserId",
            "testUserName",
            "testUserId",
            "testUserName",
            ChallengeType.DAILY_STEP_CHALLENGE)
    result = ChallengeType.DAILY_STEP_CHALLENGE.completionFunction(challenge)
    assertEquals(false, result)
  }

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun testChallengeCompletionPopUp() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    composeTestRule.setContent {
      StepQuestTheme {
        HomeScreen(
            navigationActions = NavigationActions(navController = navController),
            "testUid",
            context)
      }
    }

    mockkStatic("com.github.se.stepquest.services.ChallengesServiceKt")
    every { someChallengesCompleted(any(), any()) } answers
        {
          secondArg<(Boolean) -> Unit>().invoke(true)
        }
    //        composeTestRule.onNodeWithTag("main congratulation dialog text").assertIsDisplayed()
    //        composeTestRule.onNodeWithTag("Confirm button").assertIsDisplayed()
  }

  @Test fun testDeleteChallenge() {}
}
