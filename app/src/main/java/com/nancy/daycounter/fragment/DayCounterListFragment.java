package com.nancy.daycounter.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nancy.daycounter.R;
import com.nancy.daycounter.activity.DayCounterEditActivity;
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
        Log.d(TAG, "update list fragment UI");
        ((DayCounterAdapter) (getListAdapter())).notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.day_counters_title);
        mDayCounters = DayCounterLab.get(getActivity()).getDayCounters();
        DayCounterAdapter adapter = new DayCounterAdapter(getActivity(), R.layout.list_item_day_counter, mDayCounters);
        setListAdapter(adapter);

        // inform fragment manager to response to option menu
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        // delete crime
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // Use floating context menus on Froyo and Gingerbread
            registerForContextMenu(listView);
        } else {
            // Use contexual action bar on Honeycomb and higher
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);    // Multiple choose mode
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.fragment_list_day_counter_context, menu);
                    return true;
                }

                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_day_counter:
                            DayCounterAdapter adapter = (DayCounterAdapter) getListAdapter();
                            DayCounterLab dayCounterLab = DayCounterLab.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    dayCounterLab.deleteDayCounter(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {

                }
            });

        }

        return v;
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume()");
        super.onResume();
        updateUI();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        DayCounter dayCounter = (DayCounter) (getListAdapter().getItem(position));
        Log.d(TAG, dayCounter.getWhat() + " was clicked");
        mCallbacks.onDayCounterSelected(dayCounter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_list_day_counter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_day_counter:
                DayCounter dayCounter = new DayCounter();
                dayCounter.setDate(new Date());
                dayCounter.setWhat("None");
                DayCounterLab.get(getActivity()).getDayCounters().add(dayCounter);
                Intent i = new Intent(getActivity(), DayCounterEditActivity.class);
                i.putExtra(DayCounterDetailFragment.EXTRA_DAY_COUNTER_ID, dayCounter.getId());
                startActivityForResult(i, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.fragment_list_day_counter_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        DayCounterAdapter adapter = (DayCounterAdapter) getListAdapter();
        DayCounter dayCounter = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_day_counter:
                DayCounterLab.get(getActivity()).deleteDayCounter(dayCounter);
                adapter.notifyDataSetChanged();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private class DayCounterAdapter extends ArrayAdapter<DayCounter> {
        private int resourceId;


        public DayCounterAdapter(Context context, int resourceId, List<DayCounter> dayCounters) {
            super(context, resourceId, dayCounters);
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            // If we weren't given a cached view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.day_counter_list_item_text_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // Configure the view for this DayCounter
            DayCounter dayCounter = getItem(position);
//            Date today = new Date();
//            boolean isFutureEvent = dayCounter.getDate().getTime() > today.getTime() ;
            int diffInDays = DateUtil.diffInDays(dayCounter.getDate(), new Date());
            String title;

            if (diffInDays > 0) {
                title = getString(R.string.day_counter_list_future_title, dayCounter.getWhat(), diffInDays);
            } else {
                title = getString(R.string.day_counter_list_past_title, dayCounter.getWhat(), -diffInDays);
            }

            viewHolder.textView.setText(title);
            return convertView;
        }
    }

    /**
     * To improve performance of ListView by caching component instances
     */
    private class ViewHolder {
        TextView textView;
    }
}
