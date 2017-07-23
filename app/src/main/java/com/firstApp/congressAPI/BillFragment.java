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

public class BillFragment extends Fragment {

    public static TabLayout billTabLayout;
    public static ViewPager viewPager;
    public static int int_items = 2 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.bill_layout,null);
        billTabLayout = (TabLayout) x.findViewById(R.id.billsTab);
        viewPager = (ViewPager) x.findViewById(R.id.billviewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        billTabLayout.post(new Runnable() {
            @Override
            public void run() {
                billTabLayout.setupWithViewPager(viewPager);
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
                case 0 : return new ActiveBillFragment();
                case 1 : return new NewBillFragment();
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
                    return "ACTIVE BILLS";
                case 1 :
                    return "NEW BILLS";
            }
            return null;
        }
    }

}
