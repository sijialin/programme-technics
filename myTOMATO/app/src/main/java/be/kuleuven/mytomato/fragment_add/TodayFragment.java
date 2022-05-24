package be.kuleuven.mytomato.fragment_add;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.mytomato.DisplayActivity;
import be.kuleuven.mytomato.R;
import be.kuleuven.mytomato.database.ToDo;
import be.kuleuven.mytomato.utils.SelectTimeDialog;
import be.kuleuven.mytomato.utils.SelectTimeTodayDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends AbstractFragment{

    int h,m;

    public interface OnEnsureListener{
        public void OnEnsure(ToDo t);
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    private int[] icons = {
            R.drawable.item_calling,R.drawable.item_doctor,R.drawable.item_gift,R.drawable.item_loth,
            R.drawable.item_mail, R.drawable.item_playball,R.drawable.item_shopping,R.drawable.item_weather,
            R.drawable.item_movie,R.drawable.item_outdoor,R.drawable.item_schedule,R.drawable.item_more
    };
    private String[] texts = {
            "calling","doctor","gift","cloth","mail","play","shop","weather","movie","outdoor","schedule","other"
    };


    public TodayFragment(Activity c) {
        super(c);
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_today,container,false);
        initView(v);
        setTime();
        loadtoGV();
        time1.setText(null);
        return v;
    }

    @Override
    protected void setTime() {
        Date date = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("MM/dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        //time1.setText(sdf1.format(date));
        //time2.setText(sdf2.format(date));

        todo.setTime(sdf2.format(date));
        todo.setDate(sdf1.format(date));
    }

    @Override
    protected void initView(View v) {
        super.initView(v);
        submit.setOnClickListener(view -> {
            int todoImage = icons[position];
            String todoName = name.getText().toString().trim();
            String todoNote = note.getText().toString().trim();

            String todoDate = time1.getText().toString();
            String todoTime = time2.getText().toString();
            boolean todoL1 = l1.isChecked();
            boolean todoL2 = l2.isChecked();
            boolean todoL3 = l3.isChecked();
            boolean todoL4 = l4.isChecked();
            ToDo todo = new ToDo(todoImage,todoName,todoNote,color,todoDate,todoTime,todoL1,todoL2,todoL3,todoL4,0,0,0,h,m,0,0,0);
            onEnsureListener.OnEnsure(todo);
        });

    }

    @Override
    protected void showTimeDialog() {
        SelectTimeTodayDialog SelectTime = new SelectTimeTodayDialog(getContext());
        SelectTime.show();
        SelectTime.setOnEnsureListener(new SelectTimeTodayDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(int hour, int minute) {
                //写入数据库
                h = hour;
                m = minute;
                String time = hour+":"+minute;
                time2.setText(time);
            }
        });
    }

    @Override
    protected void loadtoGV() {
        List<Map<String,Object>> listItems = new ArrayList<>();
        for(int i=0;i < icons.length;i++){
            Map<String,Object> listitem = new HashMap<>();
            listitem.put("icon",icons[i]);
            listitem.put("name",texts[i]);
            listItems.add(listitem);
        }
        adapter = new SimpleAdapter(activity, listItems,R.layout.add_item,new String[]{"icon","name"},
                new int[]{R.id.add_item_image,R.id.add_item_name});
        types.setAdapter(adapter);
        types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                image.setImageResource(icons[i]);
                todo.setImageID(icons[i]);
                name.setText(texts[i]);
                todo.setName(texts[i]);
            }
        });
    }
}