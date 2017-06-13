package com.learning.calendarcontractdb;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.Calendar;
import java.util.TimeZone;

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
    private Button deleteButton;
    private Button updateButton;


    private int year;
    private int month;
    private int day;

    private int hour;
    private int minut;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del_or_up_activity);
        datePicker = (DatePicker) findViewById(R.id.date_update);
        timePicker = (TimePicker) findViewById(R.id.time_update);
        titleEditText = (EditText) findViewById(R.id.title_update_event);
        placeEditText = (EditText) findViewById(R.id.location_update_event);
        descriptionEditText = (EditText) findViewById(R.id.description_update_event);

        deleteButton  = (Button) findViewById(R.id.delete_event);
        updateButton = (Button) findViewById(R.id.update_button);


        Intent intent = getIntent();

        titleEditText.setText(intent.getStringExtra(EventsActivity.TITLE));
        placeEditText.setText(intent.getStringExtra(EventsActivity.LOCATION));
        descriptionEditText.setText(intent.getStringExtra(EventsActivity.DESCRIPTION));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(intent.getLongExtra(EventsActivity.START_DATE,0));

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        timePicker.setHour(calendar.getTime().getHours());
        timePicker.setMinute(calendar.getTime().getMinutes());

        hour = timePicker.getHour();
        minut = timePicker.getMinute();

        datePicker.init(year,month,
                day, new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                DeleteOrUpdateActivity.this.year = year;
                DeleteOrUpdateActivity.this.month = month;
                DeleteOrUpdateActivity.this.day = dayOfMonth;


            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                DeleteOrUpdateActivity.this.hour = hourOfDay;
                DeleteOrUpdateActivity.this.minut = minute;
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"");
                DeleteOrUpdateActivity.this.getContentResolver().delete(
                        CalendarContract.Events.CONTENT_URI,
                        String.format(getResources().getString(R.string.select_id_format),
                                CalendarContract.Events._ID,
                                String.valueOf(getIntent().getLongExtra(EventsActivity.ID_EVENT,0))),
                        null);
                Log.e(TAG, "id="+getIntent().getLongExtra(EventsActivity.ID_EVENT,0));
                finish();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"update");
                ContentValues cv = new ContentValues();
                Long startMil = AddEventActivity.createMillisFromDate(year,month,day,hour,minut);
                cv.put(CalendarContract.Events.DTSTART, startMil);
                cv.put(CalendarContract.Events.DTEND,startMil);
                cv.put(CalendarContract.Events.TITLE, String.valueOf(titleEditText.getText()));
                cv.put(CalendarContract.Events.EVENT_LOCATION, String.valueOf(placeEditText.getText()));
                cv.put(CalendarContract.Events.DESCRIPTION,String.valueOf(descriptionEditText.getText()));
                cv.put(CalendarContract.Events.CALENDAR_ID, CalendarActivity.calID);
                cv.put(CalendarContract.Events.EVENT_TIMEZONE, String.valueOf(TimeZone.getDefault()));

                DeleteOrUpdateActivity.this.getContentResolver().update(CalendarContract.Events.CONTENT_URI,cv,
                        String.format(getResources().getString(R.string.select_id_format),
                                CalendarContract.Events._ID, String.valueOf(getIntent().getLongExtra(EventsActivity.ID_EVENT,0))) ,

                        null);
                Log.e(TAG,String.format(getResources().getString(R.string.select_id_format),
                        CalendarContract.Events._ID, String.valueOf(getIntent().getLongExtra(EventsActivity.ID_EVENT,0))));
                finish();
            }
        });







    }
}
