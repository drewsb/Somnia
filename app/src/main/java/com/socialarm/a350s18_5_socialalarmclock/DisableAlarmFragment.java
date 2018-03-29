package com.socialarm.a350s18_5_socialalarmclock;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;


/**
 * This fragment allows the user to disable all alarms for the near future.
 */
public class DisableAlarmFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.fragment_disable_alarm, null);

        final SeekBar s = v.findViewById(R.id.hour_selector);
        final TextView tv = v.findViewById(R.id.hour_display);
        tv.setText((s.getProgress() + 1) + " hours");

        // Update the text to reflect the slider in real time.
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tv.setText((i + 1) + " hours");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        builder.setView(v)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // Convert hours to ms.
                       long millisecond_delay = 1000*60*60*(s.getProgress() + 1);
                       long now = Calendar.getInstance().getTimeInMillis();

                       AlarmsOpenHelper dbHelper = new AlarmsOpenHelper(getContext());
                       Cursor cursor = dbHelper.getAlarms();
                       while (!cursor.isLast()) {
                           cursor.moveToNext();
                           final int hour = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_HOUR));
                           final int minute = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_MINUTE));
                           final int enabled = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_ENABLED));
                           final int days_of_week = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK));
                           final int alarm_id = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm._ID));

                           // If alarm isn't enabled, ignore it.
                           if (enabled == 0) {
                               continue;
                           }
                           // If alarm is further in future than selected time, ignore it.
                           Calendar nextTrigger = AlarmsUtil.getNextTrigger(hour, minute, days_of_week);
                           if (nextTrigger.getTimeInMillis() - now > millisecond_delay) {
                               continue;
                           }

                           // Cancel current trigger.
                           Intent intent = new Intent(getContext(), AlarmEvent.class);
                           intent.putExtra("Alarm", alarm_id);
                           intent.setType("Alarm"+alarm_id);
                           PendingIntent Alarm = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
                           AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                           manager.cancel(Alarm);

                           // Set the next trigger.
                           Calendar toTrigger = AlarmsUtil.skipNextTrigger(hour, minute, days_of_week);

                           long tTime = toTrigger.getTimeInMillis();
                           Intent new_intent = new Intent(getContext(), AlarmEvent.class);
                           intent.putExtra("Alarm", alarm_id);
                           intent.setType("Alarm"+alarm_id);
                           PendingIntent new_alarm = PendingIntent.getActivity(getContext(), 0, new_intent, PendingIntent.FLAG_ONE_SHOT);
                           manager.setAlarmClock(new AlarmManager.AlarmClockInfo(tTime, new_alarm), new_alarm);
                       }
                   }
               })
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // Do nothing on cancel.
                   }
               });
        return builder.create();
    }

}
