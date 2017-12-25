package io.github.izzyleung.zhihudailypurify.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.github.izzyleung.zhihudailypurify.R;
import io.github.izzyleung.zhihudailypurify.support.Constants;
import io.github.izzyleung.zhihudailypurify.ui.fragment.FeedFragment;

public class SingleDayNewsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        Fragment newFragment = new FeedFragment();

        assert bundle != null;
        String dateString = bundle.getString(Constants.BundleKeys.DATE);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = Constants.Dates.simpleDateFormat.parse(dateString);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } catch (ParseException ignored) {

        }

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(DateFormat.getDateInstance().format(calendar.getTime()));

        bundle.putString(Constants.BundleKeys.DATE, dateString);
        bundle.putBoolean(Constants.BundleKeys.IS_FIRST_PAGE,
                isSameDay(calendar, Calendar.getInstance()));
        bundle.putBoolean(Constants.BundleKeys.IS_SINGLE, true);

        newFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_frame, newFragment)
                .commit();
    }

    private boolean isSameDay(Calendar first, Calendar second) {
        return first.get(Calendar.YEAR) == second.get(Calendar.YEAR)
                && first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR);
    }
}
