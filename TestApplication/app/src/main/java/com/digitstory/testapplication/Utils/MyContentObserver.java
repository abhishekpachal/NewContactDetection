package com.digitstory.testapplication.Utils;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class MyContentObserver extends ContentObserver {
    private Context context;

    public MyContentObserver(Handler handler) {
        super(handler);
    }

    public MyContentObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (!selfChange) {
            try {
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED) {
                    ContentResolver cr = context.getContentResolver();
                    Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                    if (cursor != null && cursor.getCount() > 0) {
                        //moving cursor to last position
                        //to get last element added
                        cursor.moveToLast();
                        String contactName = null, photo = null, contactNumber = null;
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            if (pCur != null) {
                                while (pCur.moveToNext()) {
                                    contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    if (contactNumber != null && contactNumber.length() > 0) {
                                        contactNumber = contactNumber.replace(" ", "");
                                    }
                                    contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                                    String msg = "Name : " + contactName + " Contact No. : " + contactNumber;
                                    //Displaying result
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                }
                                pCur.close();
                            }
                        }
                        cursor.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
