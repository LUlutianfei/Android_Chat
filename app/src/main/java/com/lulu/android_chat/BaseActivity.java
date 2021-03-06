package com.lulu.android_chat;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends Activity {
    private ForceOfflineReceiver receiver;
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        ActivityCollector.addActivity(this);
    }

    protected void finishAll(final Context context){
        ActivityCollector.finishAll();
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lulu.broadcasttest.FORCE_OFFLINE");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private class ForceOfflineReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Waining");
            builder.setMessage("You are forced to be offline.Please try to login again.");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, Login.class);
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }
}
