package com.learning.calendarcontractdb;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.Calendar;
import java.util.TimeZone;

import static com.learning.calendarcontractdb.Event.EventContract.*;

/**
 * Created by Диана on 13.06.2017.
 */
public class DeleteOrUpdateActivity extends AppCompatActivity{
    private static final String TAG = "delete_activity";
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText titleEditText;
    private EditText placeEditText;
    private EditText descriptionEditText;
    private Spinner rruleSpinner;
    private Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_or_up_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        datePicker = (DatePicker) findViewById(R.id.date_start_pick);
        timePicker = (TimePicker) findViewById(R.id.time_update);
        titleEditText = (EditText) findViewById(R.id.title_update_event);
        placeEditText = (EditText) findViewById(R.id.location_update_event);
        descriptionEditText = (EditText) findViewById(R.id.description_update_event);
        rruleSpinner = (Spinner) findViewById(R.id.rrule_spinner);

        Intent intent = getIntent();

        rruleSpinner.setSelection(Event.EventContract.rruleList.indexOf(intent.getStringExtra(RRULE)));

        titleEditText.setText(intent.getStringExtra(Event.EventContract.TITLE));
        placeEditText.setText(intent.getStringExtra(EVENT_PLACE));
        descriptionEditText.setText(intent.getStringExtra(DESCRIPTION));

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intent.getLongExtra(DTSTART, 0));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(calendar.get(Calendar.HOUR));
            timePicker.setMinute(calendar.get(Calendar.MINUTE));
        } else {
            timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
            timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }


        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }
                });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.del_or_update_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_event:
                ContentValues cv = new ContentValues();
                Long startMil = calendar.getTimeInMillis();
                cv.put(CalendarContract.Events.DTSTART, startMil);
                cv.put(CalendarContract.Events.DTEND,startMil);
                cv.put(CalendarContract.Events.TITLE, String.valueOf(titleEditText.getText()));
                cv.put(CalendarContract.Events.EVENT_LOCATION, String.valueOf(placeEditText.getText()));
                cv.put(CalendarContract.Events.DESCRIPTION,String.valueOf(descriptionEditText.getText()));
                cv.put(CalendarContract.Events.CALENDAR_ID, CalendarActivity.calendarId);
                cv.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));

                getApplicationContext().getContentResolver().update(
                        CalendarContract.Events.CONTENT_URI,
                        cv,
                        String.format(getResources().getString(R.string.select_id_format),
                                CalendarContract.Events._ID,
                                String.valueOf(getIntent().getLongExtra(ID,0))) ,

                        null);
                finish();
                break;
            case R.id.delete_event:
                DeleteOrUpdateActivity.this.getContentResolver().delete(
                        CalendarContract.Events.CONTENT_URI,
                        String.format(getResources().getString(R.string.select_id_format),
                                CalendarContract.Events._ID,
                                String.valueOf(getIntent().getLongExtra(ID,0))),
                        null);
                Log.e(TAG, "id="+getIntent().getLongExtra(ID,0));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }








}
