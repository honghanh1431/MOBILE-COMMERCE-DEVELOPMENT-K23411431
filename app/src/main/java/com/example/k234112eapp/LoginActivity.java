package com.example.k234112eapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {

    /*
    Declare all variables for interactive views
     **/
    EditText editUserName;
    EditText editPassword;
    TextView txtMessage;
    CheckBox chkSaveLogin;
    String name_share_pref="LoginInfo";
    RadioButton radAdmin, radEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        addViews();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        editUserName=findViewById(R.id.editUsername);
        editPassword=findViewById(R.id.editPassword);
        txtMessage=findViewById(R.id.txtMessage);
        chkSaveLogin=findViewById(R.id.chkSaveLogin);
        radAdmin=findViewById(R.id.radAdmin);
        radEmployee=findViewById(R.id.radEmployee);
    }

    public void loginSystem(View view) {
        String username=editUserName.getText().toString();
        String password=editPassword.getText().toString();
        if (username.equalsIgnoreCase("admin") && password.equals("123"))
        {
            boolean saved=chkSaveLogin.isChecked();
            SharedPreferences preferences=getSharedPreferences(name_share_pref,MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("UserName",username);
            editor.putString("Password",password);
            editor.putBoolean("Saved",saved);
            editor.commit();

            txtMessage.setText(getString(R.string.str_login_success));
            if(radAdmin.isChecked())
            {
                //dĩ nhiên ta phải kiểm tra account này có quyền admin hay ko (tính sau)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        } else {
            txtMessage.setText(getString(R.string.str_login_failed));
        }
    }

    public void exitSystem(View view) {
        //finish();
        AlertDialog.Builder builder =  new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.str_confirm_exit_title));
        builder.setMessage(getString(R.string.str_confirm_exit_message));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.str_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.str_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog=builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences=getSharedPreferences(name_share_pref,MODE_PRIVATE);
        String username=preferences.getString("UserName", "");
        String password=preferences.getString("Password", "");
        boolean saved=preferences.getBoolean("Saved",false);
        if(saved)
        {
            editUserName.setText(username);
            editPassword.setText(password);
        }
        chkSaveLogin.setChecked(saved);
    }
}

//login -> onpause hoặc login -> onpause -> onstop (che toàn bộ)(đang làm bth có cuộc gọi tới thì bị che màn hình
//hoặc
//(login) exit -> onpause -> onstop -> ondestroy (killable)
//save trong onpause
//phục hồi (restore) trong onResume