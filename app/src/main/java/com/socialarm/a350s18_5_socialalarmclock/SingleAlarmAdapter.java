package com.socialarm.a350s18_5_socialalarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;


public class SingleAlarmAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    public SingleAlarmAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.single_alarm_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final Context internal_context = context;
        TextView time = view.findViewById(R.id.alarm_time);
        Switch toggle = view.findViewById(R.id.alarm_on_off);

        final int hour = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_HOUR));
        final int minute = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_MINUTE));
        final int enabled = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_ENABLED));
        final int id = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm._ID));

        toggle.setChecked(enabled == 1);
        time.setText(String.format("%02d:%02d", hour, minute));
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmsOpenHelper dbHelper = new AlarmsOpenHelper(internal_context);
                if (b) {
                    //LocalTime trigger_time = LocalTime.of(a.getHour(),a.getMinute());
                    Calendar trigger_time = Calendar.getInstance();
                    trigger_time.set(Calendar.HOUR_OF_DAY, hour);
                    trigger_time.set(Calendar.MINUTE, minute);
                    trigger_time.set(Calendar.SECOND, 0);
                    Calendar now = Calendar.getInstance();
                    if (trigger_time.before(now)) {
                        trigger_time.add(Calendar.DAY_OF_MONTH, 1); // Set for tomorrow
                    }
                    long tTime = trigger_time.getTimeInMillis();
                    Intent intent = new Intent(internal_context, AlarmEvent.class);
                    intent.putExtra("Alarm", id);
                    PendingIntent Alarm = PendingIntent.getActivity(internal_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager manager = (AlarmManager) internal_context.getSystemService(Context.ALARM_SERVICE);
                    manager.setExact(AlarmManager.RTC_WAKEUP, tTime, Alarm);
                    dbHelper.setActive(id, true);
                } else {
                    Intent intent = new Intent(internal_context, AlarmEvent.class);
                    intent.putExtra("alarm", id);
                    PendingIntent Alarm = PendingIntent.getActivity(internal_context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    dbHelper.setActive(id, false);
                }
            }
        });
    }
}
