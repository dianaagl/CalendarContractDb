package com.learning.calendarcontractdb;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Диана on 12.06.2017.
 */
public class AddEventActivity extends Activity{
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String DAY = "day";

    private int year;
    private int month;
    private int day;

    private TextView dateTextView;
    private TimePicker timePicker;
    private EditText placeEditText;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button addEventButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);
        dateTextView = (TextView) findViewById(R.id.date_add_event);
        timePicker = (TimePicker) findViewById(R.id.time_add_event);
        placeEditText = (EditText) findViewById(R.id.time_zone_add_event);
        titleEditText = (EditText) findViewById(R.id.title_add_event);
        descriptionEditText = (EditText) findViewById(R.id.description_add_event);
        addEventButton = (Button) findViewById(R.id.add_event_but);


        Intent intent = getIntent();
        year = intent.getIntExtra(YEAR, 2017);
        month = intent.getIntExtra(MONTH, 1);
        day = intent.getIntExtra(DAY, 1);
        dateTextView.setText(String.format(getResources().getString(R.string.date_format), year, month, day));

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setTitle(String.valueOf(titleEditText.getText()));
                event.setEventPlace(String.valueOf(placeEditText.getText()));

                event.setDtstart(createMillisFromDate(year,month,day,timePicker.getHour(),timePicker.getMinute()));
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
                }
                createEventForCalendarWithId(String.valueOf(CalendarActivity.calID),
                        createMillisFromDate(year,month,day,timePicker.getHour(),timePicker.getMinute()),
                        createMillisFromDate(year,month,day,timePicker.getHour(),timePicker.getMinute()),
                        description,title
                        , place);
                finish();
            }
        });
    }
    public static Long createMillisFromDate(int year,int month,int day,int hours,int minuts){
        long millis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, hours, minuts);
        millis = beginTime.getTimeInMillis();
        return millis;
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
