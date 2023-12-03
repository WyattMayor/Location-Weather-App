package edu.uiuc.cs427app;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.uiuc.cs427app.db.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
public class RemoveAndAddLocActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        Intents.init();

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Initialize the DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(appContext);

        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insert test user into the database
        dbHelper.insertUser(db, "test", "testuser", "0");

        db.close();
    }

    @Test
    public void RemoveAndAddCityTest() {
        //set up database//
        String testLocation = "CHAMPAIGN";
        String username = "test";
        String password = "testuser";

        //login//
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton))
                .perform(click());
        intended(hasComponent(MainActivity.class.getName()));

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
        //checks to make sure add city activity is displayed
        intended(hasComponent(AddLocActivity.class.getName()));
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
        intended(hasComponent(MainActivity.class.getName()));

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
        intended(hasComponent(RemoveLocActivity.class.getName()));
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
        intended(hasComponent(MainActivity.class.getName()));

        //wait 2 seconds//
        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //RemoveLocActivity sign out menu//
        //check that option menu works on removelocactivity
        onView(withId(R.id.buttonRemoveLocation))
                .perform(click());
        //checks to make sure remove city activity is displayed
        onView(withId(R.id.RemoveLocationB))
                .check(matches(isDisplayed()));
        // Open the options menu
        openContextualActionModeOverflowMenu();
        // Click on the Sign Out menu item by text
        onView(withText("Sign Out")).perform(click());
        // Verify that LoginActivity is launched after sign-out
        intended(hasComponent(LoginActivity.class.getName()));
    }
    @After
    public void tearDown() throws Exception {
        Intents.release();

        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Initialize the DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(appContext);

        // Get writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete test user from the database
        dbHelper.deleteUser(db, "test");

        db.close();
    }
}
