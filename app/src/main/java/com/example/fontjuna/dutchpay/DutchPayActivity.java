package com.example.fontjuna.dutchpay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.fontjuna.dutchpay.fragments.KidsFragment;
import com.example.fontjuna.dutchpay.fragments.PapasFragment;

public class DutchPayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    private static class MyPagerAdapter extends FragmentPagerAdapter {

        public static final int PAGE_NUM = 3;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new KidsFragment();
                case 1:
                    return new PapasFragment();
            }
            return new KidsFragment();
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }
    }

}
