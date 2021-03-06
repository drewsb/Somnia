package com.socialarm.a350s18_5_socialalarmclock.Activity.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.socialarm.a350s18_5_socialalarmclock.Alarm.AlarmsOpenHelper;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.AlarmsUtil;
import com.socialarm.a350s18_5_socialalarmclock.Alarm.LocalDBContract;
import com.socialarm.a350s18_5_socialalarmclock.R;

import java.util.Calendar;


/**
 * This adapter reads a cursor from the local db and populates a view with data about a single alarm.
 */
public class SingleAlarmAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    /**
     * default constructor
     * @param context The context this adapter is used in.
     * @param c The cursor to read from.
     * @param flags Flags used to determine the behavior of the adapter.
     */
    public SingleAlarmAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Makes a new view to hold the data pointed to by cursor.
     * @param context Interface to applications's global information.
     * @param cursor The cursor from which to get the data.
     * @param parent The parent to which the new view is attached to.
     * @return The newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.single_alarm_layout, parent, false);
    }

    /**
     * Returns the true if the item at the specified position is not a separator.
     * @param position Index of the item.
     * @return True if the item is not a separator.
     */
    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    /**
     * Bind an existing view to the data pointed to by cursor.
     * @param view Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor The cursor from which to get the data. The cursor is already moved to the correct position.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final Context internal_context = context;
        final TextView time = view.findViewById(R.id.alarm_time);
        final Switch toggle = view.findViewById(R.id.alarm_on_off);

        final int hour = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_HOUR));
        final int minute = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_MINUTE));
        final int enabled = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_ENABLED));
        final int days_of_week = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm.COLUMN_NAME_DAY_OF_WEEK));
        final int id = cursor.getInt(cursor.getColumnIndex(LocalDBContract.Alarm._ID));

        int enabled_color = 0xFF000000;
        int disabled_color = 0xFF999999;


        toggle.setChecked(enabled == 1);
        time.setText(String.format("%02d:%02d", hour, minute));

        TextView sunday = view.findViewById(R.id.Sunday);
        sunday.setTextColor((days_of_week & AlarmsOpenHelper.SUNDAY) != 0 ? enabled_color : disabled_color);
        TextView monday = view.findViewById(R.id.Monday);
        monday.setTextColor((days_of_week & AlarmsOpenHelper.MONDAY) != 0 ? enabled_color : disabled_color);
        TextView tuesday = view.findViewById(R.id.Tuesday);
        tuesday.setTextColor((days_of_week & AlarmsOpenHelper.TUESDAY) != 0 ? enabled_color : disabled_color);
        TextView wednesday = view.findViewById(R.id.Wednesday);
        wednesday.setTextColor((days_of_week & AlarmsOpenHelper.WEDNESDAY) != 0 ? enabled_color : disabled_color);
        TextView thursday = view.findViewById(R.id.Thursday);
        thursday.setTextColor((days_of_week & AlarmsOpenHelper.THURSDAY) != 0 ? enabled_color : disabled_color);
        TextView friday = view.findViewById(R.id.Friday);
        friday.setTextColor((days_of_week & AlarmsOpenHelper.FRIDAY) != 0 ? enabled_color : disabled_color);
        TextView saturday = view.findViewById(R.id.Saturday);
        saturday.setTextColor((days_of_week & AlarmsOpenHelper.SATURDAY) != 0 ? enabled_color : disabled_color);

        // This section sets the on/off toggle for the alarms.
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AlarmsOpenHelper dbHelper = new AlarmsOpenHelper(internal_context);
                if (b) {
                    Calendar trigger_time = AlarmsUtil.getNextTrigger(hour, minute, days_of_week);

                    long tTime = trigger_time.getTimeInMillis();
                    Intent intent = new Intent(internal_context, AlarmEvent.class);
                    intent.putExtra("Alarm", id);
                    intent.setType("Alarm"+id);
                    PendingIntent Alarm = PendingIntent.getActivity(internal_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager manager = (AlarmManager) internal_context.getSystemService(Context.ALARM_SERVICE);
                    manager.setAlarmClock(new AlarmManager.AlarmClockInfo(tTime, Alarm), Alarm);
                    dbHelper.setActive(id, true);
                } else {
                    Intent intent = new Intent(internal_context, AlarmEvent.class);
                    intent.putExtra("alarm", id);
                    intent.setType("Alarm"+id);
                    PendingIntent Alarm = PendingIntent.getActivity(internal_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    AlarmManager manager = (AlarmManager) internal_context.getSystemService(Context.ALARM_SERVICE);
                    manager.cancel(Alarm);
                    dbHelper.setActive(id, false);
                }
            }
        });

        // This section sets the long press behavior for temporarily disabling an alarm.
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("LV", "item clicked");
                if (!toggle.isChecked()) {
                    return false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(internal_context);
                builder.setTitle("Disable next alarm?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing.
                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancel current alarm.
                        Intent intent = new Intent(internal_context, AlarmEvent.class);
                        intent.putExtra("alarm", id);
                        intent.setType("Alarm"+id);
                        PendingIntent Alarm = PendingIntent.getActivity(internal_context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                        AlarmManager manager = (AlarmManager) internal_context.getSystemService(Context.ALARM_SERVICE);
                        manager.cancel(Alarm);

                        // Set next alarm.
                        Calendar trigger_time = AlarmsUtil.skipNextTrigger(hour, minute, days_of_week);

                        long tTime = trigger_time.getTimeInMillis();
                        Intent new_intent = new Intent(internal_context, AlarmEvent.class);
                        intent.putExtra("Alarm", id);
                        PendingIntent new_alarm = PendingIntent.getActivity(internal_context, 0, new_intent, PendingIntent.FLAG_ONE_SHOT);
                        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(tTime, new_alarm), new_alarm);
                    }
                });
                builder.show();
                return true;
            }
        });

        view.setTag(id);
    }

}
