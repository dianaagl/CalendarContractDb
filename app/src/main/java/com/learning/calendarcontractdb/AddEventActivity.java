package com.learning.calendarcontractdb;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Диана on 12.06.2017.
 */
public class AddEventActivity extends AppCompatActivity{
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String DAY = "day";

    private GregorianCalendar startDateCalendar;
    private GregorianCalendar endDateCalendar;

    private DatePicker startDatePicker;
    private TimePicker startTimePicker;
    private DatePicker endDatePicker;
    private TimePicker endTimePicker;
    private EditText placeEditText;
    private EditText titleEditText;
    private EditText descriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_or_up_activity);
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolbar));

        startDatePicker = (DatePicker) findViewById(R.id.date_start_pick);
        startTimePicker = (TimePicker) findViewById(R.id.time_update);
        placeEditText = (EditText) findViewById(R.id.location_update_event);
        titleEditText = (EditText) findViewById(R.id.title_update_event);
        descriptionEditText = (EditText) findViewById(R.id.description_update_event);
        endDatePicker = (DatePicker) findViewById(R.id.date_end_pick);
        endTimePicker = (TimePicker) findViewById(R.id.time_end);
        startDateCalendar = new GregorianCalendar();
        endDateCalendar = new GregorianCalendar();



        Intent intent = getIntent();
        startDateCalendar.set(Calendar.YEAR,intent.getIntExtra(YEAR, 2017));
        startDateCalendar.set(Calendar.MONTH,intent.getIntExtra(MONTH, 1));
        startDateCalendar.set(Calendar.DAY_OF_MONTH,intent.getIntExtra(DAY, 1));

        startDatePicker.init(startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDateCalendar.set(Calendar.YEAR,year);
                        startDateCalendar.set(Calendar.MONTH,monthOfYear);
                        startDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    }
                });
        endDatePicker.init(startDateCalendar.get(Calendar.YEAR),
                startDateCalendar.get(Calendar.MONTH),
                startDateCalendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDateCalendar.set(Calendar.YEAR,year);
                        endDateCalendar.set(Calendar.MONTH,monthOfYear);
                        endDateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startTimePicker.setHour(startDateCalendar.get(Calendar.HOUR));
            startTimePicker.setMinute(startDateCalendar.get(Calendar.MINUTE));

            endTimePicker.setHour(startDateCalendar.get(Calendar.HOUR));
            endTimePicker.setMinute(startDateCalendar.get(Calendar.MINUTE));

        } else {
            startTimePicker.setCurrentHour(startDateCalendar.get(Calendar.HOUR));
            startTimePicker.setCurrentMinute(startDateCalendar.get(Calendar.MINUTE));

            endTimePicker.setCurrentHour(startDateCalendar.get(Calendar.HOUR));
            endTimePicker.setCurrentMinute(startDateCalendar.get(Calendar.MINUTE));
        }



        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                startDateCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                startDateCalendar.set(Calendar.MINUTE,minute);
            }
        });
        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                endDateCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                endDateCalendar.set(Calendar.MINUTE,minute);
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
        switch (item.getItemId()) {
            case R.id.delete_event:
                finish();
                break;
            case R.id.update_event:

                String title;
                String place;
                String description;
                try{
                    title = String.valueOf(titleEditText.getText());
                    place = String.valueOf(placeEditText.getText());
                    description = String.valueOf(descriptionEditText.getText());
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                    title = "";
                    place = "";
                    description = "";
                };

                createEventForCalendarWithId(String.valueOf(CalendarActivity.calendarId),
                        startDateCalendar.getTimeInMillis(),
                        endDateCalendar.getTimeInMillis(),
                        description,
                        title,
                        place);

                finish();
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    public void createEventForCalendarWithId(String calendarId, long startTime,long endTime, String description,
                                             String title,String location) {
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Events.DTSTART, startTime);
        cv.put(CalendarContract.Events.DTEND, endTime);
        cv.put(CalendarContract.Events.TITLE, title);
        cv.put(CalendarContract.Events.EVENT_LOCATION,location);
        cv.put(CalendarContract.Events.DESCRIPTION, description);
        cv.put(CalendarContract.Events.CALENDAR_ID, calendarId);
        cv.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));


        AddEventActivity.this.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, cv);



    }
}
