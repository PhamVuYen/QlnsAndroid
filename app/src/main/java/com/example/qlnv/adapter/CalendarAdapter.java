package com.example.qlnv.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlnv.CalendarUtils;
import com.example.qlnv.R;
import com.example.qlnv.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private ArrayList<Event> dailyEvents = new ArrayList<>();
    private final OnItemListener onItemListener;
    private HashSet<LocalDate> localDates = new HashSet<>();
    ArrayList<LocalDate> arr;
    Context context;

    public CalendarAdapter(ArrayList<LocalDate> days, OnItemListener onItemListener,Context context) {
        this.days = days;
        this.onItemListener = onItemListener;
        this.context = context;
    }

    public void setDailyEvents(ArrayList<Event> dailyEvents) {
        this.dailyEvents = dailyEvents;
    }

    public void setLocalDates(HashSet<LocalDate> localDates) {
        this.localDates = localDates;
        arr = new ArrayList<>(localDates);
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        if (days.size() > 15) //month view
//            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
//        else // week view
//            layoutParams.height = (int) parent.getHeight();

        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);
        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
        Log.d("localDate",arr+"|" +date);
        if (arr.contains(date)) {
            holder.imageView.setVisibility(View.VISIBLE);
        } else {
            holder.imageView.setVisibility(View.INVISIBLE);
        }

        if (date.equals(CalendarUtils.selectedDate)) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.custom_cell_calendar);
            holder.parentView.setBackground(drawable);
        }
//            holder.parentView.setBackgroundColor(Color.LTGRAY);

        if (date.getMonth().equals(CalendarUtils.selectedDate.getMonth()))
            holder.dayOfMonth.setTextColor(Color.BLACK);
        else
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, LocalDate date);
    }
}
