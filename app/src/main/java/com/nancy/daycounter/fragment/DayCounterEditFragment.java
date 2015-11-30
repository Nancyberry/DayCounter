package com.nancy.daycounter.fragment;

import android.support.v4.app.Fragment;

import com.nancy.daycounter.model.DayCounter;

/**
 * Created by nan.zhang on 11/27/15.
 */
public class DayCounterEditFragment extends Fragment {

    public interface Callbacks {
        public void onDayCounterUpdated(DayCounter dayCounter);
    }
}
