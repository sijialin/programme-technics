package be.kuleuven.mytomato.fragment_add;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.mytomato.R;
import be.kuleuven.mytomato.User;
import be.kuleuven.mytomato.database.ToDo;
import be.kuleuven.mytomato.utils.SelectTimeDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentFragment#} factory method to
 * create an instance of this fragment.
 */
public class RecentFragment extends AbstractFragment {

    EditText times;

    int ye,mo,da,h,m;

    public interface OnEnsureListener{
        public void OnEnsure(ToDo todo);
    }

    RecentFragment.OnEnsureListener onEnsureListener;


    public void setOnEnsureListener(RecentFragment.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    private int[] icons = {
            R.drawable.item_beach,R.drawable.item_drink,R.drawable.item_learning,R.drawable.item_listen,
            R.drawable.item_piano, R.drawable.item_read,R.drawable.item_record,R.drawable.item_sleep,
            R.drawable.item_sport,
            R.drawable.item_more

    };
    private String[] texts = {
            "vocation","drink","learn","radio","piano","read","record","sleep","sport","other"
    };


    public RecentFragment(Activity c) {
        super(c);
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_habit,container,false);
        initView(v);
        setTime();
        loadtoGV();
        return v;
    }

    @Override
    protected void setTime() {
        Date date = new Date();
        SimpleDateFormat sdf0 = new SimpleDateFormat("MM.dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");
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
            //这里增加数据库记录
            int todoImage = icons[position];
            String todoName = name.getText().toString().trim();
            String todoNote = note.getText().toString().trim();

            String todoDate = time1.getText().toString();
            String todoTime = time2.getText().toString();

            boolean todoL1 = l1.isChecked();
            boolean todoL2 = l2.isChecked();
            boolean todoL3 = l3.isChecked();
            boolean todoL4 = l4.isChecked();
            int todoTimes = Integer.parseInt(times.getText().toString());
            ToDo todo = new ToDo(todoImage,todoName,todoNote,color,todoTime,todoDate,todoL1,todoL2,todoL3,todoL4,ye,mo,da,h,m,0,todoTimes,0);

            onEnsureListener.OnEnsure(todo);
        });

    }

    @Override
    protected void showTimeDialog() {
        SelectTimeDialog SelectTime = new SelectTimeDialog(getContext());
        SelectTime.show();
        SelectTime.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(String date,String ddlTime, int year, int month,int day, int hour, int minute) {
                ye = year;
                mo = month;
                da = day;
                h = hour;
                m = minute;
                time1.setText(date);
                time2.setText(ddlTime);
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