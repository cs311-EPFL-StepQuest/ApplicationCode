package com.github.se.stepquest.map

import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.se.stepquest.map.LocationViewModel
import com.github.se.stepquest.screens.MapScreen
import com.github.se.stepquest.map.Map
import com.github.se.stepquest.ui.navigation.NavigationActions
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import io.mockk.Called
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.function.Predicate.not

@RunWith(AndroidJUnit4::class)
class MapTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

    @get:Rule val composeTestRule = createComposeRule()

    // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
    @get:Rule val mockkRule = MockKRule(this)

    // Relaxed mocks methods have a default implementation returning values
//    @RelaxedMockK lateinit var mockNavActions: NavigationActions

    // Declare vm as a public variable
    lateinit var vm: LocationViewModel

    @Before
    fun testSetup() {
        composeTestRule.setContent {
            // Provide mocked CompositionLocal value for LocalContext
            val mockContext = mockk<Context>()
            every { mockContext.applicationContext } returns mockContext
            CompositionLocalProvider(LocalContext provides mockContext) {
                // Initialize LocationViewModel
                val vm = LocationViewModel()

                // Set content with initialized LocationViewModel
                Map(vm)
            }
        }
    }

    @Test
    fun createRouteButton() {
        onComposeScreen<MapScreen>(composeTestRule) {
            createRouteButton {
                assertHasClickAction()
                performClick()
            }
        }

        // Step 3: Verify actions after button click
        // For example, if permissions are already granted, startLocationUpdates should be called
        verify(exactly = 1) { vm.startLocationUpdates(any()) }
    }

}

