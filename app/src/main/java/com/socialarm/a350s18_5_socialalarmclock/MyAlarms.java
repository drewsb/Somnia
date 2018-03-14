package com.socialarm.a350s18_5_socialalarmclock;

import android.content.Intent;
import android.net.Uri;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class MyAlarms extends Fragment {
    static final int CREATE_ALARM_REQUEST = 1;

    View v;

    AlarmsOpenHelper dbHelper;

    public static MyAlarms newInstance() {
        MyAlarms fragment = new MyAlarms();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyAlarms() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new AlarmsOpenHelper(getActivity());
        //dbHelper.onCreate(dbHelper.getWritableDatabase());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_alarms, container, false);
        UpdateValues();
        FloatingActionButton b = v.findViewById(R.id.add_alarm_button);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AlarmEditActivity.class);
                startActivityForResult(i, CREATE_ALARM_REQUEST);
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UpdateValues();
    }

    private void UpdateValues() {
        Cursor c = dbHelper.getAlarms();
        SingleAlarmAdapter adapter = new SingleAlarmAdapter(getContext(), c,0);
        ListView lv = v.findViewById(R.id.my_alarm_list);
        lv.setAdapter(adapter);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
