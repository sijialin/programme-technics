package be.kuleuven.mytomato.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import be.kuleuven.mytomato.R;
import be.kuleuven.mytomato.UserActivity;

public class UserIntoDialog extends Dialog implements View.OnClickListener {
    private EditText input;
    private ImageView back;
    private Button confirm,cancel;
    private Context c;
    private RequestQueue requestQueue;
    private static final String SEARCH_USER_URL="https://studev.groept.be/api/a21pt321/search_user/";

    public interface OnEnsureListener{
        public void OnEnsure(String input);
    }

    UserIntoDialog.OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(UserIntoDialog.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public UserIntoDialog(@NonNull Context context) {
        super(context);
        c=context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_cancel:
                cancel();
                break;
            case R.id.dialog3_back:
                cancel();
                break;
            case R.id.user_confirm:
                String text = input.getText().toString().trim();
                //no input, no operation
                if(text.length()==0) {
                    return;
                }
                //search if the input user name has already been taken, if so, show prompt, else update new user name
                requestQueue= Volley.newRequestQueue(c);
                String searchUserURL=SEARCH_USER_URL+text;
                JsonArrayRequest searchUserRequest=new JsonArrayRequest(Request.Method.GET, searchUserURL, null, response -> {
                    try {
                        JSONObject o = response.getJSONObject(0);
                        if(o.get("userName").toString().length()!=0)
                        {
                        Toast.makeText(c,"This user name has been taken",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onEnsureListener.OnEnsure(text);
                        cancel();
                }
                }, error -> Toast.makeText(c,"Unable to communicate with the server",Toast.LENGTH_LONG).show());
               requestQueue.add(searchUserRequest);
               break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user);
        back = findViewById(R.id.dialog3_back);
        input = findViewById(R.id.user_input);
        confirm = findViewById(R.id.user_confirm);
        cancel = findViewById(R.id.user_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
}
