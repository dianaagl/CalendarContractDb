package com.learning.calendarcontractdb;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Диана on 12.06.2017.
 */
public class EventsLoader extends AsyncTaskLoader<List<Event> > {

    private EventsContentObserver eventsContentObserver;


    private static final String TAG = "Loader";
    private long beginTime,endTime;

    public EventsLoader(Context context, long beginTime, long endTime) {

        super(context);
        this.beginTime = beginTime;
        this.endTime = endTime;
        Log.e(TAG,"eventsLoader");
        eventsContentObserver = new EventsContentObserver();
        context.getContentResolver().registerContentObserver(
                CalendarContract.Events.CONTENT_URI,
                false, eventsContentObserver
        );
    }

    @Override
    public List<Event> loadInBackground() {

        List<Event> list = new ArrayList<>();
        Cursor cursor = getEventsCursor();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Event event = createEventFromCursor(cursor);
                    if(event.getDtstart() > beginTime && event.getDtstart() < endTime) {
                        list.add(event);
                    }
                    cursor.moveToNext();
                    Log.e(TAG, "time " + beginTime + " - " + endTime+ " = " + event.getDtstart());
                }
            }
            cursor.close();

        } else {
            Log.e(TAG, "cursor is null");
        }

        return list;
    }
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();

    }

    @Override
    protected void onReset() {
        super.onReset();
        getContext().getContentResolver().unregisterContentObserver(eventsContentObserver);
    }

    private static Event createEventFromCursor(Cursor cursor) {
        Event event = new Event();

        event.setCalendar_id(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.CALENDAR_ID)));
        event.setDtstart(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
        event.setDtend(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND)));
        event.setDuration(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DURATION)));
        event.setEventPlace(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)));
        event.setRdate(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RDATE)));
        event.setId(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID)));
        event.setRrule(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RRULE)));
        event.setTitle(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION)));

        return event;
    }
    private Cursor getEventsCursor() {
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    null, null, null, null);

        return cursor;
    }
    private class EventsContentObserver extends ContentObserver {

        public EventsContentObserver() {
            super(new Handler(Looper.getMainLooper()));
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            onContentChanged();
        }
    }
}
