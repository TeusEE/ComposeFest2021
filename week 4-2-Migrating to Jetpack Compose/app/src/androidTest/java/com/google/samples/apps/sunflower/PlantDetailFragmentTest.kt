package com.google.samples.apps.sunflower

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.Navigation.findNavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.testing.TestListenableWorkerBuilder
import com.google.samples.apps.sunflower.utilities.chooser
import com.google.samples.apps.sunflower.utilities.testPlant
import com.google.samples.apps.sunflower.workers.SeedDatabaseWorker
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantDetailFragmentTest {

    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<GardenActivity>()

    // Note that keeping these references is only safe if the activity is not recreated.
    private lateinit var activity: ComponentActivity

    @Before
    fun jumpToPlantDetailFragment() {
        populateDatabase()

        composeTestRule.activityRule.scenario.onActivity { gardenActivity ->
            activity = gardenActivity

            val bundle = Bundle().apply { putString("plantId", "malus-pumila") }
            findNavController(activity, R.id.nav_host).navigate(R.id.plant_detail_fragment, bundle)
        }
    }

    @Test
    fun testPlantName() {
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
    }

    @Test
    fun testShareTextIntent() {
        val shareText = activity.getString(R.string.share_text_plant, testPlant.name)

        Intents.init()
        onView(withId(R.id.action_share)).perform(click())
        intended(
            chooser(
                allOf(
                    hasAction(Intent.ACTION_SEND),
                    hasType("text/plain"),
                    hasExtra(Intent.EXTRA_TEXT, shareText)
                )
            )
        )
        Intents.release()

        // dismiss the Share Dialog
        InstrumentationRegistry.getInstrumentation()
            .uiAutomation
            .performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
    }

    private fun populateDatabase() {
        val request = TestListenableWorkerBuilder<SeedDatabaseWorker>(
            InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        ).build()
        runBlocking {
            request.doWork()
        }
    }
}
