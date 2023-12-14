package com.example.qlnv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlnv.OnClickListenerAllTask;
import com.example.qlnv.R;
import com.example.qlnv.model.Task;

import java.util.ArrayList;

public class AllTaskAdapter extends RecyclerView.Adapter<AllTaskAdapter.ViewHolder> {
    public ArrayList<Task> tasks;
    Context context;
    OnClickListenerAllTask onClickListener;

    public AllTaskAdapter(ArrayList<Task> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    public void setOnClickListener(OnClickListenerAllTask onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.all_task_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.tvName.setText(task.getTask_name());
        holder.tvDeadLine.setText(task.getDeadline());
        holder.tvStatus.setText(task.getTask_status());
        holder.tvAssignFor.setText(task.getUser_id());
        holder.layoutTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showMenu(view, task, position);
                return false;
            }
        });
    }

    private void showMenu(View v, Task task, int position) {
        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(item -> {
            if (onClickListener != null) {
                onClickListener.onItemLongClick(item.getItemId(), position, task);
            }
            return false;
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_all_task, popup.getMenu());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDeadLine, tvStatus, tvAssignFor;
        private LinearLayout layoutTask;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutTask = itemView.findViewById(R.id.layoutTask);
            tvName = itemView.findViewById(R.id.task_name);
            tvDeadLine = itemView.findViewById(R.id.tvDeadline);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAssignFor = itemView.findViewById(R.id.tvAssignFor);
        }
    }
}


