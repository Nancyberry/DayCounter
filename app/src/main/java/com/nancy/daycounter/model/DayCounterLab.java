package com.nancy.daycounter.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by nan.zhang on 11/30/15.
 */
public class DayCounterLab {
    private static final String TAG = DayCounterLab.class.getSimpleName();
    private List<DayCounter> mDayCounters;
    private static DayCounterLab sDayCounterLab;
    private Context mAppContext;

    private DayCounterLab(Context appContext) {
        mAppContext = appContext;
        mDayCounters = new ArrayList<>();
        Random r = new Random();
        Log.d(TAG, "loading day counters...");

        // fake day counters currently
        for (int i = 0; i < 10; ++i) {
            DayCounter dayCounter = new DayCounter();
            dayCounter.setWhat("Day Counter #" + i);
            long unixtime = ((long) (1428874353 + r.nextDouble() * 60 * 60 * 24 * 365)) * 1000;
            Log.d(TAG, String.valueOf(unixtime));
            Date d = new Date(unixtime);
            dayCounter.setDate(d);
            mDayCounters.add(dayCounter);
        }

        Log.d(TAG, String.valueOf(new Date().getTime()));
    }

    public static DayCounterLab get(Context context) {
        if (sDayCounterLab == null) {
            sDayCounterLab = new DayCounterLab(context.getApplicationContext());
        }
        return sDayCounterLab;
    }

    public DayCounter getDayCounter(UUID uuid) {
        for (DayCounter dayCounter : mDayCounters) {
            if (dayCounter.getId().equals(uuid)) {
                return dayCounter;
            }
        }
        return null;
    }

//    public void updateDayCounter(DayCounter newDayCounter) {
//        for (DayCounter dayCounter : mDayCounters) {
//            if (dayCounter.getId().equals(newDayCounter.getId())) {
//                mDayCounters.get
//            }
//        }
//    }

    public void addDayCounter(DayCounter dayCounter) {
        mDayCounters.add(dayCounter);
    }

    public List<DayCounter> getDayCounters() {
        return mDayCounters;
    }
}
