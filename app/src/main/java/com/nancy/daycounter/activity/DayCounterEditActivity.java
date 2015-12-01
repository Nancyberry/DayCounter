package com.nancy.daycounter.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.nancy.daycounter.R;
import com.nancy.daycounter.fragment.DayCounterDetailFragment;
import com.nancy.daycounter.fragment.DayCounterEditFragment;
import com.nancy.daycounter.model.DayCounter;

import java.util.UUID;

/**
 * Created by nan.zhang on 11/30/15.
 */
public class DayCounterEditActivity extends SingleFragmentActivity implements DayCounterEditFragment.Callbacks {
    private static final String TAG = DayCounterEditActivity.class.getSimpleName();

    @Override
    protected Fragment createFragment() {
//        return new DayCounterEditFragment();
        UUID dayCounterId = (UUID) getIntent().getSerializableExtra(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID);
        return DayCounterEditFragment.newInstance(dayCounterId);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_edit_fragment;
    }

    @Override
    public void onDayCounterUpdated(DayCounter dayCounter) {
        // do nothing
        Intent intent = new Intent(this, DayCounterListActivity.class);
        startActivityForResult(intent, 0);
    }
}
