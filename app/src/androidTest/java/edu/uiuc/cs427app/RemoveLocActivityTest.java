package edu.uiuc.cs427app;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.Espresso;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.AllOf.allOf;

import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import edu.uiuc.cs427app.db.DatabaseHelper;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RemoveLocActivityTest {
    @Before
    public void LaunchLoginActivity() {
        ActivityScenario.launch(LoginActivity.class);
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void testCityAddingAndRemoval() {
        String username = "wpmayor2";
        String password = "123";

        /*signup*/

        onView(withId(R.id.signupLink)).perform(click());

        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.usernameEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(username)));

        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.passwordEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(password)));

        onView(withId(R.id.signupButton))
                .perform(click());

        /*login*/

        onView(withId(R.id.signupLink)).perform(click());

        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.usernameEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(username)));

        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.passwordEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(password)));

        onView(withId(R.id.loginButton))
                .perform(click());

        /*add city*/
        String testLocation = "Champaign";

        onView(withId(R.id.buttonAddLocation)).perform(click());

        onView(withId(R.id.locationEditText))
                .perform(ViewActions.typeText(testLocation), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.locationEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(testLocation)));

        onView(withId(R.id.AddLocationB))
                .perform(click());

        /*check that the city is in list*/
        String expectedCity = "CHAMPAIGN"; // Replace with the expected button text
        Matcher<View> buttonMatcher = allOf(withText(expectedCity), isDescendantOfA(withId(R.id.buttonContainer)));

        onView(buttonMatcher).check(matches(isDisplayed()));


        /*remove city*/
        onView(withId(R.id.buttonRemoveLocation)).perform(click());

        onView(withId(R.id.locationEditText))
                .perform(ViewActions.typeText(testLocation), ViewActions.closeSoftKeyboard());

        onView(withId(R.id.locationEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(testLocation)));

        onView(withId(R.id.RemoveLocationB))
                .perform(click());
    }

}
