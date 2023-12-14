package com.example.qlnv.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlnv.Injector;
import com.example.qlnv.OnClickListener;
import com.example.qlnv.R;
import com.example.qlnv.model.Task;
import com.example.qlnv.model.TimeKeeping;

import java.util.ArrayList;

public class TimeKeepingAdapter extends RecyclerView.Adapter<TimeKeepingAdapter.ViewHolder> implements Filterable {

    private ArrayList<TimeKeeping> list;
    private ArrayList<TimeKeeping> listFiltered;
    private Context context;

    public TimeKeepingAdapter(ArrayList<TimeKeeping> list, Context context) {
        this.list = list;
        this.listFiltered = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.item_tableview, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeKeeping timeKeeping = listFiltered.get(position);
        holder.tvID.setText(timeKeeping.getIdNv());
        holder.tvDay.setText(timeKeeping.getDay());
        holder.tvTimeArrive.setText(timeKeeping.getCheckIn());
        holder.tvTimeLeave.setText(timeKeeping.getCheckOut());
    }

    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvID, tvDay, tvTimeArrive, tvTimeLeave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTimeArrive = itemView.findViewById(R.id.tvTimeArrive);
            tvTimeLeave = itemView.findViewById(R.id.tvTimeLeave);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    ArrayList<TimeKeeping> filteredList = new ArrayList<>();
                    for (TimeKeeping timeKeeping : list) {
                        // Check if the ID contains the search query
                        if (timeKeeping.getIdNv().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(timeKeeping);
                        }
                    }
                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (ArrayList<TimeKeeping>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}