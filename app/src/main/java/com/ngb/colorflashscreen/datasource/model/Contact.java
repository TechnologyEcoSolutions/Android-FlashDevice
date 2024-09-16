package com.ngb.colorflashscreen.datasource.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import com.ngb.colorflashscreen.utils.App;

public class Contact {
    public Number type = Number.HIDDEN;
    public String number = null;
    public String name = null;
    public Bitmap photo = null;
    public String time = null;
    private Context context;
    public Contact( String phoneNumber ) {
        context = App.getContext();
        try {
            setNumber(phoneNumber);
            type = Number.JUST_PHONE;

            setContact(phoneNumber);
            type = Number.FULL;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setNumber( String phoneNumber ) throws Exception {
        long numberInt = -1;
        try {
            numberInt = Long.parseLong(phoneNumber);
        } catch (Exception ignored) {
        }

        if (numberInt < 0) {
            throw new Exception("Hidden number");
        }

        number = PhoneNumberUtils.formatNumber(phoneNumber, "VN");
        if (number == null) {
            number = phoneNumber;
        }
    }

    private void setContact( String phoneNumber ) {
        ContentResolver contentResolver = context.getContentResolver();

        // lay URI cua sdt
        Uri phoneUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        // nhan lien he theo so
        Cursor contactCursor = contentResolver.query(phoneUri, null, null, null, null);
        assert contactCursor != null;
        contactCursor.moveToFirst();

        // nhan ID lien he de yeu cau bo sung theo so
        String contactId = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts._ID));

        // luu ten
        name = contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        contactCursor.close();

        // nhan URI cua lien he
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));

        // nhan URI cua anh
        Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
        try {
            AssetFileDescriptor photoFd = contentResolver.openAssetFileDescriptor(displayPhotoUri, "r");
            assert photoFd != null;
            photo = BitmapFactory.decodeStream(photoFd.createInputStream());
        } catch (Exception e) {
            photo = null;
        }
    }

    public enum Number {HIDDEN, JUST_PHONE, FULL}

}
