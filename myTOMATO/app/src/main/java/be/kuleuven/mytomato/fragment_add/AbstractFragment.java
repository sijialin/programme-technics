package be.kuleuven.mytomato.fragment_add;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import be.kuleuven.mytomato.R;
import be.kuleuven.mytomato.database.ToDo;

public abstract class AbstractFragment extends Fragment{
    Activity activity;
    EditText note;
    ImageView image;
    TextView name;
    GridView types;
    TextView time1,time2;
    CheckBox l1,l2,l3,l4;
    Button submit;
    LinearLayout time;
    SimpleAdapter adapter;

    ImageButton c1,c2,c3,c4,c5,c6,c7;

    int position;
    int color=0;
    ToDo todo;

    public AbstractFragment(Activity c){
        activity = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todo = new ToDo();
        todo.setName("other");
        todo.setImageID(R.drawable.item_more);
    }



    protected void initView(View v) {
        note = v.findViewById(R.id.frag_item_note);
        image = v.findViewById(R.id.frag_item_image);
        name = v.findViewById(R.id.frag_item_name);
        types = v.findViewById(R.id.frag_gv);
        time1 = v.findViewById(R.id.frag_time1);
        time2 = v.findViewById(R.id.frag_time2);
        time = v.findViewById(R.id.frag_time3);
        submit = v.findViewById(R.id.frag_submit);

        l1 = v.findViewById(R.id.frag_important);
        l2 = v.findViewById(R.id.frag_urgent);
        l3 = v.findViewById(R.id.frag_appointment);
        l4 = v.findViewById(R.id.frag_other);

        c1 = v.findViewById(R.id.frag_color_red);
        c2 = v.findViewById(R.id.frag_color_green);
        c3 = v.findViewById(R.id.frag_color_yellow);
        c4 = v.findViewById(R.id.frag_color_blue);
        c5 = v.findViewById(R.id.frag_color_darkblue);
        c6 = v.findViewById(R.id.frag_color_realpurple);
        c7 = v.findViewById(R.id.frag_color_black);

        c1.setOnClickListener(view -> {
            color=1;
            name.setTextColor(Color.parseColor("#F8F2FD"));
        });
        c2.setOnClickListener(view -> {
            color = 2;
            name.setTextColor(Color.parseColor("#6f5f90"));
        });
        c3.setOnClickListener(view -> {
            color=3;
            name.setTextColor(Color.parseColor("#d8c3e1"));
        });
        c4.setOnClickListener(view -> {
            color=4;
            name.setTextColor(Color.parseColor("#511f66"));
        });
        c5.setOnClickListener(view -> {
            color=5;
            name.setTextColor(Color.parseColor("#270849"));
        });
        c6.setOnClickListener(view -> {
            color=6;
            name.setTextColor(Color.parseColor("#210c2b"));
        });
        c7.setOnClickListener(view -> {
            color=7;
            name.setTextColor(Color.parseColor("#000000"));
        });
        time.setOnClickListener(view -> showTimeDialog());    }

    protected abstract void setTime();
    protected abstract void showTimeDialog();
    protected abstract void loadtoGV();


}
