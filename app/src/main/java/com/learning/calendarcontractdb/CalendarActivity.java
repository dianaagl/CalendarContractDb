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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";

    private static final int LOADER_ID = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ListView dayEventsList;
    public static long calendarId = 1;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        dayEventsList  = (ListView) findViewById(R.id.day_events);
        dayEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CalendarActivity.this,DeleteOrUpdateActivity.class);
                Event event = (Event) dayEventsList.getItemAtPosition(position);
                intent.putExtra(Event.EventContract.TITLE,event.getTitle());
                intent.putExtra(Event.EventContract.EVENT_PLACE,event.getEventPlace());
                intent.putExtra(Event.EventContract.DTSTART,event.getDtstart());
                intent.putExtra(Event.EventContract.DTEND,event.getDtend());
                intent.putExtra(Event.EventContract.ID,event.getId());
                intent.putExtra(Event.EventContract.DESCRIPTION,event.getDescription());
                startActivity(intent);
            }
        });

        checkPermission();
        createCalendarWithId();
        selectedDate = Calendar.getInstance();
        selectedDate.set(Calendar.HOUR_OF_DAY,0);
        selectedDate.set(Calendar.MINUTE,0);
        findViewById(R.id.add_event_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this,AddEventActivity.class);

                intent.putExtra(AddEventActivity.DAY,selectedDate.get(Calendar.DAY_OF_MONTH));
                intent.putExtra(AddEventActivity.MONTH,selectedDate.get(Calendar.MONTH));
                intent.putExtra(AddEventActivity.YEAR,selectedDate.get(Calendar.YEAR));

                startActivity(intent);
            }
        });
       calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR,year);
                selectedDate.set(Calendar.MONTH,month);
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                getSupportLoaderManager().restartLoader(LOADER_ID, null,new  EventsLoaderCallbacks());
            }
        });

        getSupportLoaderManager().initLoader(LOADER_ID, null,new  EventsLoaderCallbacks());

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
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL).build();
    }
    public void createCalendarWithId() {
        Uri createUri = asSyncAdapter(CalendarContract.Calendars.CONTENT_URI);
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.NAME, getString(R.string.calendar_name) );
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, "");
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, getString(R.string.owner_account));
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, CalendarActivity.this.getString(R.string.app_name));
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        Uri insertedCalendar = CalendarActivity.this.getContentResolver().insert(createUri, cv);
        calendarId = ContentUris.parseId(insertedCalendar);

    }
    private class EventsLoaderCallbacks implements android.support.v4.app.LoaderManager.LoaderCallbacks<List<Event>> {
        @Override
        public android.support.v4.content.Loader<List<Event>> onCreateLoader(int id, Bundle args)
        {
            GregorianCalendar calendar = new GregorianCalendar(
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH));

            calendar.add(calendar.DAY_OF_MONTH,1);

            return new EventsLoader(CalendarActivity.this,selectedDate.getTimeInMillis(),
                                                                    calendar.getTimeInMillis());
        }

        @Override
        public void onLoadFinished(android.support.v4.content.Loader<List<Event>> loader, List<Event> data) {

            EventsAdapter adapter = new EventsAdapter(data);
            dayEventsList.setAdapter(adapter);

        }

        @Override
        public void onLoaderReset(android.support.v4.content.Loader<List<Event>> loader) {
        }
    }
}
