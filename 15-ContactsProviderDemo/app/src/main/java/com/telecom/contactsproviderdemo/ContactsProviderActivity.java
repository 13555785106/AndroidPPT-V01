package com.telecom.contactsproviderdemo;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.telecom.contactsproviderdemo.ContactsMeta.ContactsTable.CONTENT_URI;

public class ContactsProviderActivity extends AppCompatActivity {
    private static final String TAG = ContactsProviderActivity.class.getSimpleName();
    RecyclerView mContactsRecyclerView;
    EditText mNameEditText;
    EditText mPhoneNumsEditText;
    Button mAddButton;
    ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_provider);
        mContactAdapter = new ContactAdapter();
        mContactsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_contacts);
        mContactsRecyclerView.setAdapter(mContactAdapter);
        mContactsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mPhoneNumsEditText = (EditText) findViewById(R.id.phone_nums_edit_text);
        mAddButton = (Button) findViewById(R.id.button_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ContactsMeta.ContactsTable.Cols.NAME, mNameEditText.getText().toString());
                contentValues.put(ContactsMeta.ContactsTable.Cols.PHONE_NUMS, mPhoneNumsEditText.getText().toString());
                getContentResolver().insert(CONTENT_URI, contentValues);
                mContactAdapter.refresh();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mContactAdapter.refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main1, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_cursor_loader:
                startActivity(new Intent(getApplicationContext(),CursorLoaderActivity.class));
                break;
            default:

        }
        return true;
    }
    private class ContactHolder extends RecyclerView.ViewHolder {
        private TextView mNameTextView;
        private TextView mPhoneNumsTextView;
        private ImageView mRemoveImageView;
        private Contact mContact;

        public ContactHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_contact_item, parent, false));
            mNameTextView = itemView.findViewById(R.id.name_text_view);
            mPhoneNumsTextView = itemView.findViewById(R.id.phone_nums_text_view);
            mRemoveImageView = itemView.findViewById(R.id.remove_image_view);
            mRemoveImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getContentResolver().delete(ContentUris.withAppendedId(CONTENT_URI, mContact.getId()), null, null);
                    mContactAdapter.refresh();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder contactDialog = new AlertDialog.Builder(ContactsProviderActivity.this);
                    final View contactDialogView = LayoutInflater.from(ContactsProviderActivity.this).inflate(R.layout.contact_dialog, null);
                    final EditText nameEditText = contactDialogView.findViewById(R.id.name_edit_text);
                    nameEditText.setText(mContact.getName());
                    final EditText phoneNumsEditText = contactDialogView.findViewById(R.id.phone_nums_edit_text);
                    phoneNumsEditText.setText(mContact.getPhoneNums());
                    contactDialog.setTitle("修改电话号码");
                    contactDialog.setView(contactDialogView);
                    contactDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(ContactsMeta.ContactsTable.Cols.NAME, nameEditText.getText().toString());
                            contentValues.put(ContactsMeta.ContactsTable.Cols.PHONE_NUMS, phoneNumsEditText.getText().toString());
                            getContentResolver().update(ContentUris.withAppendedId(CONTENT_URI, mContact.getId()), contentValues, null, null);
                            mContactAdapter.refresh();
                        }
                    });
                    contactDialog.show();
                }
            });
        }

        public void bind(Contact contact) {
            mContact = contact;
            mNameTextView.setText(mContact.getName());
            mPhoneNumsTextView.setText(mContact.getPhoneNums());
        }
    }


    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

        List<Contact> mContacts = new ArrayList<>();

        public ContactAdapter() {
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ContactsProviderActivity.this);
            return new ContactHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            Contact contact = mContacts.get(position);
            holder.bind(contact);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }

        public void refresh() {
            mContacts.clear();
            Cursor c = getContentResolver().query(CONTENT_URI, null, null, null, null);
            int idIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.ID);
            int nameIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.NAME);
            int phoneNumsIndex = c.getColumnIndex(ContactsMeta.ContactsTable.Cols.PHONE_NUMS);
            while (c.moveToNext()) {
                mContacts.add(new Contact(c.getInt(idIndex), c.getString(nameIndex), c.getString(phoneNumsIndex)));
            }
            c.close();
            mContactAdapter.notifyDataSetChanged();
        }
    }
}
