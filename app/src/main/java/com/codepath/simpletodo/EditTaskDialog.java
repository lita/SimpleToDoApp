package com.codepath.simpletodo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class EditTaskDialog extends DialogFragment implements OnEditorActionListener {
    private EditText mEditText;
    private int position;
    private String oldValue;

    public interface EditTaskDialogListener {
        void onFinishEditDialog(String inputText, int position);
    }

    public EditTaskDialog() {
        // Empty constructor required for DialogFragment
    }

    public static EditTaskDialog newInstance(String title, String oldValue, int pos) {
        EditTaskDialog frag = new EditTaskDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.position = pos;
        frag.oldValue = oldValue;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_task, container);
        mEditText = (EditText) view.findViewById(R.id.txt_edit_task);
        String title = getArguments().getString("title", "Enter Task");
        getDialog().setTitle(title);
        mEditText.setText(oldValue);
        // Show soft keyboard automatically
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mEditText.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString(), position);
            dismiss();
            return true;
        }
        return false;
    }
}