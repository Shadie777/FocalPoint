/**
 * Created by Sean on 08/04/2016.
 */
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;

import com.seankeating.focalpoint.R;

import static org.junit.Assert.assertThat;


import junit.framework.Assert;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExampleTest extends InstrumentationTestCase {

    private static final String PACKAGE
            = "com.seankeating.focalpoint";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "UiAutomator";
    private UiDevice mDevice;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        Instrumentation instr = InstrumentationRegistry.getInstrumentation();

        mDevice = UiDevice.getInstance(instr);

        mDevice.pressHome();

//        // Wait for the Apps icon to show up on the screen
//        mDevice.wait(Until.hasObject(By.desc("Apps")), 3000);
//
//        UiObject2 appsButton = mDevice.findObject(By.desc("Apps"));
//        appsButton.click();
//
//
//        UiObject2 app = mDevice.findObject(By.text("FocalPoint"));
//        app.click();


        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE).depth(0)),
                LAUNCH_TIMEOUT);

    }



    @Test
    public void testTutorial() throws UiObjectNotFoundException {

        UiObject helpButton = mDevice.findObject(new UiSelector()
                .text("Help")
                .className("android.widget.Button"));

        helpButton.click();

        mDevice.wait(Until.hasObject(By.clazz("android.widget.ImageView")), 3000);

        UiObject page = new UiObject(new UiSelector().className("android.widget.RelativeLayout"));

        UiObject tutorial1 = new UiObject(new UiSelector().textContains("When")
        .className("android.widget.TextView"));


        assertThat(tutorial1.getText(), CoreMatchers.containsString("When the app opens"));

        tutorial1.waitForExists(5000);
        tutorial1.swipeLeft(10);

        //validate if second page is opened by taking the text
        UiObject tutorial2 = new UiObject(new UiSelector().textContains("place a marker")
                .className("android.widget.TextView"));

        //and assert it contains a string
        tutorial2.waitForExists(5000);
        assertThat(tutorial2.getText(), CoreMatchers.containsString("the app will then search"));

        tutorial2.swipeLeft(10);


        //validate if third page is opened
        UiObject tutorial3 = new UiObject(new UiSelector().textContains("filter by date")
                .className("android.widget.TextView")
                );

        tutorial3.waitForExists(5000);
        assertThat(tutorial3.getText(), CoreMatchers.containsString("You can filter by date"));


        //test swiping right
        page.swipeRight(10);
        tutorial2.waitForExists(5000);
        assertThat(tutorial2.getText(), CoreMatchers.containsString("the app will then search"));


        //go back to third tutorial page
        page.swipeLeft(10);
        tutorial3.waitForExists(5000);
        assertThat(tutorial3.getText(), CoreMatchers.containsString("You can filter by date"));

        page.swipeLeft(10);

        UiObject tutorial4  = new UiObject(new UiSelector()
                .textContains("Select a marker")
                .className("android.widget.TextView"));

        tutorial4.waitForExists(5000);
        assertThat(tutorial4.getText(), CoreMatchers.containsString("Tapping on the info"));


        page.swipeLeft(10);

        UiObject tutorial5  = new UiObject(new UiSelector()
                .textContains("event details")
                .className("android.widget.TextView"));

        tutorial5.waitForExists(5000);
        assertThat(tutorial5.getText(), CoreMatchers.containsString("Here you can see more"));

        mDevice.pressBack();
    }

    @Test
    public void StartApplication() throws UiObjectNotFoundException {

        UiObject login = mDevice.findObject(new UiSelector()
                .text("Log in with Facebook")
                .className("android.widget.Button"));

        login.clickAndWaitForNewWindow();


        UiObject user = mDevice.findObject(new UiSelector()
                .instance(0)
                .className(EditText.class));

        user.setText("seanjk@hotmail.co.uk");

        UiObject input = mDevice.findObject(new UiSelector()
                .instance(1)
                .className(EditText.class));
        input.setText("text");
        input.setText("Coding160695");

        onView(withText("Log in")).perform(click());

        mDevice.waitForIdle(100000000);
//        UiObject map = mDevice.findObject(new UiSelector()
//                .description("Google Map")
//                .className("android.view.View"));
//        map.waitForExists(100000000);
//        mDevice.waitForIdle(100000000);
//
//        UiObject marker = mDevice.findObject(new UiSelector().index(0));
//        if(marker != null){
//            marker.click();
//        }
//        marker.click();
    }


    public void ClickMap(){

        //click anywhere on map
        mDevice.click(200,300);
        mDevice.waitForIdle(5000);
    };

}