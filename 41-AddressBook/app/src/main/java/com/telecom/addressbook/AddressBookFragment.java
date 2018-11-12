package com.telecom.addressbook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddressBookFragment extends Fragment {
    private static final String TAG = "AddressBookFragment";
    private EditText mContactNameEditText;
    private EditText mContactNumsEditText;
    private Button mAddContactButton;
    private RecyclerView mPhoneNumRecyclerView;

    public static AddressBookFragment newInstance() {
        return new AddressBookFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_address_book, container, false);
        mPhoneNumRecyclerView = v.findViewById(R.id.phone_num_recycler_view);
        mPhoneNumRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mContactNameEditText = v.findViewById(R.id.contact_name_edit_text);
        mContactNumsEditText = v.findViewById(R.id.contact_nums_edit_text);
        mAddContactButton = v.findViewById(R.id.add_contact_button);
        mAddContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = mContactNameEditText.getText().toString().trim();
                String contactNums = mContactNumsEditText.getText().toString().trim();
                if (!contactName.isEmpty() && !contactNums.isEmpty()) {
                    ContentValues values = new ContentValues();
                    Uri rawContactUri = getActivity().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
                    long rawContactId = ContentUris.parseId(rawContactUri);
                    values.clear();

                    values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                    values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                    values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contactName);
                    getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                    values.clear();

                    for (String str : contactNums.split(" ")) {
                        str = str.trim();
                        if (!str.isEmpty()) {
                            values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, rawContactId);
                            values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, str);
                            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
                            getActivity().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
                            values.clear();
                        }
                    }
                    mPhoneNumRecyclerView.setAdapter(new PhoneItemAdapter(getPhoneItems()));
                }
            }
        });
        return v;
    }

    private List<PhoneItem> getPhoneItems() {
        List<PhoneItem> phoneItemList = new ArrayList<>();
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor c = getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.PHOTO_URI
                },
                null,
                null,
                sortOrder);
        while (c.moveToNext()) {
            PhoneItem phoneItem = new PhoneItem();
            phoneItemList.add(phoneItem);
            phoneItem.setId(c.getLong(0));
            System.out.println(c.getLong(0));
            phoneItem.setName(c.getString(1));
            Cursor cc = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + c.getString(0),
                    null,
                    null);
            while (cc.moveToNext()) {
                phoneItem.getPhoneNums().add(cc.getString(0));
            }
            cc.close();
            String photoThumbUri = c.getString(2);
            if (photoThumbUri == null) {
                phoneItem.setThumb(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            } else {
                try {
                    AssetFileDescriptor fd = getActivity().getContentResolver().openAssetFileDescriptor(Uri.parse(photoThumbUri), "r");
                    InputStream inStream = fd.createInputStream();
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    byte[] data = outStream.toByteArray();
                    phoneItem.setThumb(BitmapFactory.decodeByteArray(data, 0, data.length));
                    outStream.close();
                    fd.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        c.close();
        return phoneItemList;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPhoneNumRecyclerView.setAdapter(new PhoneItemAdapter(getPhoneItems()));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class PhoneItemHolder extends RecyclerView.ViewHolder {
        private PhoneItem mPhoneItem;
        private ImageView mThumbImageView;
        private TextView mNameTextView;
        private TextView mPhoneNumsTextView;
        private Button mDelContactButton;

        public PhoneItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_phone_num, parent, false));
            mThumbImageView = itemView.findViewById(R.id.thumb_image_view);
            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mPhoneNumsTextView = itemView.findViewById(R.id.phone_nums_text_view);
            mDelContactButton = itemView.findViewById(R.id.del_contact_button);
            mDelContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getActivity().getContentResolver().delete(
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, mPhoneItem.getId()),
                            null,
                            null
                    ) == 1) {
                        mPhoneNumRecyclerView.setAdapter(new PhoneItemAdapter(getPhoneItems()));
                    }
                }
            });
        }

        public void bind(PhoneItem phoneItem) {
            mPhoneItem = phoneItem;
            mNameTextView.setText(mPhoneItem.getName());
            StringBuilder sb = new StringBuilder();
            for (String str : mPhoneItem.getPhoneNums()) {
                sb.append(str);
                sb.append("\r\n");
            }
            mPhoneNumsTextView.setText(sb.toString().trim());
            mThumbImageView.setImageBitmap(phoneItem.getThumb());
        }
    }

    private class PhoneItemAdapter extends RecyclerView.Adapter<PhoneItemHolder> {
        private List<PhoneItem> mPhoneItems;

        public PhoneItemAdapter(List<PhoneItem> phoneItems) {
            mPhoneItems = phoneItems;
        }

        @Override
        public PhoneItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PhoneItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PhoneItemHolder holder, int position) {
            PhoneItem phoneItem = mPhoneItems.get(position);
            holder.bind(phoneItem);
        }

        @Override
        public int getItemCount() {
            return mPhoneItems.size();
        }

    }

}
