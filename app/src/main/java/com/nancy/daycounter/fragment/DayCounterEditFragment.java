package com.nancy.daycounter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.nancy.daycounter.R;
import com.nancy.daycounter.model.DayCounter;
import com.nancy.daycounter.model.DayCounterLab;
import com.nancy.daycounter.util.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by nan.zhang on 11/27/15.
 */
public class DayCounterEditFragment extends Fragment implements DatePicker.OnDateChangedListener {
    private static final String TAG = DayCounterEditFragment.class.getSimpleName();

    private Callbacks mCallbacks;
    private DayCounter mDayCounter;
    private DatePicker mDatePicker;
    private TextView mDayTitle;
    private TextView mDayValue;
    private EditText mWhatValue;
    private Button mSubmitButton;

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

    public static DayCounterEditFragment newInstance(UUID dayCounterId) {
        Bundle args = new Bundle();
        args.putSerializable(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID, dayCounterId);
        DayCounterEditFragment fragment = new DayCounterEditFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID dayCounterId = (UUID) getArguments().getSerializable(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID);
        mDayCounter = DayCounterLab.get(getActivity()).getDayCounter(dayCounterId);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_day_counter, container, false);

        mDatePicker = (DatePicker) v.findViewById(R.id.date_picker);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDayCounter.getDate());
//        mDatePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1, calendar.get(Calendar.DATE));
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), this);

        mDayTitle = (TextView) v.findViewById(R.id.edit_day_title);
        mDayValue = (TextView) v.findViewById(R.id.edit_day_value);
        updateDayTitleAndValue(mDayCounter.getDate());

        mWhatValue = (EditText) v.findViewById(R.id.edit_what_value);
        mWhatValue.setText(mDayCounter.getWhat());

        mSubmitButton = (Button) v.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date newDate = new GregorianCalendar(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth()).getTime();
                String newWhat = mWhatValue.getText().toString();
                mDayCounter.setDate(newDate);
                mDayCounter.setWhat(newWhat);
                mCallbacks.onDayCounterUpdated(mDayCounter);
            }
        });

        return v;
    }

    private void updateDayTitleAndValue(Date date) {
        int diffInDays = DateUtil.diffInDays(date, new Date());

        if (diffInDays < 0) {
            mDayTitle.setText(getString(R.string.day_counter_edit_past_title));
            mDayValue.setText(String.valueOf(-diffInDays));
        } else {
            mDayTitle.setText(getString(R.string.day_counter_edit_future_title));
            mDayValue.setText(String.valueOf(diffInDays));
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(TAG, "update to " + year + "/" + monthOfYear + "/" + dayOfMonth);
        Date newDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
//        Toast.makeText(getActivity().getApplicationContext(),
//                "onDateChanged", Toast.LENGTH_SHORT).show();
        updateDayTitleAndValue(newDate);
    }
}
