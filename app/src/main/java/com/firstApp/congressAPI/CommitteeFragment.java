package com.firstApp.congressAPI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommitteeFragment extends Fragment {

    public static TabLayout committeeTabLayout;
    public static ViewPager viewPager;
    public static int int_items = 3 ;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.committee_layout,null);
        committeeTabLayout = (TabLayout) x.findViewById(R.id.committeesTab);
        viewPager = (ViewPager) x.findViewById(R.id.committeeviewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        committeeTabLayout.post(new Runnable() {
            @Override
            public void run() {
                committeeTabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;

    }

    class MyAdapter extends FragmentPagerAdapter{

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new HouseCommitteeFragment();
                case 1 : return new SenateCommitteeFragment();
                case 2 : return new JointCommitteeFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "HOUSE";
                case 1 :
                    return "SENATE";
                case 2:
                    return "JOINT";
            }
            return null;
        }
    }

}
