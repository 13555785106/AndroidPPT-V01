package com.telecom.contactsproviderdemo;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojf on 18/2/8.
 */

public class PhoneDirDbHelper extends SQLiteOpenHelper {

    PhoneDirDbHelper(Context context) {
        super(context, ContactsMeta.NAME, null, ContactsMeta.VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ContactsMeta.ContactsTable.NAME + "(" +
                ContactsMeta.ContactsTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContactsMeta.ContactsTable.Cols.NAME + " TEXT NOT NULL, " +
                ContactsMeta.ContactsTable.Cols.PHONE_NUMS + " TEXT NOT NULL" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContactsMeta.ContactsTable.NAME);
        onCreate(db);
    }

    public List<Contact> allContacts() {
        List<Contact> contactList = new ArrayList<>();
        Cursor c = getReadableDatabase().query(ContactsMeta.ContactsTable.NAME, null, null, null, null, null, " name ");
        int idIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.ID);
        int nameIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.NAME);
        int phoneNumsIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.PHONE_NUMS);
        while (c.moveToNext()) {
            contactList.add(new Contact(c.getInt(idIndex), c.getString(nameIndex), c.getString(phoneNumsIndex)));
        }
        c.close();
        return contactList;
    }

    public Contact getContact(int id) {
        Contact contact = null;
        Cursor c = getReadableDatabase().query(ContactsMeta.ContactsTable.NAME, null, ContactsMeta.ContactsTable.Cols.ID + "=?", new String[]{Integer.toString(id)}, null, null, " name ");
        int idIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.ID);
        int nameIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.NAME);
        int phoneNumsIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.PHONE_NUMS);
        if (c.moveToFirst()) {
            contact = new Contact(c.getInt(idIndex), c.getString(nameIndex), c.getString(phoneNumsIndex));
        }
        c.close();
        return contact;
    }

    public void addContact(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsMeta.ContactsTable.Cols.NAME, contact.getName());
        contentValues.put(ContactsMeta.ContactsTable.Cols.PHONE_NUMS, contact.getPhoneNums());
        getWritableDatabase().insert(ContactsMeta.ContactsTable.NAME, null, contentValues);
        Cursor c = getWritableDatabase().rawQuery("select last_insert_rowid() from " + ContactsMeta.ContactsTable.NAME, null);
        if (c.moveToFirst())
            contact.setId(c.getInt(0));
        c.close();
    }

    public boolean delContact(int id) {
        if (getWritableDatabase().delete(
                ContactsMeta.ContactsTable.NAME,
                ContactsMeta.ContactsTable.Cols.ID + "=?",
                new String[]{Integer.toString(id)}) == 1)
            return true;
        return false;
    }

    public boolean chgContact(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsMeta.ContactsTable.Cols.NAME, contact.getName());
        contentValues.put(ContactsMeta.ContactsTable.Cols.PHONE_NUMS, contact.getPhoneNums());
        if (getWritableDatabase().update(
                ContactsMeta.ContactsTable.NAME,
                contentValues,
                ContactsMeta.ContactsTable.Cols.ID + "=?",
                new String[]{Integer.toString(contact.getId())}) == 1)
            return true;
        return false;
    }

}
