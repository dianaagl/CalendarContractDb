package com.learning.calendarcontractdb;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



/**
 * Created by Диана on 12.06.2017.
 */
public class EventsAdapter extends BaseAdapter {
    private static final String TAG = "Adapter";
    List<Event> eventList = new ArrayList<>();



    public EventsAdapter(List<Event> eventList) {
        this.eventList = new ArrayList<>(eventList);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eventList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG,"getView");
        View view = convertView;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
            view.setTag(new EventHolder(view));
        }

        EventHolder holder = (EventHolder) view.getTag();
        Event event = eventList.get(position);
        Log.e(TAG,"event ="+ event);
        Log.e(TAG,"time="+event.getDtstart() ); //event.getDtstart());
        //holder.eventTimezoneTextView.setText(event.getEventPlace());
        holder.dtstartTextView.setText(getDate(event.getDtstart()));
        holder.dtendTextView.setText(getDate(event.getDtend()));
        holder.titleTextView.setText(event.getTitle());
        holder.eventPlaceTextView.setText(event.getEventPlace());
        holder.descriptionTextView.setText(event.getDescription());
        //Log.e(TAG,"event=" + event);
        return view;
    }
    public static String getDate(Long milliSeconds)
    {
        String dateFormat = "dd/MM/yyyy hh:mm";

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

    }
    private static class EventHolder{
        private TextView dtstartTextView;
        private TextView dtendTextView;
        private TextView titleTextView;
        private TextView eventPlaceTextView;
        private TextView descriptionTextView;

        public EventHolder(View view) {
            dtstartTextView = (TextView) view.findViewById(R.id.start_event);
            dtendTextView = (TextView) view.findViewById(R.id.end_event);
            eventPlaceTextView = (TextView) view.findViewById(R.id.place_event);
            descriptionTextView = (TextView) view.findViewById(R.id.description_event);

            titleTextView = (TextView) view.findViewById(R.id.title_event);
        }

    }
}
