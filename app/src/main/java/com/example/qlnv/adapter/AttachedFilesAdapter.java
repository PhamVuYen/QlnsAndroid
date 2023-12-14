package com.example.qlnv.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlnv.R;

import java.util.List;
public class AttachedFilesAdapter extends RecyclerView.Adapter<AttachedFilesAdapter.ViewHolder> {

    private List<Uri> files;
    Context context;

    public AttachedFilesAdapter(List<Uri> files,  Context context) {
        this.files = files;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attached_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri fileUri = files.get(position);
        String fileName = getFileNameFromUri(fileUri);
        holder.fileTextView.setText(fileName);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAttachedFile(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

     private void removeAttachedFile(int position) {
         files.remove(position);
         notifyItemChanged(position);
     }

    private String getFileNameFromUri(Uri uri) {
        String fileName = "";
        if (uri != null) {
            if (uri.getScheme().equals("content")) {
                try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                }
            } else if (uri.getScheme().equals("file")) {
                fileName = uri.getLastPathSegment();
            }
        }
        return fileName;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fileTextView;
        ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileTextView = itemView.findViewById(R.id.fileTextView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
