package com.learning.calendarcontractdb;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Диана on 12.06.2017.
 */
public class EventsActivity extends AppCompatActivity {

    public static final String TITLE = "title";
    public static final String LOCATION = "location";
    public static final String START_DATE = "start";
    public static final String ID_EVENT = "id";
    public static final String DESCRIPTION = "description";

    public static final String END_TIME = "end";


    private static final int LOADER_ID = 1;
    private static final String TAG = "EventsActivity";



    private TextView displayNameTextView;
    private EventsAdapter eventsAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_activity);
        listView = (ListView) findViewById(R.id.event_list);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventsActivity.this,DeleteOrUpdateActivity.class);
                Event event = (Event) listView.getItemAtPosition(position);
                intent.putExtra(TITLE,event.getTitle());
                intent.putExtra(LOCATION,event.getEventPlace());
                intent.putExtra(START_DATE,event.getDtstart());
                intent.putExtra(END_TIME,event.getDtend());
                intent.putExtra(ID_EVENT,event.getId());
                intent.putExtra(DESCRIPTION,event.getDescription());
                startActivity(intent);
            }
        });
        //
        getSupportLoaderManager().initLoader(LOADER_ID, null,
                new EventsLoaderCallbacks());
    }



    private class EventsLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Event>> {

        @Override
        public Loader<List<Event>> onCreateLoader(int id, Bundle args)
        {
            return new EventsLoader(EventsActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
            Log.e(TAG,"data=" + data);

            EventsAdapter adapter = new EventsAdapter(data);
            listView.setAdapter(adapter);

        }

        @Override
        public void onLoaderReset(Loader<List<Event>> loader) {
        }
    }
}
