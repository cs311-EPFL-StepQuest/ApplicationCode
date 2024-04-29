package com.github.se.stepquest

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.github.se.stepquest.ui.theme.StepQuestTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.mockk.coEvery
import io.mockk.every
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class ProfilePageLayoutTest {

  @get:Rule val composeTestRule = createComposeRule()
  @get:Rule val mockkRule = MockKRule(this)

  @Test
  fun display_profile_layout() {
    composeTestRule.setContent {
      StepQuestTheme { ProfilePageLayout(navigationActions = mockk(relaxed = true)) }
    }

    // Assertions
    // Check if the "Settings" image is displayed
    composeTestRule.onNodeWithContentDescription("Settings").assertIsDisplayed()
    // Check if the "Profile" text is displayed
    composeTestRule.onNodeWithText("Profile").assertIsDisplayed()
    // Check if the username is displayed
    composeTestRule.onNodeWithText("No name").assertIsDisplayed()
    // Check if the "Total Steps" text is displayed
    composeTestRule.onNodeWithText("Total Steps: 0").assertIsDisplayed()
    // Check if the "Achievements" text is displayed and clickable
    composeTestRule.onNodeWithText("Achievements: 5").apply { assertIsDisplayed() }
    // Check if the "Friends List" button is displayed and has click action
    composeTestRule.onNodeWithText("Friends List").apply {
      assertIsDisplayed()
      assertHasClickAction()
    }
  }

  @Test
  fun firebase_access_tests_for_profile() {
    val mockFirebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    val mockFirebaseUser = mockk<FirebaseUser>(relaxed = true)
    val mockDatabase = mockk<FirebaseDatabase>(relaxed = true)
    val mockDatabaseRef = mockk<DatabaseReference>(relaxed = true)
    val mockStepsRef = mockk<DatabaseReference>(relaxed = true)
    val mockUsernameRef = mockk<DatabaseReference>(relaxed = true)

    val mockDataSnapshot = mockk<DataSnapshot>(relaxed = true)
    every { mockFirebaseAuth.currentUser } returns mockFirebaseUser
    every { mockFirebaseUser.uid } returns "testUid"
    every { mockFirebaseUser.photoUrl } returns mockk()
    every { mockDatabase.reference.child("users") } returns mockDatabaseRef
    every { mockFirebaseAuth.currentUser?.let { mockDatabaseRef.child(it.uid).child("totalSteps") } } returns mockStepsRef
    every { mockFirebaseAuth.currentUser?.let { mockDatabaseRef.child(it.uid).child("username") } } returns mockUsernameRef
    // Set up mock ValueEventListener for totalSteps
    val stepsListenerSlot = slot<ValueEventListener>()
    every { mockStepsRef.addListenerForSingleValueEvent(capture(stepsListenerSlot)) } answers {
      stepsListenerSlot.captured.onDataChange(mockDataSnapshot)
    }

    // Set up mock ValueEventListener for username
    val usernameListenerSlot = slot<ValueEventListener>()
    every { mockUsernameRef.addListenerForSingleValueEvent(capture(usernameListenerSlot)) } answers {
      usernameListenerSlot.captured.onDataChange(mockDataSnapshot)
    }
    every { mockDataSnapshot.getValue(Int::class.java) } returns 100 // Mock total steps value
    every { mockDataSnapshot.getValue(String::class.java) } returns "Test User" // Mock username value

    composeTestRule.setContent {
      StepQuestTheme { ProfilePageLayout(navigationActions = mockk(relaxed = true)) }
    }

    // Verify UI components
    composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
    composeTestRule.onNodeWithText("Total Steps: 100").assertIsDisplayed()

  }
}
