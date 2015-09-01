package com.codepath.simpletodo;

import org.json.JSONObject;

/**
 * Created by litacho on 8/30/15.
 */
public class Task {
    public String task;
    public String dueDate;
    public Boolean completed;

    public Task(String task, String dueDate) {
        this.task = task;
        this.dueDate = dueDate;
        this.completed = false;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("task", task);
            obj.put("dueDate", dueDate);
            obj.put("completed", completed);
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();
    }
}
