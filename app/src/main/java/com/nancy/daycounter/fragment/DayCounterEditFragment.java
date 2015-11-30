package com.nancy.daycounter.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.nancy.daycounter.model.DayCounter;

/**
 * Created by nan.zhang on 11/27/15.
 */
public class DayCounterEditFragment extends Fragment {

    private Callbacks mCallbacks;

    public interface Callbacks {
        public void onDayCounterUpdated(DayCounter dayCounter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;   // set callbacks to the activity attached to
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
