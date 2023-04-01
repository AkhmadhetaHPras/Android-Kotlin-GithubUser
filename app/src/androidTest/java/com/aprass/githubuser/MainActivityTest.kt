package com.aprass.githubuser

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
    private val headerFavorite = "Some People You Might Know"
    private val firstStateHomeText = "Explore Github User"
    private val darkMode = "Dark Mode"

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertFavoritePage() {
        onView(withId(R.id.navigation_favorite)).perform(click())
        onView(withId(R.id.header_title)).check(matches(isDisplayed()))
        onView(withId(R.id.header_title)).check(matches(withText(headerFavorite)))
    }

    @Test
    fun assertHomePage() {
        onView(withId(R.id.navigation_home)).perform(click())
        onView(withId(R.id.text_information)).check(matches(isDisplayed()))
        onView(withId(R.id.text_information)).check(matches(withText(firstStateHomeText)))
    }

    @Test
    fun assertSettingPage() {
        onView(withId(R.id.navigation_settings)).perform(click())
        onView(withId(R.id.switch_theme)).check(matches(isDisplayed()))
        onView(withId(R.id.switch_theme)).check(matches(withText(darkMode)))
    }
}