package com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.AlarmsOpenHelper;
import com.socialarm.a350s18_5_socialalarmclock.FirebaseMessaging.MessageSender;
import com.socialarm.a350s18_5_socialalarmclock.R;

/**
 * This fragment lists all the current alarms and lets the user create new ones.
 */
public class MyAlarms extends Fragment {
    static final int CREATE_ALARM_REQUEST = 1;

    View v;

    public final String TAG = "MyAlarms";

    AlarmsOpenHelper dbHelper;
    public static Context contextOfApplication;

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }


    /**
     * Create a new fragment.
     * @return The new fragment
     */
    public static MyAlarms newInstance() {
        MyAlarms fragment = new MyAlarms();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required empty public constructor
     */
    public MyAlarms() {}

    /**
     * Called when the fragment is created.
     * @param savedInstanceState Previous instance state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new AlarmsOpenHelper(getActivity());
        contextOfApplication = getActivity();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("ID", "Refreshed token: " + refreshedToken);
    }

    /**
     * Close the database to prevent memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    /**
     * Once the view has resumed, update table values
     */
    @Override
    public void onResume(){
        super.onResume();
        updateValues();
    }

    /**
     * Set the view and the FAB.
     * @param inflater The layout inflator to use
     * @param container The parent of the new view
     * @param savedInstanceState Previous instance state.
     * @return The new view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_alarms, container, false);
        updateValues();
        FloatingActionButton b = v.findViewById(R.id.add_alarm_button);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AlarmEditActivity.class);
                startActivityForResult(i, CREATE_ALARM_REQUEST);
            }
        });

        FloatingActionButton debug = v.findViewById(R.id.debug);
        debug.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MessageSender m = new MessageSender();
                m.notifyFriends("snooze");
            }
        });
        return v;
    }

    /**
     *
     * Called when another activity returns.
     * @param requestCode used to determine which result this is.
     * @param resultCode Success or failure of called activity
     * @param data Extra information returned from
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateValues();
    }

    /**
     * Recheck the local db for new alarms.
     */
    private void updateValues() {
        Cursor c = dbHelper.getAlarms();
        SingleAlarmAdapter adapter = new SingleAlarmAdapter(getContext(), c,0);
        ListView lv = v.findViewById(R.id.my_alarm_list);
        lv.setAdapter(adapter);
        lv.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }
}
