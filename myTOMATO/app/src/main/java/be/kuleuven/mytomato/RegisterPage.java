package be.kuleuven.mytomato;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegisterPage extends AppCompatActivity {
    private EditText userNameInput;
    private EditText passwordInput;
    private RadioGroup genderInput;
    private EditText whatsUpInput;

    private static int REQUEST_CODE_FROM_ALBUM=100;
    private static final int CODE_RESULT_REQUEST =102;
    private int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    String imageString;
    private static final String SEARCH_USER_URL="https://studev.groept.be/api/a21pt321/search_user/";
    private static final String INSERT_USER_URL="https://studev.groept.be/api/a21pt321/insert_user/";
    private static final String UPDATE_PHOTO_BY_NAME_URL="https://studev.groept.be/api/a21pt321/update_photo_by_username/";
    private RequestQueue requestQueue;

    public int Gender=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        userNameInput=findViewById(R.id.userNameInput);
        passwordInput=findViewById(R.id.passwordInput);
        genderInput=findViewById(R.id.genderInput);
        whatsUpInput=findViewById(R.id.whatsUpInput);

        requestQueue= Volley.newRequestQueue(this);

        genderInput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.gender_unknown:
                        Gender = 0;
                        break;
                    case R.id.gender_female:
                        Gender = 1;
                        break;
                    case R.id.gender_male:
                        Gender = 2;
                        break;
                }
            }
        });
    }

    public void confirmRegister(View caller){
        if(userNameInput.getText().toString().length()!=0)
            checkDuplicity();
        else
            Toast.makeText(RegisterPage.this,"Please input the user name",Toast.LENGTH_LONG).show();
    }
    public void checkDuplicity(){
        String searchUserURL=SEARCH_USER_URL+userNameInput.getText().toString();
        JsonArrayRequest searchUserRequest=new JsonArrayRequest(Request.Method.GET, searchUserURL, null, response -> {
            try {
                JSONObject o = response.getJSONObject(0);
                if(o.get("userName").toString().length()!=0)
                {
                    Toast.makeText(RegisterPage.this,"This user name has been taken",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                checkInput();
            }
        }, error -> Toast.makeText(RegisterPage.this,"Unable to communicate with the server",Toast.LENGTH_LONG).show());
        requestQueue.add(searchUserRequest);
    }
    public void checkInput(){
        boolean alright=false;
        if(passwordInput.getText().length()==0)
            Toast.makeText(RegisterPage.this,"Please input the password",Toast.LENGTH_LONG).show();
        else{
                alright=true;
        }
        if(alright)
            createNewUser();
    }
    public void createNewUser(){
       String insertUserURL=INSERT_USER_URL;
        insertUserURL+=userNameInput.getText().toString()+"/";
        insertUserURL+=passwordInput.getText().toString()+"/";
        Calendar calendar= Calendar.getInstance();
        String year=Integer.toString(calendar.get(Calendar.YEAR));
        String month=Integer.toString(calendar.get(Calendar.MONTH)+1);
        String day=Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String registerDate=day+"-"+month+"-"+year;
        insertUserURL+=registerDate+"/";
        insertUserURL+="1"+"/";//photoInput.getText().toString()+"/";
        insertUserURL+=Gender+"/";
        insertUserURL+=whatsUpInput.getText().toString();

        StringRequest registerRequest=new StringRequest(Request.Method.GET, insertUserURL, response -> {
            Toast.makeText(RegisterPage.this,"new account registered", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(RegisterPage.this,MainActivity.class);
            startActivity(intent);
        }, error -> Toast.makeText(RegisterPage.this,"Error",Toast.LENGTH_LONG).show());
        requestQueue.add(registerRequest);
    }

    public void backToMain(View caller){
        Intent intent=new Intent(RegisterPage.this,MainActivity.class);
        startActivity(intent);
    }

    public void onClick(View view) {
        //choosePhoto();
    }
    /*private void choosePhoto(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            psImage(photoUri);
        }
        if(requestCode==CODE_RESULT_REQUEST){
            setImageToImageView(data);
        }
    }


    public void psImage(Uri imageUri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }
    public void setImageToImageView(Intent intent){
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap cutted = extras.getParcelable("data");
            photoInput.setImageBitmap(cutted);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            cutted.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }


    }*/
}
