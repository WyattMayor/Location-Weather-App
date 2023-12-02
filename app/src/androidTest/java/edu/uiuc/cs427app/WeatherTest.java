package edu.uiuc.cs427app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
public class WeatherTest {

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

    public WeatherTest(String cityName) {
        this.cityName = cityName;
    }

    @Before
    public void setUp() throws Exception {
        Intents.init();
    }

    @Test
    public void checkWeather() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.getIntent().putExtra("city", cityName);
        });

        onView(withId(R.id.weatherButton)).perform(click());
        intended(hasComponent(WeatherActivity.class.getName()));

        // Wait 5 seconds max to wait for weather API response.
        waitForCondition(withId(R.id.temperatureText), not(withText("Temperature")), 5000);

        onView(withId(R.id.cityNameText)).check(matches(withText(cityName)));
        onView(withId(R.id.temperatureText)).check(matches(withTextPattern("\\d+\\.\\d+°F")));
        onView(withId(R.id.humidityText)).check(matches(withTextPattern("Humidity: \\d+\\.\\d+%")));
        onView(withId(R.id.windConditionText)).check(matches(withTextPattern("Wind Speed: \\d+\\.\\d+ MPH, Degree: \\d+\\.\\d+°")));
    }

    @After
    public void tearDown() throws Exception {
        Intents.release();
    }

    private void waitForCondition(Matcher<View> viewMatcher, Matcher<View> condition, long timeout) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout) {
            try {
                // Try to match the condition.
                onView(viewMatcher).check(matches(condition));
                return;
            } catch (NoMatchingViewException | AssertionError e) {
                // If the condition not met, wait 100ms before retrying
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Interrupted during sleep", ie);
                }
            }
        }
        // The condition is not met within the timeout.
        throw new AssertionError("Condition not met within " + timeout + " milliseconds.");
    }


    private static Matcher<View> withTextPattern(final String regex) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            protected boolean matchesSafely(TextView textView) {
                return textView.getText().toString().matches(regex);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with text matching pattern: " + regex);
            }
        };
    }
}
