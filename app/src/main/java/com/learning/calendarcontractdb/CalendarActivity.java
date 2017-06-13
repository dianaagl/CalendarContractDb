package com.learning.calendarcontractdb;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;


import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    public static  long calID ;
    private static final String TAG = "CalendarActivity";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private int year;
    private int month;
    private int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);



        checkPermission();
        //createCalendarWithId();
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);
        calID = calendarView.getId();
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {

                CalendarActivity.this.year = year;
                CalendarActivity.this.month = month;
                CalendarActivity.this.day = dayOfMonth;


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        switch (item.getItemId()) {
            case R.id.add_event_menu_item: {

                Intent intent = new Intent(CalendarActivity.this,AddEventActivity.class);

                intent.putExtra(AddEventActivity.DAY,day);
                intent.putExtra(AddEventActivity.MONTH,month);
                intent.putExtra(AddEventActivity.YEAR,year);
                Log.e(TAG,"day="+ day+" " +month+" "+ year);
                startActivity(intent);
                break;
            }
            case R.id.show_all_events:{
                Intent intent = new Intent(CalendarActivity.this,EventsActivity.class);

                startActivity(intent);

            }

            default: {
                handled = super.onOptionsItemSelected(item);
                break;
            }
        }
        return handled;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG,"perm req code="+ requestCode);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                       ) {


                }

                return;
            }

        }

    }

    private void checkPermission() {
        Log.e(TAG,"request");
        if (ActivityCompat.checkSelfPermission(CalendarActivity.this,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(CalendarActivity.this,
                Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED ) {



            ActivityCompat.requestPermissions(CalendarActivity.this,
                    new String[] {Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},
                    PERMISSION_REQUEST_CODE);

        }
        else {

        }
    }


    private Uri asSyncAdapter(Uri uri) {
        return uri.buildUpon()
                .appendQueryParameter(android.provider.CalendarContract.CALLER_IS_SYNCADAPTER,"true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CalendarActivity.this.getString(R.string.app_name))
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL).build();
    }
    public void createCalendarWithId() {
        Uri createUri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI);
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.NAME, "Calendar " );
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, "");
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, "Owner");
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, CalendarActivity.this.getString(R.string.app_name));
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        Uri insertedCalendar = CalendarActivity.this.getContentResolver().insert(createUri, cv);
        //Uri insertedEvent = EventsActivity.this.getContentResolver().insert(CalendarContract.Events.CONTENT_URI, cv);
        calID = ContentUris.parseId(insertedCalendar);
        Log.e(TAG,"cal_id="+ calID);
    }

}
