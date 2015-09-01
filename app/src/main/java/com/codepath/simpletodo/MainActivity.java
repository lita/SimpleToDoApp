package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.codepath.simpletodo.DueDateDialog.DatePickerFragmentListener;
import com.codepath.simpletodo.EditTaskDialog.EditTaskDialogListener;
import com.codepath.simpletodo.TaskAdapter.EditTask;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EditTaskDialogListener, EditTask, DatePickerFragmentListener {
    ArrayList<Task> tasks;
    TaskAdapter taskAdapter;
    ListView lvItems;
    public final static String EDIT_ITEM_VALUE = "com.codepath.simpletodo.MESSAGE";
    public final static String EDIT_ITEM_DATE = "com.codepath.simpletodo.DATE";
    public final static String EDIT_ITEM_POS = "com.codepath.simpletodo.ITEM_POS";
    private final int REQUEST_CODE = 20;
    public final static int RESULT_OK = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        setTitle("Lita's Simple To-Do App");
        readItems();
        taskAdapter = new TaskAdapter(this, android.R.layout.simple_list_item_1, tasks);
        taskAdapter.setCallback(this);
        lvItems.setAdapter(taskAdapter);
        setupListViewListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (itemText.isEmpty()) {
            return;
        }

        DateTime today = new DateTime();
        DateTime tomorrow = today.plusDays(1);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM/dd/YYYY");

        Task task = new Task(itemText, fmt.print(tomorrow));
        taskAdapter.add(task);

        etNewItem.setText("");
        writeItems();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {
                tasks.remove(pos);
                taskAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter,
                                    View item, int pos, long id) {
                showEditDialog(pos);
            }
        });
    }

    private void showEditDialog(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        Task task = tasks.get(pos);
        EditTaskDialog editTaskDialog = EditTaskDialog.newInstance("Edit Task", task.task, pos);
        editTaskDialog.show(fm, "fragment_edit_task");
    }

    private void showDueDateDialog(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        Task task = tasks.get(pos);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = format.parse(task.dueDate);
            DueDateDialog dueDateDialog = DueDateDialog.newInstance("Edit Due Date", date, pos);
            dueDateDialog.show(fm, "fragment_edit_task");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishEditDialog(String inputText, int position) {
        if (inputText.isEmpty()) {
            tasks.remove(position);
        } else {
            Task task = tasks.get(position);
            task.task = inputText;
        }
        taskAdapter.notifyDataSetChanged();
        writeItems();
    }

    @Override
    public void onDateSet(Date date, int position) {
        Task task = tasks.get(position);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        task.dueDate = format.format(date);
        taskAdapter.notifyDataSetChanged();
        writeItems();
    }

    @Override
    public void editDueDate(int pos) {
        showDueDateDialog(pos);
    }

    @Override
    public void editCompleted(boolean completed, int pos) {
        Task task = tasks.get(pos);
        task.completed = completed;
        taskAdapter.notifyDataSetChanged();
        writeItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        writeItems();
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        ArrayList<String> taskStrings;
        tasks = new ArrayList<Task>();
        try {
            taskStrings = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            taskStrings = new ArrayList<String>();
        }

        for (String taskStr : taskStrings) {
            try {
                JSONObject obj = new JSONObject(taskStr);
                Task task = new Task(obj.getString("task"), obj.getString("dueDate"));
                task.completed = obj.getBoolean("completed");
                tasks.add(task);
            } catch(org.json.JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
