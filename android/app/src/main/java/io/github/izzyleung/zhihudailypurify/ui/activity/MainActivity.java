package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.util.Calendar;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.support.Constants;
import io.github.izzyleung.zhihudailypurify.ui.fragment.FeedFragment;

public class MainActivity extends BaseActivity {
    private static final int PAGE_COUNT = 7;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutResID = R.layout.activity_main;

        super.onCreate(savedInstanceState);

        TabLayout tabs = findViewById(R.id.main_pager_tabs);
        ViewPager viewPager = findViewById(R.id.main_pager);
        assert tabs != null;
        assert viewPager != null;
        viewPager.setOffscreenPageLimit(PAGE_COUNT);

        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab_pick_date);
        assert floatingActionButton != null;
        floatingActionButton.setOnClickListener(v -> prepareIntent(PickDateActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_settings) {
            return prepareIntent(PrefsActivity.class);
        } else if (itemId == R.id.action_go_to_search) {
            return prepareIntent(SearchActivity.class);
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean prepareIntent(Class clazz) {
        startActivity(new Intent(MainActivity.this, clazz));
        return true;
    }

    private class MainPagerAdapter extends FragmentStatePagerAdapter {
        MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            Fragment newFragment = new FeedFragment();

            Calendar dateToGetUrl = Calendar.getInstance();
            dateToGetUrl.add(Calendar.DAY_OF_YEAR, 1 - i);
            String date = Constants.Dates.simpleDateFormat.format(dateToGetUrl.getTime());

            bundle.putString(Constants.BundleKeys.DATE, date);
            bundle.putBoolean(Constants.BundleKeys.IS_FIRST_PAGE, i == 0);
            bundle.putBoolean(Constants.BundleKeys.IS_SINGLE, false);

            newFragment.setArguments(bundle);
            return newFragment;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Calendar displayDate = Calendar.getInstance();
            displayDate.add(Calendar.DAY_OF_YEAR, -position);

            return (position == 0 ? getString(R.string.zhihu_daily_today) + " " : "")
                    + DateFormat.getDateInstance().format(displayDate.getTime());
        }
    }
}