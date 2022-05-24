package be.kuleuven.mytomato.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import be.kuleuven.mytomato.R;

public class ChangePasswordDialog extends Dialog implements View.OnClickListener {
    Context c;
    ImageView back,showPW1,showPW2,showPW3;
    EditText old,new1,new2;
    Button confirm,cancel;
    String oldpassword;

    public interface OnEnsureListener{
        public void OnEnsure(String newpw);
    }

    ChangePasswordDialog.OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(ChangePasswordDialog.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public ChangePasswordDialog(@NonNull Context context,String oldpassword) {
        super(context);
        setContentView(R.layout.dialog_user_pw);
        c = context;
        this.oldpassword = oldpassword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = findViewById(R.id.password_back);
        old = findViewById(R.id.pasword_current);
        new1 = findViewById(R.id.password_new);
        new2 = findViewById(R.id.password_new2);
        PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();
        old.setTransformationMethod(passwordTransformationMethod);
        new1.setTransformationMethod(passwordTransformationMethod);
        new2.setTransformationMethod(passwordTransformationMethod);
        showPW1 = findViewById(R.id.showPW1);
        showPW2 = findViewById(R.id.showPW2);
        showPW3 = findViewById(R.id.showPW3);
        confirm = findViewById(R.id.password_confirm);
        cancel = findViewById(R.id.password_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(view->dismiss());
        back.setOnClickListener(view->dismiss());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.password_confirm:
                String oldpw,newpw1,newpw2;
                oldpw = old.getText().toString().trim();
                newpw1 = new1.getText().toString().trim();
                newpw2 = new2.getText().toString().trim();
                if(newpw1.equals(newpw2)){
                    //这里需要验证是否旧密码是对的
                    if(oldpassword.equals(oldpw)){
                        onEnsureListener.OnEnsure(newpw1);
                        dismiss();}
                    else{
                        Toast.makeText(c,"incorrect old password!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(c,"unmatched new password!",Toast.LENGTH_SHORT).show();
                    new1.setText("");
                    new2.setText("");
                }
                break;
            case R.id.showPW1:
                showPW1.setImageResource(R.drawable.open_eye);
                Toggle(0);
                break;
            case R.id.showPW2:
                Toggle(1);
                break;
            case R.id.showPW3:
                Toggle(2);
                break;
        }

    }

    boolean[] seen={false,false,false};
    ImageView[] images = {showPW1,showPW2,showPW3};
    TextView[] texts = {old,new1,new2};
    private void Toggle(int i) {
        images[i].setImageResource(R.drawable.open_eye);
        if(seen[i]){
            PasswordTransformationMethod passwordTransformationMethod = PasswordTransformationMethod.getInstance();
            texts[i].setTransformationMethod(passwordTransformationMethod);
            images[i].setImageResource(R.drawable.close_eye);
            seen[i]=false;
        }
        else{
            HideReturnsTransformationMethod hideReturnsTransformationMethod = HideReturnsTransformationMethod.getInstance();
            texts[i].setTransformationMethod(hideReturnsTransformationMethod);
            images[i].setImageResource(R.drawable.open_eye);
            seen[i]=true;
        }
    }


}
