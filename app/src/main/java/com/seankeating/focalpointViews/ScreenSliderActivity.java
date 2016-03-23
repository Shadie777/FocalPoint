package com.seankeating.focalpointViews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.seankeating.focalpoint.R;
/**
 * Created by Sean on 08/12/2015.
 */


public class ScreenSliderActivity extends FragmentActivity{


    /** number of pages to show for sliding


    /** paper widget which handles the animation */
    private ViewPager mviewPager;


    /** paper widget which supplies the pages to the view page */
    private PagerAdapter mpagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);

        mviewPager = (ViewPager) findViewById(R.id.pager);
        mpagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(mpagerAdapter);
        mviewPager.setCurrentItem(0);

    }

}



/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
 class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new FirstTutorialFragment();
            case 1:
                return new SecondTutorialFragment();
        }
    return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

