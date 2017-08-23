package com.nohseunghwa.gallane;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nohseunghwa.gallane.backing.CommonDutchPay;
import com.nohseunghwa.gallane.fragments.KidsFragment;
import com.nohseunghwa.gallane.fragments.PapaFragment;
import com.nohseunghwa.gallane.fragments.SendFragment;

public class GallaActivity extends AppCompatActivity implements CommonDutchPay {

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
        editor.putString(INPUT_EXPRESSION, "");
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
                    return TAB_TITLE_1;
                }
                case 1: {
                    return TAB_TITLE_2;
                }
                case 2: {
                    return TAB_TITLE_3;
                }
            }
            return super.getPageTitle(position);
        }
    }

//    private void testDialog() {
//        Confirm confirm = new Confirm("테스ㅡ트", "알아보세요!", "ok", "no");
//
//    }

}
