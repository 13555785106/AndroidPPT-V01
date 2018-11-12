package com.telecom.contactsproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.telecom.contactsproviderdemo.ContactsMeta.ContactsTable.CONTENT_URI;

public class ContactsProvider extends ContentProvider {
    private static final String TAG = ContactsProvider.class.getSimpleName();
    private static final UriMatcher sUriMather = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int MAIN_CONTACTS_URI_INDICATOR = 1;
    private static final int SINGLE_CONTACT_URI_INDICATOR = 2;

    static {
        sUriMather.addURI(ContactsMeta.AUTHORITY, "contacts", MAIN_CONTACTS_URI_INDICATOR);
        sUriMather.addURI(ContactsMeta.AUTHORITY, "contacts/#", SINGLE_CONTACT_URI_INDICATOR);
    }

    SQLiteDatabase mSQLiteDatabase;

    public ContactsProvider() {

    }

    @Override
    public boolean onCreate() {
        mSQLiteDatabase = new PhoneDirDbHelper(getContext()).getWritableDatabase();
        Log.e(TAG,"getReadPermission()="+getReadPermission());
        Log.e(TAG,"getWritePermission()="+getWritePermission());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMather.match(uri)) {
            case MAIN_CONTACTS_URI_INDICATOR:
                return ContactsMeta.ContactsTable.CONTENT_TYPE;
            case SINGLE_CONTACT_URI_INDICATOR:
                return ContactsMeta.ContactsTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("错误的Uri格式:" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        switch (sUriMather.match(uri)) {
            case MAIN_CONTACTS_URI_INDICATOR:
                c = mSQLiteDatabase.query(ContactsMeta.ContactsTable.NAME, projection, selection, selectionArgs, null, null, " name ");
                break;
            case SINGLE_CONTACT_URI_INDICATOR:
                c = mSQLiteDatabase.query(ContactsMeta.ContactsTable.NAME, null, ContactsMeta.ContactsTable.Cols.ID + "=?", new String[]{uri.getPathSegments().get(1)}, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("错误的Uri格式:" + uri);
        }
        //如果查询结果发生变化，会通知ContentResolver
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sUriMather.match(uri) == MAIN_CONTACTS_URI_INDICATOR) {
            values.remove(ContactsMeta.ContactsTable.Cols.ID);
            mSQLiteDatabase.insert(ContactsMeta.ContactsTable.NAME, null, values);
            Cursor c = mSQLiteDatabase.rawQuery("select last_insert_rowid() from " + ContactsMeta.ContactsTable.NAME, null);
            int id = 0;
            if (c.moveToFirst())
                id = c.getInt(0);
            c.close();
            Uri insertedUri = ContentUris.withAppendedId(CONTENT_URI, id);
            // 当该URI的ContentProvider数据发生变化时，通知外界（即访问该ContentProvider数据的访问者）
            getContext().getContentResolver().notifyChange(insertedUri, null);
            return insertedUri;
        } else
            throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMather.match(uri)) {
            case SINGLE_CONTACT_URI_INDICATOR:
                count = mSQLiteDatabase.delete(
                        ContactsMeta.ContactsTable.NAME,
                        ContactsMeta.ContactsTable.Cols.ID + "=?",
                        new String[]{uri.getPathSegments().get(1)});
                break;
            case MAIN_CONTACTS_URI_INDICATOR:
                count = mSQLiteDatabase.delete(ContactsMeta.ContactsTable.NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        if (sUriMather.match(uri) == SINGLE_CONTACT_URI_INDICATOR) {
            int count = mSQLiteDatabase.update(
                    ContactsMeta.ContactsTable.NAME,
                    values,
                    ContactsMeta.ContactsTable.Cols.ID + "=?",
                    new String[]{uri.getPathSegments().get(1)});
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } else
            throw new UnsupportedOperationException("Not yet implemented");
    }
}
