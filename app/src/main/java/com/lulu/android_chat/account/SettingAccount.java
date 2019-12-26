package com.lulu.android_chat.account;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lulu.android_chat.ActivityCollector;
import com.lulu.android_chat.BaseActivity;
import com.lulu.android_chat.MainActivity;
import com.lulu.android_chat.R;

import java.io.IOException;

public class SettingAccount extends BaseActivity  {

    private EditText username;
    private EditText password;
    private EditText password_repeat;
    private Button next;
    private String mail;
    private ImageView prev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_account);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    private void init() {
        WebView webView = findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.137.1:3000");
        mail = getIntent().getStringExtra("email");
        username = findViewById(R.id.settingAccount_username);
        password = findViewById(R.id.settingAccount_password);
        password_repeat = findViewById(R.id.settingAccount_password_repeat);
        next = findViewById(R.id.settingAccount_next);
        prev = findViewById(R.id.account_prev);
    }

    private void event() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!TextUtils.isEmpty(username.getText()))&&
                        (!TextUtils.isEmpty(password.getText()))&&
                        (!TextUtils.isEmpty(password_repeat.getText()))&&
                        (String.valueOf(password.getText())
                                .equals(String.valueOf(password_repeat.getText())))){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder url = new StringBuilder("http://10.102.176.1:3000");
                            url.append("/account");
                            url.append("?");
                            url.append("username="+username.getText());
                            url.append("&");
                            url.append("password="+password.getText());
                            url.append("&");
                            url.append("mail="+mail);
                            try {
                                String res = OkHttp.get(String.valueOf(url));
                                Log.d("user1",res);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    finishAll(SettingAccount.this);
                }
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((!TextUtils.isEmpty(username.getText()))&&
                        (!TextUtils.isEmpty(password.getText()))&&
                        (!TextUtils.isEmpty(password_repeat.getText())))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password_repeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((!TextUtils.isEmpty(username.getText()))&&
                        (!TextUtils.isEmpty(password.getText()))&&
                        (!TextUtils.isEmpty(password_repeat.getText())))
                    next.setTextColor(Color.parseColor("#ffffff"));
                else
                    next.setTextColor(Color.parseColor("#8a8a8a"));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password_repeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((!TextUtils.isEmpty(username.getText()))&&
                        (!TextUtils.isEmpty(password.getText()))&&
                        (!TextUtils.isEmpty(password_repeat.getText()))){
                    next.setTextColor(Color.parseColor("#ffffff"));
                }
                else{
                    next.setTextColor(Color.parseColor("#8a8a8a"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
