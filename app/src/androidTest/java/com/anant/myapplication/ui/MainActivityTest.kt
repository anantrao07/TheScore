package com.anant.myapplication.ui

import android.app.Activity
import android.app.Instrumentation
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import com.anant.myapplication.ui.adapter.TeamListAdapter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainActivityTest {

    @get:Rule
     var activityActivityTestRule: ActivityTestRule<MainActivity> =
        ActivityTestRule<MainActivity>(MainActivity::class.java)

    private var mainActivity: MainActivity? = null
    @Before
    fun setUp() {
        mainActivity = activityActivityTestRule.activity
    }

    @Test
    fun  scrollToPosition(){

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        Espresso.onView( ViewMatchers.withId(com.anant.myapplication.R.id.teamListRecyclerView)).perform(
             RecyclerViewActions.actionOnItemAtPosition<TeamListAdapter.TeamListViewHolder>(25, ViewActions.click())
         )
    }
    @After
    fun tearDown() {
        mainActivity = null
    }
}