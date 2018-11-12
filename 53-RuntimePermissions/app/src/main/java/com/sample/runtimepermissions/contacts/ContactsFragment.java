/*
* Copyright 2015 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sample.runtimepermissions.contacts;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.sample.runtimepermissions.R;

import java.util.ArrayList;


public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Contacts";
    /**
     * 提供获取联系人信息时要显示的列.
     */
    private static final String[] PROJECTION = {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
    /**
     * 按姓名进行排序.
     */
    private static final String ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC";
    private static String DUMMY_CONTACT_NAME = "__DUMMY CONTACT from runtime permissions sample";

    private TextView mMessageText;

    /**
     * 创建一个ContactsFragment实例.
     */
    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        mMessageText = rootView.findViewById(R.id.contact_message);

        // 为添加联系人按钮注册事件.
        Button button = rootView.findViewById(R.id.contact_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDummyContact();
            }
        });

        // 为显示第一个联系人按钮注册事件.
        button = rootView.findViewById(R.id.contact_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadContact();
            }
        });
        return rootView;
    }

    /**
     * 重启Loader来查询Contacts Content Provider显示第一个联系人.
     */
    private void loadContact() {
        getLoaderManager().restartLoader(0, null, this);
    }

    /**
     * 生成一个新的查询.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(), ContactsContract.Contacts.CONTENT_URI, PROJECTION,
                null, null, ORDER);
    }


    /**
     * 显示第一个联系人信息或者一个信息.
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            final int totalCount = cursor.getCount();
            if (totalCount > 0) {
                cursor.moveToFirst();
                String name = cursor
                        .getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                mMessageText.setText(
                        getResources().getString(R.string.contacts_string, totalCount, name));
            } else {
                mMessageText.setText(R.string.contacts_empty);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMessageText.setText(R.string.contacts_empty);
    }

    /**
     * 访问Contacts Content Provider直接插入一个联系人.
     * 联系人为"__DUMMY ENTRY",仅包含一个名字.
     */
    private void insertDummyContact() {
        // 插入新联系人时的两个操作
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(2);

        // 第一步，设置一个原始新联系人.
        ContentProviderOperation.Builder op =
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null);
        operations.add(op.build());

        // 第二步，为联系人设置姓名.
        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, DUMMY_CONTACT_NAME);
        operations.add(op.build());


        ContentResolver resolver = getActivity().getContentResolver();
        try {
            resolver.applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Snackbar.make(mMessageText.getRootView(), "不能创建新联系人: " +
                    e.getMessage(), Snackbar.LENGTH_LONG);
        }
    }
}
