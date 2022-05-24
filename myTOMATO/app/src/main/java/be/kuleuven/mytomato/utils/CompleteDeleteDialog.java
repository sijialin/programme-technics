package be.kuleuven.mytomato.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import be.kuleuven.mytomato.R;

public class CompleteDeleteDialog extends Dialog implements View.OnClickListener{
    Button finished;
    TextView edit,delete;
    ImageView back;
    int index;
    public CompleteDeleteDialog(@NonNull Context context,int index) {
        super(context);
        this.index = index;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_complete_delete);
        finished = (Button)findViewById(R.id.dialog2_finished);
        edit = (TextView) findViewById(R.id.dialog2_edit);
        delete = (TextView)findViewById(R.id.dialog2_delete);
        back = findViewById(R.id.dialog2_back);

        back.setOnClickListener(this);
        edit.setOnClickListener(this);
        finished.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog2_back:
                cancel();
                break;
            case R.id.dialog2_finished:
                itemFinished();
                break;
            case R.id.dialog2_edit:
                itemEdit();
                break;
            case R.id.dialog2_delete:
                itemDelete();
                break;
        }
    }

    private void itemFinished() {
        onEnsureListener.OnEnsure(0);
        cancel();
    }

    private void itemEdit() {
        SelectTimeDialog SelectTime = new SelectTimeDialog(getContext());
        SelectTime.show();
        SelectTime.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(String date,String ddlTime, int year, int month, int day,int hour, int minute) {
                onEnsureListener.OnEnsure(date,ddlTime,year,month,day,hour,minute);
                //这里写入数据库
            }
        });
        cancel();
    }

    private void itemDelete() {
        onEnsureListener.OnEnsure(1);
        cancel();
    }

    public interface OnEnsureListener{
        public void OnEnsure(String time1,String ddlTime,int year,int month,int day,int hour,int minute);
        public void OnEnsure(int type);
    }
    OnEnsureListener onEnsureListener;
    public void setOnEnsureListener(CompleteDeleteDialog.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }
}
