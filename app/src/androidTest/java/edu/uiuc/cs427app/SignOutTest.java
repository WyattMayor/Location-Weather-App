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
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;

import edu.uiuc.cs427app.db.DatabaseHelper;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SignOutTest {

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

        db.close();
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

        db.close();
    }
}
