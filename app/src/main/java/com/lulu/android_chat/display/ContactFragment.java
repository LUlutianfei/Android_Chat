package com.lulu.android_chat.display;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lulu.android_chat.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private List<String> contactsList = new ArrayList<>();
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.display_contact,container,false);
        ListView contactView = view.findViewById(R.id.contact_listView);
        adapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, contactsList);
        contactView.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) view.getContext(),
                    new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            readContacts();
        }
        return view;
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
            cursor = view.getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);
            if(cursor!=null){
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName+"\n"+number);
                }
                Log.d("msg", String.valueOf(contactsList));
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                Log.d("msg", "onRequestPermissionsResult: ");
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    readContacts();
                }else {
                    Toast.makeText(getContext(), "未授权", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

