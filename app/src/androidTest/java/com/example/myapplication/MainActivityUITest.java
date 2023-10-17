package com.example.myapplication;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {

    @Rule
    public ActivityScenarioRule<MainActivity2> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity2.class);

    @Test
    public void homeUIElementTest() {
        ViewInteraction imageButton = onView(
                allOf(withId(R.id.search_home),
                        withParent(withParent(withId(R.id.nav_host))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.settingsButton),
                        withParent(withParent(withId(R.id.nav_host))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView), withText("Newsman"),
                        withParent(withParent(withId(R.id.nav_host))),
                        isDisplayed()));
        textView.check(matches(withText("Newsman")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView), withText("Newsman"),
                        withParent(withParent(withId(R.id.nav_host))),
                        isDisplayed()));
        textView2.check(matches(withText("Newsman")));
    }

    @Test
    public void settingsUIElementTest() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.settingsButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction textView = onView(
                allOf(withText("Settings"),
                        withParent(allOf(withId(R.id.toolbar),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView.check(matches(withText("Settings")));

        ViewInteraction textView2 = onView(
                allOf(withId(android.R.id.title), withText("News Preference"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText("News Preference")));

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.title), withText("Language"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView3.check(matches(withText("Language")));

        ViewInteraction textView4 = onView(
                allOf(withId(android.R.id.title), withText("Region"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView4.check(matches(withText("Region")));

        ViewInteraction textView5 = onView(
                allOf(withId(android.R.id.title), withText("Theme"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView5.check(matches(withText("Theme")));
    }

    @Test
    public void mainActivity2Test() {
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.search_home),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(androidx.appcompat.R.id.search_close_btn), withContentDescription("Clear query"),
                        withParent(allOf(withId(androidx.appcompat.R.id.search_plate),
                                withParent(withId(androidx.appcompat.R.id.search_edit_frame)))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction autoCompleteTextView = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("Search�"),
                        withParent(allOf(withId(androidx.appcompat.R.id.search_plate),
                                withParent(withId(androidx.appcompat.R.id.search_edit_frame)))),
                        isDisplayed()));
        autoCompleteTextView.check(matches(isDisplayed()));

        ViewInteraction autoCompleteTextView2 = onView(
                allOf(withId(androidx.appcompat.R.id.search_src_text), withText("Search�"),
                        withParent(allOf(withId(androidx.appcompat.R.id.search_plate),
                                withParent(withId(androidx.appcompat.R.id.search_edit_frame)))),
                        isDisplayed()));
        autoCompleteTextView2.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
