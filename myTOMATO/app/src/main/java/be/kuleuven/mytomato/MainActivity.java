package be.kuleuven.mytomato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import be.kuleuven.mytomato.database.ToDo;

public class MainActivity extends AppCompatActivity {

    EditText name;
    EditText password;
    Button login;
    private RequestQueue requestQueue;
    private static final String SEARCH_USER_URL="https://studev.groept.be/api/a21pt321/search_user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.login_name);
        password = (EditText) findViewById(R.id.login_password);
        PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();
        password.setTransformationMethod(passwordTransformationMethod);
        login = (Button)findViewById(R.id.login);
        requestQueue= Volley.newRequestQueue(this);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.login:
                checklogin();
                break;
            case R.id.main_register:
                Intent intent = new Intent(this, RegisterPage.class);
                startActivity(intent);
                break;
        }
    }

    private void checklogin() {
        String myName = name.getText().toString();
        String myPassword = password.getText().toString();
        if (myName.length()==0 || myPassword.length()==0) {
            Toast.makeText(MainActivity.this, "Please input the userName and password.", Toast.LENGTH_LONG).show();
        }
        else {
            searchUser(myName);
        }
    }

    private void searchUser(String account) {
        String searchUserURL=SEARCH_USER_URL+account;
        JsonArrayRequest searchUserRequest=new JsonArrayRequest(Request.Method.GET, searchUserURL, null, response -> {
            JSONObject o;
            try {
                o = response.getJSONObject(0);
                if(o.get("userName").toString().length()!=0)
                {
                    if(o.get("password").equals(password.getText().toString())){
                        int id=o.getInt("id");
                        String userName=o.get("userName").toString();
                        String password=o.get("password").toString();
                        String registerDate=o.get("registerDate").toString();
                        String photo=o.get("photo").toString();
                        int gender=o.getInt("gender");
                        String whatsUp= o.getString("whatsUp");

                        User user=(User)getApplication();
                        user.initializing(id,userName,password,registerDate,photo,gender,whatsUp);
                        user.downloadToDoList(this);

                        Toast.makeText(MainActivity.this,"Log In!!!",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(MainActivity.this,"The password is wrong.",Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"This account doesn't exist.",Toast.LENGTH_SHORT).show();
            }
        }, error -> Toast.makeText(MainActivity.this,"Unable to communicate with the server",Toast.LENGTH_LONG).show());
        requestQueue.add(searchUserRequest);
    }

    public void onListReady(List<ToDo> toDoList) {
        Intent intent = new Intent(MainActivity.this,DisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable)toDoList);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}

