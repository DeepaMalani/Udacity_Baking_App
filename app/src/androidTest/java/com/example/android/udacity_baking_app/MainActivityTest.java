package com.example.android.udacity_baking_app;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Created by Deep on 6/30/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void scrollToPostionHasText() {
        // First, scroll to position.
        onView(withId(R.id.recycler_view_recipe))
                .perform(RecyclerViewActions.scrollToPosition(0));

        // Check that the item has the text.
        String text = "Nutella Pie";
        onView(withText(text)).check(matches(isDisplayed()));


    }

    @Test
    public void clickRecipeItemOpenRecipeSteps() {
        onView(withId(R.id.recycler_view_recipe)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        //Verify that next screen shows
        onView(withId(R.id.recycler_view_recipe_steps)).check(matches(isDisplayed()));

    }


}

