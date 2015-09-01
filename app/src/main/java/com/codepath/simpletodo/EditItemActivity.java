package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditItemActivity extends AppCompatActivity {
    Integer pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EDIT_ITEM_VALUE);
        pos = intent.getIntExtra(MainActivity.EDIT_ITEM_POS, 0);
        String dateStr = intent.getStringExtra(MainActivity.EDIT_ITEM_DATE);
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
        DatePicker dueDatePicker = (DatePicker) findViewById(R.id.datePicker);

        try {
            Date date = fmt.parse(dateStr);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_edit_item);
        EditText etNewItem = (EditText) findViewById(R.id.editText);

        etNewItem.setText(message);
        etNewItem.setSelection(etNewItem.getText().length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSave(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.editText);
        String itemText = etNewItem.getText().toString();
        DatePicker dueDatePicker = (DatePicker) findViewById(R.id.datePicker);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String selectedDate = sdf.format(getDateFromDatePicker(dueDatePicker));
        Intent data = new Intent();
        data.putExtra(MainActivity.EDIT_ITEM_VALUE, itemText);
        data.putExtra(MainActivity.EDIT_ITEM_POS, pos);
        data.putExtra(MainActivity.EDIT_ITEM_DATE, selectedDate);
        setResult(MainActivity.RESULT_OK, data);
        this.finish();
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
}
