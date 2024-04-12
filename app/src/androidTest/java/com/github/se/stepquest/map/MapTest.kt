package com.github.se.stepquest.map
//
// import android.content.res.Resources
// import android.content.Context
// import androidx.activity.ComponentActivity
// import androidx.compose.runtime.CompositionLocalProvider
// import androidx.compose.ui.platform.LocalContext
// import androidx.compose.ui.test.junit4.createComposeRule
// import androidx.test.core.app.ActivityScenario
// import androidx.test.core.app.ApplicationProvider
// import androidx.test.espresso.core.internal.deps.guava.base.Joiner.on
// import androidx.test.ext.junit.runners.AndroidJUnit4
// import com.github.se.stepquest.screens.MapScreen
// import com.google.common.base.CharMatcher.any
//// import com.google.common.io.Resources
// import com.kaspersky.components.composesupport.config.withComposeSupport
// import com.kaspersky.kaspresso.kaspresso.Kaspresso
// import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
// import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
// import io.mockk.every
// import io.mockk.impl.annotations.RelaxedMockK
// import io.mockk.junit4.MockKRule
// import io.mockk.mockk
// import io.mockk.verify
// import org.junit.Assert.*
// import org.junit.Before
// import org.junit.Rule
// import org.junit.Test
// import org.junit.runner.RunWith
// import org.mockito.Mock
// import org.mockito.Mockito.mock
// import org.mockito.MockitoAnnotations
//
// @RunWith(AndroidJUnit4::class)
// class MapTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {
//
//    @get:Rule val composeTestRule = createComposeRule()
//
//    // This rule automatic initializes lateinit properties with @MockK, @RelaxedMockK, etc.
//    @get:Rule val mockkRule = MockKRule(this)
//
//    // Relaxed mocks methods have a default implementation returning values
////    @RelaxedMockK lateinit var mockNavActions: NavigationActions
//
//    // Declare vm as a public variable
//    lateinit var vm: LocationViewModel
//
////    @Mock
////    @RelaxedMockK lateinit var mockContext: Context
//    lateinit var testContext: Context
//    // Launch a ComponentActivity using ActivityScenario
//    lateinit var activityScenario: ActivityScenario<ComponentActivity>
//
//
//
//    @Before
//    fun testSetup() {
//        vm = LocationViewModel()
//        activityScenario = ActivityScenario.launch(ComponentActivity::class.java)
//
//        // Set up your view model or other dependencies
//        activityScenario.onActivity { activity ->
//            // Call your function with the activity's context
//            vm.startLocationUpdates(activity)
//        }
//
////        mockContext = mockk()
////
////        // Mocking getResources() method of Context
////        every { mockContext.resources } returns mockk<Resources>()
//
//        // Use ApplicationProvider to get a test context
//        testContext = ApplicationProvider.getApplicationContext<Context>()
//
////        composeTestRule.setContent { Map(vm) }
//
//        // Setting up the content with mocked context
//        composeTestRule.setContent {
//            CompositionLocalProvider(LocalContext provides testContext) {
//                Map(vm)
//            }
//        }
//
//    }
//
//
//    @Test
//    fun createRouteButton() {
//        onComposeScreen<MapScreen>(composeTestRule) {
//            createRouteButton {
//                assertHasClickAction()
//                performClick()
//            }
//        }
//
//        verify(exactly = 1) { vm.startLocationUpdates(any()) }
//
////        activityScenario = ActivityScenario.launch(ComponentActivity::class.java)
////
////        // Step 3: Verify actions after button click
////        // For example, if permissions are already granted, startLocationUpdates should be
// called
////        verify(exactly = 1) { activityScenario.onActivity { activity ->
////            // Call your function with the activity
////            vm.startLocationUpdates(activity)
////        } }
//    }
//
// }
