package com.example.qlnv.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qlnv.CalendarUtils;
import com.example.qlnv.R;
import com.example.qlnv.model.Event;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    Context context;
    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.event_cell, parent, false);
        } else {
            row = convertView;
        }

        TextView eventCellTV = row.findViewById(R.id.eventCellTV);
        if (event.getStatus().equals("Đang làm")) {
            eventCellTV.setBackground(row.getContext().getResources().getDrawable(R.drawable.custom_cell_processing));
        } else if (event.getStatus().equals("Đã hoàn thành")) {
            eventCellTV.setBackground(row.getContext().getResources().getDrawable(R.drawable.custom_event_cell_finish));
        } else {
            eventCellTV.setBackground(row.getContext().getResources().getDrawable(R.drawable.custom_cell_event_red));
        }

        String eventTitle = event.getName() + " - " + CalendarUtils.formattedTime(event.getTime()) + " - " + event.getStatus();
        String assignTitle = "Giao cho: " + event.getIdUser();
        eventCellTV.setText(eventTitle + "\n" + assignTitle);
        return row;
    }
}
