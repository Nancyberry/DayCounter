package com.nancy.daycounter.activity;

import android.support.v4.app.FragmentActivity;

import com.nancy.daycounter.fragment.DayCounterEditFragment;
import com.nancy.daycounter.model.DayCounter;

/**
 * Created by nan.zhang on 11/30/15.
 */
public class DayCounterEditActivity extends FragmentActivity implements DayCounterEditFragment.Callbacks {
    
    @Override
    public void onDayCounterUpdated(DayCounter dayCounter) {

    }
}
