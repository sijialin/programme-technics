package be.kuleuven.mytomato;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.mytomato.adapter.itemAdapter;
import be.kuleuven.mytomato.database.ToDo;

public class SearchActivity extends AppCompatActivity {
    ListView searchItems;
    EditText searchText;
    TextView empty;
    List<ToDo> mDatas;
    itemAdapter adapter;
    ImageView start;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        mDatas = new ArrayList<>();
        adapter = new itemAdapter(this,mDatas,this.getResources());
        searchItems.setAdapter(adapter);
        searchItems.setEmptyView(empty);
        Bundle extras = getIntent().getExtras();
        String textBefore = extras.getString("input");
        searchText.setText(textBefore);
        startSearch();
    }

    private void initView() {
        searchItems = findViewById(R.id.search_list);
        searchText = findViewById(R.id.search_input);
        empty = findViewById(R.id.search_empty);
        start = findViewById(R.id.search_begin);

        user = (User)getApplication();

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_back:
                finish();
                break;
            case R.id.search_begin:
                startSearch();
        }
    }

    private void startSearch() {
        String msg = searchText.getText().toString().trim();
        if (TextUtils.isEmpty(msg)){
            Toast.makeText(this,"NO INPUT!",Toast.LENGTH_SHORT).show();
            return;
        }
        List<ToDo> searchResults = user.getRelevantToDos(msg);
        //Toast.makeText(this,i,Toast.LENGTH_SHORT).show();
        mDatas.clear();
        mDatas.addAll(searchResults);
        adapter.notifyDataSetChanged();
        searchText.setText("");
    }

    private List<ToDo> getRelevantToDos(String msg) {
        //根据提示信息进行查找
        List<ToDo> todos= new ArrayList<>();
        //todos.add(new ToDo(R.drawable.item_movie,"movie","Harry Potter",2,"12:00","today",false,false,false,false,2022,5,7,23,33));
        return todos;
    }
}