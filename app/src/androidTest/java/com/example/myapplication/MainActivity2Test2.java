package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivity2Test2 {

    @Rule
    public ActivityScenarioRule<MainActivity2> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void mainActivity2Test2() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.textview_news_title_home),
                        withParent(withParent(withId(R.id.recycler_view_home))),
                        isDisplayed()));
    }
}
