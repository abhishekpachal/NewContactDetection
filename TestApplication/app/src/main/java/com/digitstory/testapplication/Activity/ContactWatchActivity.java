package com.digitstory.testapplication.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import com.digitstory.testapplication.R;
import com.digitstory.testapplication.Services.ContactWatchService;

public class ContactWatchActivity extends AppCompatActivity {
    public final static int MY_PERMISSIONS_READ_CONTACTS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_watch);
        startContactLookService();
    }


    private void startContactLookService() {
        try {
            if (ActivityCompat.checkSelfPermission(ContactWatchActivity.this,
                    Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED) {//Checking permission
                //Starting service for registering ContactObserver
                Intent intent = new Intent(ContactWatchActivity.this, ContactWatchService.class);
                startService(intent);
            } else {
                //Ask for READ_CONTACTS permission
                ActivityCompat.requestPermissions(ContactWatchActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_READ_CONTACTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //If permission granted
        if (requestCode == MY_PERMISSIONS_READ_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startContactLookService();
        }
    }
}
