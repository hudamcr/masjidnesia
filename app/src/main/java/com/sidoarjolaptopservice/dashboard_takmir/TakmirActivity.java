package com.sidoarjolaptopservice.dashboard_takmir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.sidoarjolaptopservice.masjid.R;

import java.util.ArrayList;
import java.util.List;

public class TakmirActivity extends AppCompatActivity {
    TabLayout tab;
    ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.belum,
            R.drawable.pending,
            R.drawable.terima
    };
    private int[] tabIcons2 = {
            R.drawable.belum,
            R.drawable.pending,
            R.drawable.terima
    };
    Boolean session = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takmir);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        tab = (TabLayout) findViewById(R.id.tab);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);
        tab.setupWithViewPager(viewPager);
        setupTabIcons();
        tab.setTabTextColors(
                getResources().getColor(R.color.drkgray ),
                getResources().getColor(R.color.white)
        );
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onTabSelected(TabLayout.Tab tabs) {
                if(tabs.getPosition()==0){
                    tab.getTabAt(0).setIcon(tabIcons[0]);
                    tab.getTabAt(1).setIcon(tabIcons2[1]);
                    tab.getTabAt(2).setIcon(tabIcons2[2]);
                    tab.getTabAt(0).getIcon().setAlpha(255);
                    tab.getTabAt(1).getIcon().setAlpha(100);
                    tab.getTabAt(2).getIcon().setAlpha(100);
                    //tab.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#666565")));

                }
                if(tabs.getPosition()==1){
                    tab.getTabAt(0).setIcon(tabIcons2[0]);
                    tab.getTabAt(1).setIcon(tabIcons[1]);
                    tab.getTabAt(2).setIcon(tabIcons2[2]);
                    tab.getTabAt(0).getIcon().setAlpha(100);
                    tab.getTabAt(1).getIcon().setAlpha(255);
                    tab.getTabAt(2).getIcon().setAlpha(100);
                    //tab.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#666565")));
                }
                if(tabs.getPosition()==2){
                    tab.getTabAt(0).setIcon(tabIcons2[0]);
                    tab.getTabAt(1).setIcon(tabIcons2[1]);
                    tab.getTabAt(2).setIcon(tabIcons[2]);
                    tab.getTabAt(0).getIcon().setAlpha(100);
                    tab.getTabAt(1).getIcon().setAlpha(100);
                    tab.getTabAt(2).getIcon().setAlpha(255);

                    //tab.setTabTextColors(ColorStateList.valueOf(Color.parseColor("#666565")));
/*                    tab.setTabTextColors(getResources().getColor(R.color.white),
                            getResources().getColor(R.color.white));
  */              }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabs) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void setupTabIcons() {
        tab.getTabAt(0).setIcon(tabIcons[0]);
        tab.getTabAt(1).setIcon(tabIcons2[1]);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BelumFragment(), "BELUM BAYAR");
        adapter.addFrag(new PendingFragment(), "PENDING");
        adapter.addFrag(new SudahFragment(), "SUDAH BAYAR");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
