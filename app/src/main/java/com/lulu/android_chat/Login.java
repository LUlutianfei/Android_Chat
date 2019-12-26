package com.lulu.android_chat;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lulu.android_chat.account.OkHttp;
import com.lulu.android_chat.account.RegisterAuth;
import com.lulu.android_chat.account.RegisterMail;
import com.lulu.android_chat.account.SettingAccount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.NonNull;

public class Login extends BaseActivity {

    public static final int UPDATE_TEXT =1;
    @SuppressLint("HandlerLeak")
    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case UPDATE_TEXT:
                    Toast.makeText(Login.this,"账号或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    break;
                    default:break;
            }
        }
    };
    private EditText username;
    private EditText password;
    private ImageButton login;
    private ImageButton showHide;
    private TextView register;
    private CheckBox rememberPassword;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        event();
    }

    void init()
    {
        preferences = this.getSharedPreferences("online", Context.MODE_MULTI_PROCESS);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        showHide = findViewById(R.id.showHide);
        register = findViewById(R.id.register);
        rememberPassword = findViewById(R.id.remember_password);
        username.setText(preferences.getString("username",""));
        boolean isRemember = preferences.getBoolean("remember_password",false);
        if(isRemember){
            password.setText(preferences.getString("password",""));
            rememberPassword.setChecked(true);
        }
        Spannable spanText = username.getText();
        if (spanText != null) {
            Selection.setSelection(spanText, spanText.length());
        }
        if((!TextUtils.isEmpty(password.getText()))&&(!TextUtils.isEmpty(username.getText())))
            login.setImageResource(R.drawable.login_active);
        else
            login.setImageResource(R.drawable.login);
    }

    private void event() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameAndPassword();
            }
        });
        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getInputType()== (InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT)){
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showHide.setImageResource(R.drawable.show);
                }else{
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
                    showHide.setImageResource(R.drawable.hide);
                }
                // 保证切换后光标位于文本末尾
                Spannable spanText = password.getText();
                if (spanText != null) {
                    Selection.setSelection(spanText, spanText.length());
                }
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if((!TextUtils.isEmpty(password.getText()))&&(!TextUtils.isEmpty(username.getText())))
                    login.setImageResource(R.drawable.login_active);
                else
                    login.setImageResource(R.drawable.login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if((!TextUtils.isEmpty(password.getText()))&&(!TextUtils.isEmpty(username.getText())))
                    login.setImageResource(R.drawable.login_active);
                else
                    login.setImageResource(R.drawable.login);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, RegisterMail.class));
            }
        });

    }

    private void usernameAndPassword(){
        final String user = String.valueOf(username.getText());
        final String pass = String.valueOf(password.getText());

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject msg = new JSONObject();
                String result;
                try {
                    msg.put("username",user);
                    StringBuilder url = new StringBuilder("http://10.102.185.152:3000");
                    url.append("/account");
                    Log.d("user1", url.toString());
                    result = OkHttp.post(url.toString(),msg.toString());
                    JSONObject res = new JSONObject(result);
                    JSONArray resArry = res.getJSONArray("msg");
                    String p;
                    if(resArry.length()>0) {
                        JSONObject out = (JSONObject) res.getJSONArray("msg").get(0);
                        p = (String) out.get("password");
                        //Log.d("user1", p);
                        if(p.equals(pass)){
                            editor = preferences.edit();
                            if(rememberPassword.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("password",pass);
                            }else
                                editor.clear();
                            editor.putString("username",user);
                            editor.putBoolean("online",true);
                            editor.apply();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Message message = new Message();
                            message.what = UPDATE_TEXT;
                            handler.sendMessage(message);
                        }
                    }else{
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
