package com.example.fontjuna.dutchpay;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.fontjuna.dutchpay.fragments.KidsFragment;
import com.example.fontjuna.dutchpay.fragments.PapaFragment;
import com.example.fontjuna.dutchpay.fragments.SendFragment;

public class DutchPayActivity extends AppCompatActivity {

    private static final String MESSAGE_STR = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dutch_pay);

        clearSharedPreference();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }

    private void clearSharedPreference() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = message.edit();
        editor.putString(MESSAGE_STR, "");
        editor.apply();
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
                    return new PapaFragment();
                case 2:
//                    return new ItemFragment();
//                case 3:
                    return new SendFragment();
            }
            return new KidsFragment();
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return "어리신 용";
                }
                case 1: {
                    return "어르신 용";
                }
                case 2: {
//                    return "결과 상세";
//                }
//                case 3: {
                    return "결과 공유";
                }
            }
            return super.getPageTitle(position);
        }
    }

}
