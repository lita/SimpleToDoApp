package com.codepath.simpletodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by litacho on 8/31/15.
 */
public class DueDateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private int position;
    private static String DATE_KEY = "DATE_KEY";

//    public interface EditDueDateDialogListener {
//        void onFinishDueDateDialog(int year, int month, int day, int position);
//    }
    public interface DatePickerFragmentListener {
        public void onDateSet(Date date, int position);
    }

    public DueDateDialog() {
        // Empty constructor required for DialogFragment
    }

    public static DueDateDialog newInstance(String title, Date date, int pos) {
        DueDateDialog frag = new DueDateDialog();
        Bundle args = new Bundle();
        args.putSerializable(DATE_KEY, date);
        args.putString("title", title);
        frag.setArguments(args);
        frag.position = pos;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        String title = getArguments().getString("title", "Enter Due Date");
        Date initialDate = (Date) getArguments().getSerializable(DATE_KEY);
        int[] yearMonthDay = getYearMonthDayArray(initialDate);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yearMonthDay[0], yearMonthDay[1],
                yearMonthDay[2]);
        dialog.setTitle(title);
        dialog.getCurrentFocus();

        return dialog;
    }

    protected void notifyDatePickerListener(Date date) {
        DatePickerFragmentListener listener = (DatePickerFragmentListener) getActivity();
        listener.onDateSet(date, position);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        Date date = c.getTime();

        notifyDatePickerListener(date);
        dismiss();
    }

    private int[] getYearMonthDayArray(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)};
    }
}
