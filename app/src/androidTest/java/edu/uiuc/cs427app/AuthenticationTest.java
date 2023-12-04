package edu.uiuc.cs427app;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.intent.Intents;

import org.junit.Rule;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

import edu.uiuc.cs427app.db.DatabaseHelper;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AuthenticationTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);
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
        dbHelper.insertUser(db, "testuser", "testpassword", "0");

        // Query the database for the inserted user
        Cursor cursor = db.query("User", null, "username=?", new String[]{"testuser"}, null, null, null);

        // Assert that the user was inserted.
        assertNotNull("Cursor is null, user not found", cursor);

        // Close the cursor and database
        cursor.close();

        db.close();
    }

    @Test
    public void testSignUp() {
        // Simulate signup
        onView(withId(R.id.signupLink)).perform(click());
        intended(hasComponent(SignupActivity.class.getName()));
        onView(withId(R.id.usernameEditText)).perform(typeText("abc"));
        onView(withId(R.id.passwordEditText)).perform(typeText("123"));
        onView(withId(R.id.signupButton)).perform(click());
        // Verify go back to login activity
        intended(hasComponent(LoginActivity.class.getName()));

        // Simulate signup error
        onView(withId(R.id.signupLink)).perform(click());
        onView(withId(R.id.usernameEditText)).perform(typeText("abc"));
        onView(withId(R.id.passwordEditText)).perform(typeText("123"));
        onView(withId(R.id.signupButton)).perform(click());
        onView(withId(R.id.errorTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.loginLink)).perform(click());

        // Verify login
        onView(withId(R.id.usernameEditText)).perform(typeText("abc"));
        onView(withId(R.id.passwordEditText)).perform(typeText("123"));
        onView(withId(R.id.loginButton)).perform(click());
        intended(hasComponent(MainActivity.class.getName()));
    }

    @Test
    public void testInvalidLogin() {
        // Simulate login
        onView(withId(R.id.usernameEditText)).perform(typeText("invalid"));
        onView(withId(R.id.passwordEditText)).perform(typeText("invalid"));
        onView(withId(R.id.loginButton)).perform(click());
        // Verify errorText view displayed
        onView(withId(R.id.errorTextView)).check(matches(isDisplayed()));
    }
    @Test
    public void testSignOut() {
        // Simulate login
        onView(withId(R.id.usernameEditText)).perform(typeText("testuser"));
        onView(withId(R.id.passwordEditText)).perform(typeText("testpassword"));
        onView(withId(R.id.loginButton)).perform(click());

        // Verify MainActivity is loaded
        intended(hasComponent(MainActivity.class.getName()));

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
        dbHelper.deleteUser(db, "testuser");
        dbHelper.deleteUser(db, "abc");
        db.close();
    }
}
