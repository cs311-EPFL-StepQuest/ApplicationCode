package com.github.se.stepquest.screens

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.Navigation
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.github.se.stepquest.ui.theme.StepQuestTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.platform.Platform
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LeaderboardScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        // Initialize Firebase Admin SDK with emulator settings
        val context = ApplicationProvider.getApplicationContext<Context>()
        val options = FirebaseOptions.Builder()
            .setApplicationId("1:316177260128:android:d6da82112d5626348d2d05")
            .setApiKey("AIzaSyB7BOcOCQ5f-A3HtoXH6O8cynAryQ3zFjE")
            .setDatabaseUrl("http://127.0.0.1:9000/?ns=stepquest-4de5e")
            .build()
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context, options)
        }
    }

    @Test
    fun leaderboardCardsAreDisplayed() {
        // Start the app
        composeTestRule.setContent {
            StepQuestTheme {
                Leaderboards(userId = "LeaderboardTestUid", mockk(relaxed = true))
            }
        }

        composeTestRule.onNodeWithText("Leaderboard").assertIsDisplayed()

        composeTestRule.onNodeWithText("General Leaderboard").assertIsDisplayed()

        composeTestRule.onNodeWithText("Friends Leaderboard").assertIsDisplayed()

    }

    @Test
    fun checkIfTopUserIsCorrect() {
        val database = Firebase.database
        val host = if (Platform.isAndroid) "10.0.2.2" else "localhost"
        database.useEmulator(host, 9000)
        val leaderboardRef = database.reference.child("leaderboard").child("A")
        leaderboardRef.setValue(1000000)
        runBlocking {
            // Wait for the value to be set
            while (true) {
                val snapshot = leaderboardRef.get().await()
                if (snapshot.exists()) {
                    // Value is set, break out of the loop
                    break
                }
            }
        }

        composeTestRule.setContent {
            StepQuestTheme {
                Leaderboards(userId = "BdUmnrMZwraipednJIYXphUlWft2", mockk(relaxed = true))
            }
        }
        composeTestRule.onNodeWithTag("1").assertIsDisplayed()
    }
}