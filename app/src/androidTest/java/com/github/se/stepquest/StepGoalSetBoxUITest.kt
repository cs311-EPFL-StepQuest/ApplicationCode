package com.github.se.stepquest

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.se.stepquest.ui.theme.StepQuestTheme
import org.junit.Rule
import org.junit.Test

class SetStepGoalsDialogTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun dummy_is_correctly_displayed() {
    composeTestRule.setContent { StepQuestTheme { ProgressionPageLayout() } }
    // Verify that the character image is displayed
    composeTestRule.onNodeWithContentDescription("tempCharacter").assertIsDisplayed()

    // Verify that the "Daily Steps" text is displayed
    composeTestRule.onNodeWithText("Daily Steps : 0/5000").assertIsDisplayed()

    // Verify that the "Weekly Steps" text is displayed
    composeTestRule.onNodeWithText("Weekly Steps : 0/35000").assertIsDisplayed()

    // Verify that the "Set new step goals" button is displayed
    composeTestRule.onNodeWithText("Set new step goals").assertIsDisplayed()
  }

  @Test
  fun dialog_opens_correctly() {
    composeTestRule.setContent { StepQuestTheme { ProgressionPageLayout() } }
    // Click on the "Set new step goals" button
    composeTestRule.onNodeWithText("Set new step goals").performClick()

    // Check if "Daily steps" from the dialog box is here to see if it opened
    composeTestRule.onNodeWithText("Daily steps").assertIsDisplayed()
  }

  @Test
  fun dialog_ui_elements_displayed_correctly() {
    // Start the dialog
    composeTestRule.setContent {
      StepQuestTheme {
        SetStepGoalsDialog(
            onDismiss = { /* Not used in this test */},
            onConfirm = { _, _ -> /* Not used in this test */ })
      }
    }

    // Verify that the dialog title is displayed
    composeTestRule.onNodeWithText("Set New Step Goals").assertIsDisplayed()

    // Verify that the close icon button is displayed
    composeTestRule.onNodeWithContentDescription("Close").assertIsDisplayed()

    // Verify that the "Daily steps" text is displayed
    composeTestRule.onNodeWithText("Daily steps").assertIsDisplayed()

    // Verify that the text field for entering daily steps is displayed
    composeTestRule.onNodeWithTag("daily_steps_setter").assertIsDisplayed()

    // Verify that the "Confirm" button is displayed
    composeTestRule.onNodeWithText("Confirm").assertIsDisplayed()
  }

  @Test
  fun dialog_confirm_button_click_simple() {
    var dailyStepGoal = 0
    var weeklyStepGoal = 0

    // Start the dialog
    composeTestRule.setContent {
      StepQuestTheme {
        SetStepGoalsDialog(
            onDismiss = { /* Not used in this test */},
            onConfirm = { dailyStep, weeklyStep ->
              dailyStepGoal = dailyStep
              weeklyStepGoal = weeklyStep
            })
      }
    }

    // Enter daily step goal
    composeTestRule.onNodeWithTag("daily_steps_setter").performTextInput("6000")

    // Click on the "Confirm" button
    composeTestRule.onNodeWithText("Confirm").performClick()

    // Verify that the correct daily step goal is passed to the callback
    assert(dailyStepGoal == 6000)

    // Verify that the correct weekly step goal is calculated and passed to the callback
    assert(weeklyStepGoal == 42000) // 6000 * 7
  }

  @Test
  fun dialog_confirm_button_click_lower_bound_test() {
    var dailyStepGoal = 0
    var weeklyStepGoal = 0

    // Start the dialog
    composeTestRule.setContent {
      StepQuestTheme {
        SetStepGoalsDialog(
            onDismiss = { /* Not used in this test */},
            onConfirm = { dailyStep, weeklyStep ->
              dailyStepGoal = dailyStep
              weeklyStepGoal = weeklyStep
            })
      }
    }

    // Enter daily step goal
    composeTestRule.onNodeWithTag("daily_steps_setter").performTextInput("100")

    // Click on the "Confirm" button
    composeTestRule.onNodeWithText("Confirm").performClick()

    // Verify that the correct daily step goal is passed to the callback
    assert(dailyStepGoal == 1000)

    // Verify that the correct weekly step goal is calculated and passed to the callback
    assert(weeklyStepGoal == 7000)
  }

  @Test
  fun dialog_confirm_button_click_upper_bound() {
    var dailyStepGoal = 0
    var weeklyStepGoal = 0

    // Start the dialog
    composeTestRule.setContent {
      StepQuestTheme {
        SetStepGoalsDialog(
            onDismiss = { /* Not used in this test */},
            onConfirm = { dailyStep, weeklyStep ->
              dailyStepGoal = dailyStep
              weeklyStepGoal = weeklyStep
            })
      }
    }

    // Enter daily step goal
    composeTestRule.onNodeWithTag("daily_steps_setter").performTextInput("100000")

    // Click on the "Confirm" button
    composeTestRule.onNodeWithText("Confirm").performClick()

    // Verify that the correct daily step goal is passed to the callback
    assert(dailyStepGoal == 10000)

    // Verify that the correct weekly step goal is calculated and passed to the callback
    assert(weeklyStepGoal == 70000)
  }

  @Test
  fun dialog_confirm_button_click_rounding_check() {
    var dailyStepGoal = 0
    var weeklyStepGoal = 0

    // Start the dialog
    composeTestRule.setContent {
      StepQuestTheme {
        SetStepGoalsDialog(
            onDismiss = { /* Not used in this test */},
            onConfirm = { dailyStep, weeklyStep ->
              dailyStepGoal = dailyStep
              weeklyStepGoal = weeklyStep
            })
      }
    }

    // Enter daily step goal
    composeTestRule.onNodeWithTag("daily_steps_setter").performTextInput("5003")

    // Click on the "Confirm" button
    composeTestRule.onNodeWithText("Confirm").performClick()

    // Verify that the correct daily step goal is passed to the callback
    assert(dailyStepGoal == 5250)

    // Verify that the correct weekly step goal is calculated and passed to the callback
    assert(weeklyStepGoal == 36750)
  }
}