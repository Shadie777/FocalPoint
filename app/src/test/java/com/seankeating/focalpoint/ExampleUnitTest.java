package com.seankeating.focalpoint;

import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;

import junit.framework.Assert;

@SuppressWarnings("rawtypes")
public class ExampleTest extends ActivityInstrumentationTestCase2 {
    private Solo solo;

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.seankeating.focalpointPresenter.LoginActivity";

    private static Class<?> launcherActivityClass;
    static{
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public ExampleTestTest() throws ClassNotFoundException {
        super(launcherActivityClass);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testRun() {
        //Wait for activity: 'com.example.ExampleActivty'
        solo.waitForActivity("LoginActivity", 2000);

        TutorialSection();

        incorrectDetails();

        correctDetails();

    }

    public void TutorialSection() {
        solo.clickOnButton("Help");
        solo.waitForActivity("ScreenSliderActivity", 2000);


        solo.drag(300, 0, 100, 100, 1);

    }

    public void incorrectDetails() {
        //click button
        solo.clickOnButton("Log in with Facebook");
        //interrupt connection to facebook
        solo.clickLongOnScreen(50, 50);

        Assert.assertTrue(solo.waitForText("Login attempt canceled"));


        solo.clickOnButton("Log in with Facebook");
        solo.sleep(1000);

        solo.clickOnText("Email Address or phone number");
        solo.typeText("fmfmfmf");

        solo.clickOnText("Password");
        solo.typeText("fmfmfmf");

        solo.clickOnText("Log In");
        solo.sleep(300);

        Assert.assertTrue(solo.searchText("doesn't match any account"));

        solo.clickLongOnScreen(31, 97);

        Assert.assertTrue(solo.waitForText("Login attempt canceled"));
    }



    public void correctDetails() {
        solo.clickOnButton("Log in with Facebook");
        solo.sleep(1000);

        solo.clickOnText("Email Address or phone number");
        solo.typeText("fdhkhbr_moiduescu_1459631980@tfbnw.net");

        solo.clickOnText("Password");
        solo.typeText("AppTest1");

        solo.clickOnText("Log In");
        solo.sleep(300);


        solo.waitForActivity("MapsActivity", 2000);
        solo.assertCurrentActivity("string", MapsActivity.class);
    }
}