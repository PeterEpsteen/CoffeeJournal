package com.example.peter.coffeejournal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peter on 11/4/17.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm){
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }



    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
