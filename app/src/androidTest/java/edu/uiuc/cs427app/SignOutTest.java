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
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.uiuc.cs427app.db.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
public class SignOutTest {

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
        String username = "test";
        String password = "testuser";

        //Sign out MainActivity//
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton))
                .perform(click());
        openContextualActionModeOverflowMenu();
        onView(withText("Sign Out")).perform(click());
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));

        //wait 2 seconds//
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Sign out RemoveLocActivity//
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton))
                .perform(click());
        onView(withId(R.id.buttonRemoveLocation))
                .perform(click());
        openContextualActionModeOverflowMenu();
        onView(withText("Sign Out")).perform(click());
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));

        //wait 2 seconds//
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Sign out AddActivity//
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton))
                .perform(click());
        onView(withId(R.id.buttonAddLocation))
                .perform(click());
        openContextualActionModeOverflowMenu();
        onView(withText("Sign Out")).perform(click());
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));

        //wait 2 seconds
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Sign out DetailsActivity//
        onView(withId(R.id.usernameEditText))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.passwordEditText))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.loginButton))
                .perform(click());
        String testLocation ="champaign";
        onView(withId(R.id.buttonAddLocation))
                .perform(click());
        onView(withId(R.id.locationEditText))
                .perform(ViewActions.typeText(testLocation), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.locationEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(testLocation)));
        onView(withId(R.id.AddLocationB))
                .perform(click());
        onView(allOf(withText(containsString("champaign")), isAssignableFrom(Button.class))).perform(click());
        openContextualActionModeOverflowMenu();
        onView(withText("Sign Out")).perform(click());
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
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
