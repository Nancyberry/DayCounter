package com.nancy.daycounter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.nancy.daycounter.R;
import com.nancy.daycounter.fragment.DayCounterDetailFragment;
import com.nancy.daycounter.model.DayCounter;
import com.nancy.daycounter.model.DayCounterLab;

import java.util.List;
import java.util.UUID;

/**
 * Created by nan.zhang on 11/26/15.
 */
public class DayCounterPagerActivity extends FragmentActivity implements DayCounterDetailFragment.Callbacks {
    ViewPager mViewPager;
    private static final String TAG = DayCounterPagerActivity.class.getSimpleName();
    public static final int EDIT_DETAIL = 1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setViewPager();
    }

    @Override
    public void onDayCounterEditSelected(DayCounter dayCounter) {
        Intent intent = new Intent(this, DayCounterEditActivity.class);
        intent.putExtra(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID, dayCounter.getId());
        startActivityForResult(intent, EDIT_DETAIL);

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_DETAIL) {
                setViewPager();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Set view pager and current item to render.
     */
    private void setViewPager() {
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.view_pager);
        setContentView(mViewPager);

        final List<DayCounter> dayCounters = DayCounterLab.get(this).getDayCounters();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                UUID id = dayCounters.get(position).getId();
                return DayCounterDetailFragment.newInstance(id);
            }

            @Override
            public int getCount() {
                return dayCounters.size();
            }
        });

        UUID dayCounterId = (UUID) getIntent().getSerializableExtra(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID);

        for (int i = 0; i < dayCounters.size(); ++i) {
            if (dayCounters.get(i).getId().equals(dayCounterId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
