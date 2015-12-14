package com.nancy.daycounter.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.nancy.daycounter.R;
import com.nancy.daycounter.fragment.DayCounterDetailFragment;
import com.nancy.daycounter.fragment.DayCounterEditFragment;
import com.nancy.daycounter.fragment.DayCounterListFragment;
import com.nancy.daycounter.model.DayCounter;

/**
 * Created by nan.zhang on 11/26/15.
 */
public class DayCounterListActivity extends SingleFragmentActivity implements DayCounterListFragment.Callbacks, DayCounterDetailFragment.Callbacks, DayCounterEditFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new DayCounterListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;     // use alias
    }


    @Override
    public void onDayCounterSelected(DayCounter dayCounter) {
        if (findViewById(R.id.detail_fragment_container) == null) {     // small screen with one pane
            // start an instance of PagerActivity
            Intent intent = new Intent(this, DayCounterPagerActivity.class);
            intent.putExtra(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID, dayCounter.getId());
            startActivityForResult(intent, 0);
        } else {    // large screen with two panes
            // refresh detail fragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment newFragment = DayCounterDetailFragment.newInstance(dayCounter.getId());
            fragmentTransaction.replace(R.id.detail_fragment_container, newFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onDayCounterEditSelected(DayCounter dayCounter) {   // If called, it's using two-pane
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment editFragment = DayCounterEditFragment.newInstance(dayCounter.getId());
        fragmentTransaction.replace(R.id.detail_fragment_container, editFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDayCounterUpdated(DayCounter dayCounter) { // If called, it's using two-pane
        // go back to detail page
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment newFragment = DayCounterDetailFragment.newInstance(dayCounter.getId());
        fragmentTransaction.replace(R.id.detail_fragment_container, newFragment);
        fragmentTransaction.commit();

        // update list titie
        DayCounterListFragment listFragment = (DayCounterListFragment)getSupportFragmentManager().findFragmentById(R.id.list_fragment_container);
        listFragment.updateUI();
    }
}
