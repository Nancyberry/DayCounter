package com.nancy.daycounter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nancy.daycounter.R;
import com.nancy.daycounter.model.DayCounter;
import com.nancy.daycounter.model.DayCounterLab;
import com.nancy.daycounter.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by nan.zhang on 11/26/15.
 */
public class DayCounterDetailFragment extends Fragment {
    public static final String EXTRA_DAY_COUNTER_ID = DayCounterDetailFragment.class.getName();

    private DayCounter mDayCounter;
    private TextView mDayCountTextView;
    private TextView mOriginalDayTextView;
    private TextView mWhatDayTextView;
    private Button mEditButton;
    private Callbacks mCallbacks;

    public interface Callbacks {
        public void onDayCounterEditSelected(DayCounter dayCounter);
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

    public static DayCounterDetailFragment newInstance(UUID dayCounterId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DAY_COUNTER_ID, dayCounterId);
        DayCounterDetailFragment fragment = new DayCounterDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID dayCounterId = (UUID) getArguments().getSerializable(EXTRA_DAY_COUNTER_ID);
        mDayCounter = DayCounterLab.get(getActivity()).getDayCounter(dayCounterId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_day_counter, container, false);
        mDayCountTextView = (TextView) v.findViewById(R.id.day_count_text);
        mOriginalDayTextView = (TextView) v.findViewById(R.id.original_day_text);
        mWhatDayTextView = (TextView) v.findViewById(R.id.what_day_text);
        mEditButton = (Button) v.findViewById(R.id.edit_detail_button);

        int diffInDays = DateUtil.diffInDays(mDayCounter.getDate(), new Date());
        if (diffInDays < 0) {
            mDayCountTextView.setText(getString(R.string.day_counter_detail_past_title, -diffInDays));
        } else if (diffInDays == 0) {
//            mDayCountTextView.setText(getString(R.string.day_counter_detail_current_title));
        } else {
            mDayCountTextView.setText(getString(R.string.day_counter_detail_future_title, diffInDays));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.day_counter_detail_date_format));
        mOriginalDayTextView.setText(dateFormat.format(mDayCounter.getDate()));

        mWhatDayTextView.setText(mDayCounter.getWhat());

        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.onDayCounterEditSelected(mDayCounter);
            }
        });

        return v;
    }
}
