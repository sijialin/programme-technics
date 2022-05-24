package be.kuleuven.mytomato.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import be.kuleuven.mytomato.R;

public class ChangeGenderDialog extends Dialog implements View.OnClickListener{
    ImageView back;
    Button confirm,cancel;
    RadioGroup genderinput;
    RadioButton fe,ma,un;
    TextView genderinfo;

    public interface OnEnsureListener{
        public void OnEnsure(String gender);
    }

    ChangeGenderDialog.OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(ChangeGenderDialog.OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public ChangeGenderDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_user_gender);

        final int[] g = {-1};
        genderinput = findViewById(R.id.genderinput);
        /*enderinput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.gender_unknown:
                        genderinfo.setText(g[0]);
                        g[0] = 0;
                        break;
                    case R.id.gender_female:
                        genderinfo.setText("jaja1");
                        g[0] = 1;
                        break;
                    case R.id.gender_male:
                        g[0] = 2;
                        break;
                }
            }
        });*/
        confirm = findViewById(R.id.gender_confirm);
        cancel = findViewById(R.id.gender_cancel);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(view -> cancel());
        back = findViewById(R.id.gender_back);
        back.setOnClickListener(view-> {
            cancel();
        });
        genderinfo = findViewById(R.id.gender_info);
        fe = findViewById(R.id.gender_female);
        ma = findViewById(R.id.gender_male);
        un = findViewById(R.id.gender_unknown);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gender_back:
                cancel();
                break;
            case R.id.gender_confirm:
                //genderinfo.setText(Gender);
                String gender = "Unknown";
                if(fe.isChecked()) gender = "Female";
                if(ma.isChecked()) gender = "Male";
                onEnsureListener.OnEnsure(gender);
                cancel();
                break;
        }

    }
}
