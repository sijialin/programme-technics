package be.kuleuven.mytomato.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import be.kuleuven.mytomato.R;

public class ChangeWhatsupDialog extends Dialog implements View.OnClickListener {
    EditText input;
    ImageView back;
    Button confirm,cancel;
    String oldWhatsup;

    public interface OnEnsureListener{
        public void OnEnsure(String input);
    }

    ChangeWhatsupDialog.OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(ChangeWhatsupDialog.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public ChangeWhatsupDialog(@NonNull Context context, String whatsup) {
        super(context);
        whatsup = oldWhatsup;
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
                if(text.length()==0) {
                    return;
                }
                onEnsureListener.OnEnsure(text);
                cancel();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_whatsup);
        back = findViewById(R.id.dialog3_back);
        input = findViewById(R.id.user_input);
        input.setHint(oldWhatsup);
        confirm = findViewById(R.id.user_confirm);
        cancel = findViewById(R.id.user_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(view->dismiss());
        back.setOnClickListener(view->dismiss());
    }
}
