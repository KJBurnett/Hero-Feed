package com.burntech.kyler.comicrss;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.burntech.kyler.comicrss.FirstFragment;
import com.burntech.kyler.comicrss.SmartFragmentStatePagerAdapter;

/**
 * Created by Kyler J. Burnett on 4/5/2015.
 */
public class MyPagerAdapter extends SmartFragmentStatePagerAdapter {
    private int NUM_ITEMS = 4;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    /*
            @Override
            public float getPageWidth (int position) {
                return 0.93f;
            }
    */
    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return FirstFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 1 - This will show FirstFragment different title
                return FirstFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 2 - This will show SecondFragment
                return FirstFragment.newInstance(2, "Page # 3");
            case 3: // Fragment # 3 - This will show FirstFragment different title
                return FirstFragment.newInstance(3, "Page # 4");
            case 4: // Fragment # 4 - Empty.
                return FirstFragment.newInstance(4, "Page # 5");
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}// end MyPagerAdapter
