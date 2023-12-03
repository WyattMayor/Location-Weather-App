package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MapTest {

    @Rule
    public ActivityScenarioRule<DetailsActivity> activityScenarioRule =
            new ActivityScenarioRule<>(DetailsActivity.class);
    private String cityName;

    @Parameterized.Parameters(name = "Test with city: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {"New York"}, {"Chicago"}, {"Los Angeles"}, {"San Francisco"}, {"Champaign"}
        });
    }

    public MapTest(String cityName) {
        this.cityName = cityName;
    }

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void checkMap() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.getIntent().putExtra("city", cityName);
        });

        onView(withId(R.id.mapButton)).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));

        // Wait 5 seconds max to wait for weather API response.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //make sure map is displayed for all the cities
        onView(withContentDescription("Google Map")).perform(click());


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }
}
