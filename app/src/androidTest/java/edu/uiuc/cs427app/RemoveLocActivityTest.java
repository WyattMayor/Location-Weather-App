package edu.uiuc.cs427app;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import edu.uiuc.cs427app.db.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
public class RemoveLocActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);
    @Test
    public void RemoveCityTest() {
        //set up database//
        String testLocation = "CHAMPAIGN";
        String username = "wpmayor2";
        String password = "123";
        String DarkMode = "0";

        /*DatabaseHelper mockedDatabase = Mockito.mock(DatabaseHelper.class);

        mockedDatabase.insertUser(mockSQLiteDatabase, username, password, DarkMode);

        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.setDatabaseHelper(mockSQLiteDatabase);
        });*/

        /*//signup//
        onView(withId(R.id.signupLink))
                .perform(click());
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.signupButton))
                .perform(click());

        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        //login//
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton))
                .perform(click());

        //wait 2 seconds//
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //AddCiy//
        //Clicks add city button
        onView(withId(R.id.buttonAddLocation))
                .perform(click());
        //checks to make sure remove city activity is displayed
        onView(withId(R.id.AddLocationB))
                .check(matches(isDisplayed()));
        //adds champaign to text box on remove city
        onView(withId(R.id.locationEditText))
                .perform(ViewActions.typeText(testLocation), ViewActions.closeSoftKeyboard());
        //checks to see if the text box contains champaign
        onView(withId(R.id.locationEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(testLocation)));
        //clicks remove city button on RemoveLocActivity
        onView(withId(R.id.AddLocationB))
                .perform(click());
        //Checks to make sure mainactivity is displayed again
        onView(withId(R.id.buttonAddLocation))
                .check(matches(isDisplayed()));

        //wait 2 seconds//
        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //RemoveCity//
        //Clicks remove city button
        onView(withId(R.id.buttonRemoveLocation))
                .perform(click());
        //checks to make sure remove city activity is displayed
        onView(withId(R.id.RemoveLocationB))
                .check(matches(isDisplayed()));
        //adds champaign to text box on remove city
        onView(withId(R.id.locationEditText))
                .perform(ViewActions.typeText(testLocation), ViewActions.closeSoftKeyboard());
        //checks to see if the text box contains champaign
        onView(withId(R.id.locationEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(testLocation)));
        //clicks remove city button on RemoveLocActivity
        onView(withId(R.id.RemoveLocationB))
                .perform(click());
        //Checks to make sure mainactivity is displayed again
        onView(withId(R.id.buttonRemoveLocation))
                .check(matches(isDisplayed()));
    }
}
