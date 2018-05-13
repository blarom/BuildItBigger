package com.udacity.gradle.builditbigger;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public ActivityTestRule<TestMainActivity> mActivityRule = new ActivityTestRule<>(TestMainActivity.class);

    @Test
    public void testButtonClick() throws InterruptedException {
        onView(withId(R.id.joke_button)).perform(click());

        //https://medium.com/@v.danylo/simple-way-to-test-asynchronous-actions-in-android-service-asynctask-thread-rxjava-etc-d43b0402e005
        final Object syncObject = new Object();

        //Setting the event to listen to
        TestMainActivity testMainActivity = mActivityRule.getActivity();
        testMainActivity.setCallback(new TestMainActivity.TestCallback() {

            @Override
            public void onHandleGceResponseCalled(String data) {

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


    @Before
    public void setUpCounter() {
        //mCounter = new ClickCounter();
    }
    @Test
    public void testInitialCount() {
        //Assert.assertEquals(mCounter.getCount(), 0);
    }

}
