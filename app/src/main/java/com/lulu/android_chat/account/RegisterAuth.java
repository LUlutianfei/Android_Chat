package com.lulu.android_chat.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lulu.android_chat.BaseActivity;
import com.lulu.android_chat.R;

import java.util.Random;

public class RegisterAuth extends BaseActivity  {


    private Button next;
    private EditText auth;
    private String verification;
    private String mail;
    private ImageView prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_register_auth);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    void init(){
        Intent intent = getIntent();
        verification = intent.getStringExtra("auth");
        mail = intent.getStringExtra("email");
        Log.d("mail", mail);
        next = findViewById(R.id.auth_next);
        auth = findViewById(R.id.register_auth);
        prev = findViewById(R.id.auth_prev);

    }

    private void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(auth.getText())) {
                    if(verification.equals(auth.getText().toString())) {
                        Intent intent = new Intent(RegisterAuth.this,
                                SettingAccount.class);
                        intent.putExtra("email",mail);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(RegisterAuth.this, "验证码有误，请重新输入",
                                Toast.LENGTH_LONG).show();
                    }
                }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        auth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(auth.getText()))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
