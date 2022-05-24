package be.kuleuven.mytomato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.mytomato.adapter.AddPagerAdapter;
import be.kuleuven.mytomato.database.ToDo;
import be.kuleuven.mytomato.fragment_add.RecentFragment;
import be.kuleuven.mytomato.fragment_add.TodayFragment;

public class AddActivity extends AppCompatActivity {

    TabLayout add_tablayout;
    ViewPager add_viewpager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        add_tablayout = findViewById(R.id.add_tabs);
        add_viewpager = findViewById(R.id.add_vp);

        initPager();
        user=(User)getApplication();
    }

    private void initPager() {
        //two fragments
        List<Fragment> fragmentList = new ArrayList<>();
        TodayFragment todayFrag = new TodayFragment(this);//TodayFragment(this);
        RecentFragment recentFrag = new RecentFragment(this);
        fragmentList.add(todayFrag);
        fragmentList.add(recentFrag);

        //get adapter
        AddPagerAdapter pagerAdapter = new AddPagerAdapter(getSupportFragmentManager(),fragmentList);
        add_viewpager.setAdapter(pagerAdapter);
        add_tablayout.setupWithViewPager(add_viewpager);

        todayFrag.setOnEnsureListener(todo -> CallDataBase(todo));
        recentFrag.setOnEnsureListener(todo -> CallDataBase(todo));
    }

    private void CallDataBase(ToDo todo) {
        user.addToDo(todo,this);
    }



    //go back to display page
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_back:
                finish();
                break;
        }
    }

    public void onReady() {
        finish();
    }
}