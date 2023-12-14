package com.example.qlnv.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlnv.OnClickListener;
import com.example.qlnv.R;
import com.example.qlnv.model.Task;

import java.util.List;

public class TaskNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_TIME_HEADER = 0;
    private static final int VIEW_TYPE_TASK = 1;

    public List<Object> items;
    static OnClickListener onClickListener;
    static Context context;

    public void update(List<Object> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public TaskNewAdapter(List<Object> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return VIEW_TYPE_TIME_HEADER;
        } else {
            return VIEW_TYPE_TASK;
        }
    }

    public void clearData() {
        items.clear(); // Assuming taskList is the list containing your data
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_TIME_HEADER) {
            View view = inflater.inflate(R.layout.list_item_time_header, parent, false);
            return new TimeHeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.task_layout, parent, false);
            return new TaskViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TimeHeaderViewHolder) {
            String time = (String) items.get(position);
            ((TimeHeaderViewHolder) holder).bind(time);
        } else if (holder instanceof TaskViewHolder) {
            Task task = (Task) items.get(position);
            ((TaskViewHolder) holder).bind(task);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class TimeHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView timeHeaderTextView;

        TimeHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            timeHeaderTextView = itemView.findViewById(R.id.timeHeaderTextView);
        }

        void bind(String time) {
            timeHeaderTextView.setText(time.substring(0, 10));
        }
    }

    private static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvDeadLine,tvStatus;
        private LinearLayout layoutTask;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutTask = itemView.findViewById(R.id.layoutTask);
            tvName = itemView.findViewById(R.id.task_name);
            tvDeadLine = itemView.findViewById(R.id.tvDeadline);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }

        @SuppressLint("ResourceAsColor")
        void bind(Task task) {
            tvName.setText(task.getTask_name());
            tvDeadLine.setText(task.getDeadline());
            tvStatus.setText(task.getTask_status());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setCornerRadius(context.getResources().getDimension(R.dimen.corner_radius));
            switch (task.getTask_status()) {
                case "Quá hạn":
                    gradientDrawable.setColor(ContextCompat.getColor(context, R.color.red));
                    break;
                case "Đã hoàn thành":
                    gradientDrawable.setColor(ContextCompat.getColor(context, R.color.green));
                    break;
                case "Đang làm":
                case "Chưa xong":
                    gradientDrawable.setColor(ContextCompat.getColor(context, R.color.yellow));
                    break;

            }


            // Set the background drawable with the GradientDrawable
            layoutTask.setBackground(gradientDrawable);
            layoutTask.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onClickListener.onItemLongClick(task.getTask_id());
                    return false;
                }
            });
        }
    }
}

