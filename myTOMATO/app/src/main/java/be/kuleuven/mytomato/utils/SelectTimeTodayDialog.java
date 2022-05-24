package be.kuleuven.mytomato.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import java.util.Calendar;

import be.kuleuven.mytomato.R;

public class SelectTimeTodayDialog extends Dialog implements View.OnClickListener {
    TimePicker timePicker;
    Button confirm,cancel;

    public interface OnEnsureListener{
        public void OnEnsure(int hour,int minute);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeTodayDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar_time);
        timePicker = findViewById(R.id.dialog_time);
        confirm = findViewById(R.id.dialog_confirm);
        cancel = findViewById(R.id.dialog_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.dialog_cancel:
                cancel();
                break;
            case R.id.dialog_confirm:
                timePicker.setIs24HourView(true);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                onEnsureListener.OnEnsure(hour,minute);
                cancel();
                break;
        }
    }

}

