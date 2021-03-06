package com.telecom.contactsproviderdemo;

import android.net.Uri;

/**
 * Created by xiaojf on 18/2/9.
 */

public class ContactsMeta {
    public static final String AUTHORITY = "com.telecom.contactsproviderdemo.ContactsProvider";
    static final String NAME = "phone_dir.db";
    static final int VERSION = 1;

    public static final class ContactsTable {
        public static final String NAME = "contacts";
        public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/contacts");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.contactsproviderdemo.contact";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.contactsproviderdemo.contact";
        public static final class Cols {
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String PHONE_NUMS = "phone_nums";
        }
    }
}
