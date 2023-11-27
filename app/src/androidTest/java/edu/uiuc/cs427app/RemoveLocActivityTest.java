package edu.uiuc.cs427app;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import edu.uiuc.cs427app.db.DatabaseHelper;

@RunWith(AndroidJUnit4.class)
public class RemoveLocActivityTest {
    @Before
    public void launchActivity() {
        ActivityScenario.launch(RemoveLocActivity.class);
    }
    @Test
    public void testCityRemoval() {

        DatabaseHelper mockDatabase = Mockito.mock(DatabaseHelper.class);


        String testLocation = "Champaign";

        //TODO create mock database

        //TODO Add user

        //TODO Add Champaign

        //TODO Assert Champaign is in the database

        // adds city name to edit text box
        Espresso.onView(ViewMatchers.withId(R.id.locationEditText))
                .perform(ViewActions.typeText(testLocation), ViewActions.closeSoftKeyboard());

        // asserts that edit text box has champaign in it
        Espresso.onView(ViewMatchers.withId(R.id.locationEditText))
                .check(ViewAssertions.matches(ViewMatchers.withText(testLocation)));

        //clicks remove location button
        Espresso.onView(ViewMatchers.withId(R.id.RemoveLocationB))
                .perform(ViewActions.click());

        // asserts that main activity is started after the remove button is clicked
        Intents.intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
    }
    @Test
    public void CheckCityRemoval() {
        //TODO Assert Champaign is not in the database
    }

}
