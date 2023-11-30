package com.example.pantrypal;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pantrypal.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class InstrumentedTest1 {

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void checkIfMainTextIsVisible() {
        Espresso.onView(ViewMatchers.withId(R.id.textview_main))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void checkMainTextContent() {
        // Check if MainActivity is displayed and if the content is "Welcome to Pantry Pal!"
        Espresso.onView(ViewMatchers.withId(R.id.textview_main))
                .check(ViewAssertions.matches(ViewMatchers.withText("Welcome to the Pantry Pal!")));
    }
}