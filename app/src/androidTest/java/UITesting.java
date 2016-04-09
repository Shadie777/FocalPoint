/**
 * Created by Sean on 08/04/2016.
 */
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.widget.Button;
import android.widget.EditText;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class UITesting extends InstrumentationTestCase {

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

        UiObject tutorial4 = new UiObject(new UiSelector()
                .textContains("Select a marker")
                .className("android.widget.TextView"));

        tutorial4.waitForExists(5000);
        assertThat(tutorial4.getText(), CoreMatchers.containsString("Tapping on the info"));


        page.swipeLeft(10);

        UiObject tutorial5 = new UiObject(new UiSelector()
                .textContains("event details")
                .className("android.widget.TextView"));

        tutorial5.waitForExists(5000);
        assertThat(tutorial5.getText(), CoreMatchers.containsString("Here you can see more"));

        mDevice.pressBack();
    }

    @Test
    public void testLogin() throws UiObjectNotFoundException, InterruptedException {

        //press login button
        UiObject login = mDevice.findObject(new UiSelector()
                .text("Log in with Facebook")
                .className("android.widget.Button"));


        //check that new window has loaded
        assertTrue("activity loaded", login.clickAndWaitForNewWindow(30000));


        //set username
        UiObject user = mDevice.findObject(new UiSelector()
                .instance(0)
                .className(EditText.class));

        user.setText("seanjk@hotmail.co.uk");

        Thread.sleep(1000);

        //set password
        UiObject input = mDevice.findObject(new UiSelector()
                .instance(1)
                .className(EditText.class));
        input.setText("text");
        input.setText("Coding160695");

        Thread.sleep(1000);

        //click login
        UiObject button = mDevice.findObject(new UiSelector()
                .instance(0)
                .className(Button.class));

        button.click();

        //wait
        Thread.sleep(5000);

        //accept permissions
        UiObject button3 = mDevice.findObject(new UiSelector()
                .instance(1)
                .className(Button.class));
        button3.waitForExists(5000);
        button3.click();

        //wait
        Thread.sleep(5000);


        testMap();

        //click actionbar
        mDevice.click(717, 99);

        UiObject logout = mDevice.findObject(new UiSelector()
                .text("Logout")
                .className("android.widget.TextView"));

        //check that user logged out
        assertTrue("activity loaded", logout.clickAndWaitForNewWindow(5000));
    }


    public void testMap() throws UiObjectNotFoundException, InterruptedException {

        UiObject map = mDevice.findObject(new UiSelector()
                .description("Google Map")
                .className("android.view.View"));


        //check that map exists
        assertTrue("map exists", map.exists());

        UiObject marker = map.getChild(new UiSelector().index(0));

        if (marker.exists()) {
          //check marker is on map
            assertTrue("marker exists", marker.exists());

            //get marker description
            String markerdesc = marker.getContentDescription();

            //click
            marker.click();

            Thread.sleep(1000);

            //get coordinates and click on info window
            Rect bounds = marker.getBounds();
            int x = bounds.centerX();
            int y = bounds.centerY();
            mDevice.click(x, y + 10);
            Thread.sleep(3000);

            //test event details on new page
            testEventDetailsWindow();

            Thread.sleep(5000);

            //check if map is still there
            assertTrue("map exists", map.exists());

            //check if marker is still there
            assertThat(marker.getContentDescription(), CoreMatchers.containsString(markerdesc));


            //click refresh
            mDevice.click(480,95);

            //get marker that has reappeared
            marker = map.getChild(new UiSelector().index(0));

            //check if marker is the same as the orginal
            assertThat(marker.getContentDescription(), CoreMatchers.containsString(markerdesc));

        } else {
            assertFalse("marker doesnt exist", marker.exists());
        }

        //filter
        testFilter();

        //custommarker
        testCustomMarker();
    }

    public void testEventDetailsWindow() throws InterruptedException {
        UiObject title = mDevice.findObject(new UiSelector()
                .index(0)
                .className("android.widget.TextView"));


        //check that event details have loaded
        assertThat(title, CoreMatchers.notNullValue());

        mDevice.pressBack();

        Thread.sleep(1000);
    }

    public void testCustomMarker() throws UiObjectNotFoundException, InterruptedException {
        UiObject map = mDevice.findObject(new UiSelector()
                .description("Google Map")
                .className("android.view.View"));

        //swipe down on map to new location and click to create marker
        map.swipeDown(4);

        Thread.sleep(3000);
        map.click();

        Thread.sleep(5000);

        UiObject customlocation = map.getChild(new UiSelector().descriptionContains("You").index(map.getChildCount()-1));
        UiObject marker = map.getChild(new UiSelector().index(map.getChildCount()-2));


        //check if custom marker is placed
        assertTrue("custom marker exists", customlocation.exists());

        if(marker.exists()){
            //check that there is an event marker
            assertTrue("marker exists", marker.exists());

            //get marker description
            String markerdesc = marker.getContentDescription();

            Thread.sleep(3000);

            //click custom marker
            customlocation.click();

            //click its info window
            Rect bounds = marker.getBounds();
            int x = bounds.centerX();
            int y = bounds.centerY();
            mDevice.click(x, y + 10);
            Thread.sleep(3000);


            //click
            marker.click();

            Thread.sleep(1000);


            //get coordinates and click on info window
             bounds= marker.getBounds();
             x = bounds.centerX();
             y = bounds.centerY();
            mDevice.click(x, y + 10);
            Thread.sleep(3000);



            //test event details on new page
            testEventDetailsWindow();


            //get marker that has reappeared
            marker = map.getChild(new UiSelector().index(1));

            //check if map is still there
            assertTrue("map exists", map.exists());

            //check if marker is the same as the orginal
            assertThat(marker.getContentDescription(), CoreMatchers.containsString(markerdesc));

            //click refresh
            mDevice.click(480,95);

            //get marker that has reappeared
            marker = map.getChild(new UiSelector().index(map.getChildCount()-2));

            //check if marker is the same as the orginal
            assertThat(marker.getContentDescription(), CoreMatchers.containsString(markerdesc));

        }else{
            //check that there is no event marker
            assertFalse("marker doesnt exist", marker.exists());
        }
    }

    public void testFilter() throws UiObjectNotFoundException, InterruptedException {

        //click actionbar
        mDevice.click(717, 99);

        UiObject filter = mDevice.findObject(new UiSelector()
                .text("Filter")
                .className("android.widget.TextView"));
        filter.click();


        //change the day to filter
       mDevice.click(200,668);

        UiObject ok = mDevice.findObject(new UiSelector()
                .text("OK")
                .className("android.widget.Button"));

        ok.click();

        //system goes back to the map
        Thread.sleep(3000);

        UiObject map = mDevice.findObject(new UiSelector()
                .description("Google Map")
                .className("android.view.View"));


        //check that map exists
        assertTrue("map exists", map.exists());

        UiObject marker = map.getChild(new UiSelector().index(0));


        if (marker.exists()) {
            //check marker is on map
            assertTrue("marker exists", marker.exists());

            //get marker description
            String markerdesc = marker.getContentDescription();

            //click
            marker.click();

            Thread.sleep(1000);

            //get coordinates and click on info window
            Rect bounds = marker.getBounds();
            int x = bounds.centerX();
            int y = bounds.centerY();
            mDevice.click(x, y + 10);
            Thread.sleep(3000);

            //test event details on new page
            testEventDetailsWindow();

            Thread.sleep(5000);

            //check if map is still there
            assertTrue("map exists", map.exists());

            //check if marker is still there
            assertThat(marker.getContentDescription(), CoreMatchers.containsString(markerdesc));


            //click refresh
            mDevice.click(480,95);

            //get marker that has reappeared
            marker = map.getChild(new UiSelector().index(0));

            //check if marker is the same as the orginal
            assertThat(marker.getContentDescription(), CoreMatchers.containsString(markerdesc));

        } else {
            assertFalse("marker doesnt exist", marker.exists());
        }
    }
}