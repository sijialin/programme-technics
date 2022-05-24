package be.kuleuven.mytomato.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;
import java.util.zip.DataFormatException;

import be.kuleuven.mytomato.R;

public class SelectTimeDialog extends Dialog implements View.OnClickListener {
    EditText hour;
    EditText minute;
    DatePicker datePicker;
    Button confirm,cancel;

    public interface OnEnsureListener{
        public void OnEnsure(String time1,String ddlTime,int year,int month,int day,int hour,int minute);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);
        hour = findViewById(R.id.dialog_hour);
        minute = findViewById(R.id.dialog_minute);
        datePicker = findViewById(R.id.dialog_date);
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
                String hourstr = hour.getText().toString();
                String minutestr = minute.getText().toString();
                if(hourstr.isEmpty()||minutestr.isEmpty()){
                    showErrorMessage();
                }
                else{
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth()+1;
                    int day = datePicker.getDayOfMonth();
                    String monthstr = String.valueOf(month);
                    if(month<10) monthstr="0"+monthstr;
                    String daystr = String.valueOf(day);
                    if(day<10) daystr = "0"+daystr;
                    int h=0;
                    if(!TextUtils.isEmpty(hourstr)){h = Integer.parseInt(hourstr); }
                    int m=0;
                    if(!TextUtils.isEmpty(minutestr)){ m = Integer.parseInt(minutestr); }
                    if(h>23||h<0||m<0||m>59){
                        showErrorMessage();
                        }
                    else{
                        if(hourstr.length()==1) hourstr ="0"+hourstr;
                        if(minutestr.length()==1) minutestr ="0"+minutestr;
                        String ddlTime = hourstr+":"+minutestr;
                        Calendar calendar = Calendar.getInstance();
                        int todayYear = calendar.get(Calendar.YEAR);
                        int today = calendar.get(Calendar.DAY_OF_MONTH);
                        String date = monthstr+"."+daystr;
                        if(!(year==todayYear)){
                            date = year+"."+date;
                        }
                        if(today==day){
                            date = "today";
                        }
                        onEnsureListener.OnEnsure(date,ddlTime,year,month,day,h,m);
                        }
                }
                cancel();
                break;
        }
    }

    private void showErrorMessage() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("warning")
                .setIcon(R.drawable.caution)
                .setMessage("No correct time input!")
                .create();
        dialog.show();
    }

}
