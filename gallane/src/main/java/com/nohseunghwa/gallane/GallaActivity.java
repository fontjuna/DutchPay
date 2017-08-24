package com.nohseunghwa.gallane;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nohseunghwa.gallane.fragments.CalcFragment;
import com.nohseunghwa.gallane.fragments.KidsFragment;
import com.nohseunghwa.gallane.fragments.PapaFragment;
import com.nohseunghwa.gallane.fragments.SendFragment;

import static com.nohseunghwa.gallane.backing.Constants.INPUT_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_0;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_1;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_2;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_3;

public class GallaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galla);

        clearSharedPreference();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    KidsFragment fragment = (KidsFragment) adapter.getItem(position);
                    fragment.restoreResult();
                } else if (position == 1) {
                    PapaFragment fragment = (PapaFragment) adapter.getItem(position);
                    fragment.restoreResult();
                } else if (position == 2) {
                    SendFragment fragment = (SendFragment) adapter.getItem(position);
                    fragment.restoreResult();
                } else if (position == 3) {
                    CalcFragment fragment = (CalcFragment) adapter.getItem(position);
                    fragment.restoreResult();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(adapter);
    }

    private void clearSharedPreference() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = message.edit();
        editor.putString(INPUT_EXPRESSION, "");
        editor.apply();
    }

    private static class MyPagerAdapter extends FragmentStatePagerAdapter {

        public static final int PAGE_NUM = 4;
        private KidsFragment mKidsFragment;
        private PapaFragment mPapaFragment;
        private SendFragment mSendFragment;
        private CalcFragment mCalcFragment;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mKidsFragment = new KidsFragment();
            mPapaFragment = new PapaFragment();
            mSendFragment = new SendFragment();
            mCalcFragment = new CalcFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mKidsFragment;
                case 1:
                    return mPapaFragment;
                case 2:
                    return mSendFragment;
                case 3:
                    return mCalcFragment;
            }
            return mKidsFragment;
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return TAB_TITLE_0;
                }
                case 1: {
                    return TAB_TITLE_1;
                }
                case 2: {
                    return TAB_TITLE_2;
                }
                case 3: {
                    return TAB_TITLE_3;
                }
            }
            return super.getPageTitle(position);
        }
    }

}
