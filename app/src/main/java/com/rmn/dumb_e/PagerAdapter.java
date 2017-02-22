package com.rmn.dumb_e;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

/**
 * Created by rmn on 31-08-2016.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    int mPos = 0, i = 0;
    int mCount;
    HomeFragment.OnFragmentInteractionListener mListener;

    public PagerAdapter(FragmentManager fm, int count, int i) {
        super(fm);
        mCount = count;
        this.i = i;
    }

    @Override
    public Fragment getItem(int position) {

        if (i == 1 && position == 0) {
            return HomeFragment.newInstance("rmn");
        }

        if (position < 4) {

            return HolderFragment.newInstance(position + 1);

        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return mCount;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:

                return "SECTION 1";
            case 1:

                return "SECTION 2";
            case 2:

                return "SECTION 3";
            case 3:

                return "";
        }
        return null;
    }


}
