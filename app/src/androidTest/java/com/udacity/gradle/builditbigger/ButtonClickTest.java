package com.udacity.gradle.builditbigger;

import android.app.Activity;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;

import com.example.android.jokesandroidlibrary.JokesActivity;
import com.example.android.jokesjavalibrary.JavaJokes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ButtonClickTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testButtonClick() throws InterruptedException {
        onView(withId(R.id.joke_button)).perform(click());
        //onView(withId(R.id.joke_textview)).check(matches(withText("Hi, I have no sense of humor.")));
        JavaJokes javaJokes = new JavaJokes();

        onView(withId(R.id.joke_textview)).check(matches(withText(javaJokes.getJoke())));
        //checkResultOfAsyncTask();
    }


    @Before
    public void setUpCounter() {
        //mCounter = new ClickCounter();
    }
    @Test
    public void testInitialCount() {
        //Assert.assertEquals(mCounter.getCount(), 0);
    }

    private Activity getActivityInstance(){
        //https://stackoverflow.com/questions/38737127/espresso-how-to-get-current-activity-to-test-fragments/38990078?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });
        return currentActivity[0];
    }

    private void checkResultOfAsyncTask() throws InterruptedException {

        //https://medium.com/@v.danylo/simple-way-to-test-asynchronous-actions-in-android-service-asynctask-thread-rxjava-etc-d43b0402e005
        final Object syncObject = new Object();

        //Setting the event to listen to
        JokesActivity mTestJokesActivity = (JokesActivity) getActivityInstance();
        //TestJokesActivity testJokesActivity = new TestJokesActivity();
        mTestJokesActivity.setCallback(new JokesActivity.TestCallback() {

            @Override
            public void onReturnJokeCalled(String data) {

                onView(withId(R.id.joke_textview)).check(matches(withText("Hi, I have no sense of humor.")));
                synchronized (syncObject){
                    syncObject.notify();
                }
            }
        } );

        //Waiting until the event happens
        synchronized (syncObject){
            syncObject.wait();
        }
    }
}
