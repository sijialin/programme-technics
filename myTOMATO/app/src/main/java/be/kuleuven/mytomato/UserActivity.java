package be.kuleuven.mytomato;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.mytomato.utils.ChangeGenderDialog;
import be.kuleuven.mytomato.utils.ChangePasswordDialog;
import be.kuleuven.mytomato.utils.ChangeWhatsupDialog;
import be.kuleuven.mytomato.utils.UserIntoDialog;

public class UserActivity extends AppCompatActivity {
    private static int REQUEST_CODE_FROM_ALBUM=100;
    private static final int CODE_RESULT_REQUEST =102;
    private int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    private ImageView back, name, password, gender, help,userPhoto,whatsup,editImage;
    TextView logout, myname, mypassword, mygender,displayName,displayWhatsup,ID,registerDate;

    private Uri photoUri;
    private User user;
    private RequestQueue requestQueue;
    private static final String SEARCH_USER_URL="https://studev.groept.be/api/a21pt321/search_user/";

    String[] genders = {"Unknown","Female","Male"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();

        user=(User)getApplication();
        ID.setText(Integer.toString(user.getUserID()));
        myname.setText(user.getUserName());
        displayName.setText(user.getUserName());
        mygender.setText(genders[user.getGender()]);
        String whatsupDisplay = user.getWhats_up();
        if(whatsupDisplay.isEmpty()||whatsupDisplay.equals("null")) whatsupDisplay = "Be better everyday!";
        displayWhatsup.setText(whatsupDisplay);
        registerDate.setText(user.getRegisterDate());

        if(user.getPhoto()!=""&&user.getPhoto()!=null&&user.getPhoto().length()>100){
        byte[] imageBytes = Base64.decode( user.getPhoto(), Base64.DEFAULT );
        Bitmap bitmap2 = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        userPhoto.setImageBitmap( bitmap2 );}
    }


    private void initView() {
        back = findViewById(R.id.user_back);
        name = findViewById(R.id.user_intoName);
        password = findViewById(R.id.user_intoPassword);
        gender = findViewById(R.id.user_intoGender);
        help = findViewById(R.id.user_intoQ);
        logout = findViewById(R.id.user_logout);
        myname = findViewById(R.id.user_name);
        mypassword = findViewById(R.id.user_password);
        mygender = findViewById(R.id.user_gender);
        displayName = findViewById(R.id.displayName);
        displayWhatsup = findViewById(R.id.displayWhatsup);
        userPhoto = findViewById(R.id.user_photo);
        whatsup = findViewById(R.id.user_intoWhatsup);
        ID = findViewById(R.id.user_ID);
        registerDate = findViewById(R.id.user_registerDate);
    }
    private void choosePhoto(){
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
           userPhoto.setImageBitmap(cutted);
           user.setImage(cutted);
        }


    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_photo:
                choosePhoto();
                break;
            case R.id.user_back:
                finish();
                break;
            case R.id.user_intoName:
                showUserDialog();
                break;
            case R.id.user_intoPassword:
                showPasswordDialog();
                break;
            case R.id.user_intoGender:
                showGenderDialog();
                break;
            case R.id.user_intoQ:
                showHelp();
                break;
            case R.id.user_intoWhatsup:
                showWhatsupDialog();
                break;
            case R.id.user_logout:
                user.initializing(0,null,null,null,"",0,null);
                Intent it1 = new Intent(this, MainActivity.class);
                startActivity(it1);
                break;
        }
    }

    private void showHelp() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(UserActivity.this);

        alertDialog.setTitle("HELP");

        alertDialog.setMessage("APP authors : Wenjie Zhu,Sijia Lin\nVersion : 1.0\n(only for educational use)");

        alertDialog.setPositiveButton("back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(UserActivity.this,"Wish you have fun!",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog=alertDialog.create();
        dialog.show();
    }

    private void showWhatsupDialog() {
        ChangeWhatsupDialog dialog = new ChangeWhatsupDialog(this,user.getWhats_up());
        dialog.show();
        dialog.setOnEnsureListener(input -> {
            displayWhatsup.setText(input);
            CallDataBase(input,2);
        });
    }

    private void showGenderDialog() {
        ChangeGenderDialog dialog = new ChangeGenderDialog(this);
        dialog.show();
        dialog.setOnEnsureListener(gender -> {
            int newg = 0;
            if(gender.equals("Female")) newg = 1;
            if(gender.equals("Male")) newg = 2;
            mygender.setText(genders[newg]);
            CallDataBaseGender(newg);
        });
    }

    private void CallDataBaseGender(int newg) {
        user.setGender(newg,this);
    }

    private void showPasswordDialog() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(this,user.getPassword());
        dialog.show();
        dialog.setOnEnsureListener(new ChangePasswordDialog.OnEnsureListener() {
            @Override
            public void OnEnsure(String newpw) {
                CallDataBase(newpw,1);
                //在数据库中改变用户密码
            }
        });
    }

    private void showUserDialog() {
        UserIntoDialog dialog = new UserIntoDialog(this);
        dialog.show();
        dialog.setOnEnsureListener(input -> {
            //更新数据库
            CallDataBase(input,0);

        });
    }

    private void CallDataBase(String input,int type) {
        if(type==0) user.setUserName(input,this);
        if(type==1) user.setPassword(input,this);
        if(type==2) user.setWhats_up(input,this);
    }

    public void onReadyName(String n) {
        myname.setText(n);
        displayName.setText(n);
    }

    public void onReady(){

    }

    public void onReadyGender(int newGender) {
    }
}
