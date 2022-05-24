package be.kuleuven.mytomato;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import be.kuleuven.mytomato.database.ToDo;
import be.kuleuven.mytomato.adapter.itemAdapter;
import be.kuleuven.mytomato.utils.CompleteDeleteDialog;

public class DisplayActivity extends AppCompatActivity{

    ListView itemlv;
    List<ToDo> mItems;
    itemAdapter adapter;
    User me;

    int day,month,week;
    TextView topTime;
    TextView todaySummary,recentSummary;
    ImageView search,user;
    EditText inputText;

    int today;
    int recent;

    String[] weekStrs = {"Mon","Mon","Tues","Wedn","Thurs","Fri","Sat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        me=(User)getApplication();
        initHeaderView();

        Intent intent = this.getIntent();
        Bundle extras = getIntent().getExtras();
        //mItems = (List<ToDo>)extras.get("list");

        mItems = me.getToDoList();

        itemlv = findViewById(R.id.main_list1);
        Resources r = getResources();
        adapter = new itemAdapter(this,mItems,r);
        itemlv.setAdapter(adapter);

        itemlv.setOnItemClickListener((adapterView, view, i, l) -> {
            String name = (mItems.get(i).getName());
            itemClicked(i,name);
        });
    }



    private void itemClicked(int index,String name){
        CompleteDeleteDialog dialog = new CompleteDeleteDialog(this,index);
        dialog.show();
        dialog.setOnEnsureListener(new CompleteDeleteDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(String time1, String ddlTime, int year, int month, int day,int hour, int minute) {
                //这里改变位置i对应的时间
                mItems.get(index).setDate(time1);
                mItems.get(index).setTime(ddlTime);
                mItems.get(index).setYear(year);
                mItems.get(index).setMonth(month);
                mItems.get(index).setDay(day);
                mItems.get(index).setHour(hour);
                mItems.get(index).setMinute(minute);
                adapter.notifyDataSetChanged();
                CallDataBase(index,time1,ddlTime,year,month,day,hour,minute);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void OnEnsure(int type) {
                if(type==0){
                    //itemfinished! 数据库
                    if(mItems.get(index).getTimesToDo()==0||mItems.get(index).getTimesToDo()==1||
                            mItems.get(index).getTimesToDo()==(mItems.get(index).getDoneTimes()+1)){
                        CallDataBase(index,1);//me.setToDoDone(index);
                         }
                    else{
                        CallDataBase(index,2);
                    }
                }
                else{
                    callDelete(index);
                }
                mItems = me.getToDoList();
                adapter.notifyDataSetChanged();
                initSummary();
            }
        });
        /*AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(name.toUpperCase(Locale.ROOT))
                .setMessage("Are you finished?")
                .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //需要在数据库里删除
                        mItems.remove(index);
                        loadData();
                        dialogInterface.dismiss();
                    }
                })
                .create();
        dialog.show();*/
    }

    private void CallDataBase(int index,int type) {
        if (type==1) {
            me.setToDoDone(index,this);
        }
        if(type==2){
            me.setDoneOne(index,this);
        }
    }

    private void CallDataBase(int index,String time,String ddl,int year,int month,int day,int hour,int minute){
        me.editTime(index,time,ddl,year,month,day,hour,minute,this);
    }

    private void callDelete(int index) {
        me.deleteToDo(index,DisplayActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mItems = me.getToDoList();
        adapter.notifyDataSetChanged();
        initSummary();
        //mHandler.postDelayed(mUpdateTimeTask,2000);
        //loadData();
    }

    /*private void loadData() {
        //这里调取数据库today and recent list
        List<ToDo> list = new ArrayList<>();
        mItems = me.getToDoList();
        adapter.notifyDataSetChanged();
        //also get the number of completed items
        //initSummary();
    }*/

    private String calculateToday() {
        today=0;
        for(ToDo t:mItems){
            if(t.getTimesToDo()==0) {
                today++;
            }
        }
        String todayDisplay="today : ";
        //在数据库里找到今日完成情况，表示为”.../...”
        todayDisplay = todayDisplay+String.valueOf(today);
        return todayDisplay;
    }

    private String calculateRecent() {
        recent=0;
        for(ToDo t:mItems){
            if(t.getTimesToDo()!=0) {
                recent++;
            }
        }
        String todayDisplay="recent : ";
        //在数据库里找到今日完成情况，表示为”.../...”
        todayDisplay = todayDisplay+String.valueOf(recent);
        return todayDisplay;
    }


    private void initHeaderView() {
        topTime = findViewById(R.id.top_time);
        todaySummary = findViewById(R.id.today_summary);
        recentSummary = findViewById(R.id.recent_summary);
        search = findViewById(R.id.main_search_btn);
        inputText = findViewById(R.id.main_input);
        user = findViewById(R.id.main_user);

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        week = calendar.get(Calendar.DAY_OF_WEEK);
        String weekstr = weekStrs[week-1];
        String today = weekstr+" ";
        if(month<10) today+="0";
        today=today+String.valueOf(month)+"/";
        if(day<10) today+="0";
        today+=String.valueOf(day);

        topTime.setText(today);
    }

    private void initSummary() {
        todaySummary.setText(calculateToday());
        recentSummary.setText(calculateRecent());
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_user:
                Intent it_user = new Intent(this,UserActivity.class);
                startActivity(it_user);
                break;
            case R.id.main_addItem:
                Intent it1 = new Intent(this,AddActivity.class);
                startActivity(it1);
                break;
            case R.id.main_search_btn:
                Intent it = new Intent(this,SearchActivity.class);
                String text = inputText.getText().toString().trim();
                it.putExtra("input",text);
                inputText.setText("");
                startActivity(it);
                break;

        }
    }


    public void onReady(List<ToDo> toDoList) {
        mItems = toDoList;
        adapter.notifyDataSetChanged();
    }
}