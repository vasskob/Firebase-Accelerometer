package com.task.vasskob.firebase.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.task.vasskob.firebase.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class SignInActivityTest {

    private String mEmailToBeTyped;
    private String mPasswordToBeTyped;

    @Rule
    public ActivityTestRule<SignInActivity> signInActivityActivityTestRule =
            new ActivityTestRule<>(SignInActivity.class, true, true);

    public SignInActivityTest() {
        super();
    }

    @Before
    public void initValidString() {
        mEmailToBeTyped = "vasskob@gmail.com";
        mPasswordToBeTyped = "123456";
    }

    @Test
    public void onEmailSignInClick() throws Exception {
        // http://stackoverflow.com/questions/9405561/test-if-a-button-starts-a-new-activity-in-android-junit-pref-without-robotium

        onView(withId(R.id.field_email)).perform(typeText(mEmailToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.field_password)).perform(typeText(mPasswordToBeTyped), closeSoftKeyboard());
        onView(withId(R.id.button_sign_in)).perform(click());

        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(UpLoadFileActivity.class.getName(), null, false);
        Activity uploadActivity = getInstrumentation().waitForMonitorWithTimeout(am, 5000);
        assertNotNull(uploadActivity);
        uploadActivity.finish();
    }

    @Test
    public void onEmailSignUpClick() throws Exception {

    }

    @Test
    public void isValidEmail() throws Exception {

    }
}