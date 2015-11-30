package com.nancy.daycounter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nancy.daycounter.R;
import com.nancy.daycounter.model.DayCounter;
import com.nancy.daycounter.model.DayCounterLab;
import com.nancy.daycounter.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by nan.zhang on 11/26/15.
 */
public class DayCounterListFragment extends ListFragment {
    private List<DayCounter> mDayCounters;
    private static final String TAG = DayCounterListFragment.class.getSimpleName();
    private Callbacks mCallbacks;

    public interface Callbacks {
        public void onDayCounterSelected(DayCounter dayCounter);
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

    public void updateUI() {
        ((DayCounterAdapter) (getListAdapter())).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.day_counters_title);
        mDayCounters = DayCounterLab.get(getActivity()).getDayCounters();
        DayCounterAdapter adapter = new DayCounterAdapter(mDayCounters);
        setListAdapter(adapter);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DayCounter dayCounter = (DayCounter) (getListAdapter().getItem(position));
        Log.d(TAG, dayCounter.getWhat() + " was clicked");
        mCallbacks.onDayCounterSelected(dayCounter);
    }

    private class DayCounterAdapter extends ArrayAdapter<DayCounter> {
        public DayCounterAdapter(List<DayCounter> dayCounters) {
            super(getActivity(), 0, dayCounters);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_day_counter, null);
            }

            // Configure the view for this DayCounter
            DayCounter dayCounter = getItem(position);
            TextView textView = (TextView) convertView.findViewById(R.id.day_counter_list_item_text_view);
//            Date today = new Date();
//            boolean isFutureEvent = dayCounter.getDate().getTime() > today.getTime() ;
            int diffInDays = DateUtil.diffInDays(dayCounter.getDate(), new Date());
            String title;

            if (diffInDays > 0) {
                title = getString(R.string.day_counter_list_future_title, dayCounter.getWhat(), diffInDays);
            } else {
                title = getString(R.string.day_counter_list_past_title, dayCounter.getWhat(), -diffInDays);
            }

            textView.setText(title);
            return convertView;
        }
    }
}
