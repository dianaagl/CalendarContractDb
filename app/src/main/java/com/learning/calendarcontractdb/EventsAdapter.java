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
import java.util.Date;
import java.util.List;



/**
 * Created by Диана on 12.06.2017.
 */
public class EventsAdapter extends BaseAdapter {
    private static final String TAG = "Adapter";
    public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm";
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
        return eventList.get(position).getId();
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
        holder.dtstartTextView.setText(getDate(event.getDtstart()));
        holder.dtendTextView.setText(getDate(event.getDtend()));
        holder.titleTextView.setText(event.getTitle());
        holder.descriptionTextView.setText(event.getDescription());
        return view;
    }
    public static String getDate(Long milliSeconds)
    {
        return new SimpleDateFormat(DATE_FORMAT).format(new Date(milliSeconds));
    }
    private static class EventHolder{
        private TextView dtstartTextView;
        private TextView dtendTextView;
        private TextView titleTextView;
        private TextView descriptionTextView;

        public EventHolder(View view) {
            dtstartTextView = (TextView) view.findViewById(R.id.start_event);
            dtendTextView = (TextView) view.findViewById(R.id.end_event);
            descriptionTextView = (TextView) view.findViewById(R.id.description_event);
            titleTextView = (TextView) view.findViewById(R.id.title_event);
        }

    }
}
