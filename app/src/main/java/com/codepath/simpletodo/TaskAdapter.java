package com.codepath.simpletodo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by litacho on 8/30/15.
 */
public class TaskAdapter extends ArrayAdapter<Task> {
    private EditTask callback;

    public interface EditTask {
        public void editDueDate (int position);
        public void editCompleted (boolean completed, int position);
    }

    public TaskAdapter(Context context, @LayoutRes int resource, ArrayList<Task> tasks) {
        super(context, resource, tasks);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }
        // Lookup view for data population
        TextView taskView = (TextView) convertView.findViewById(R.id.task);
        Button dueDateView = (Button) convertView.findViewById(R.id.dueDate);
        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

        // / Populate the data into the template view using the data object
        dueDateView.setText(task.dueDate);
        checkBox.setChecked(task.completed);
        taskView.setText(task.task);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.editCompleted(checkBox.isChecked(), position);
                }
            }
        });

        dueDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.editDueDate(position);
                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public void setCallback(EditTask callback){

        this.callback = callback;
    }
}
