package com.telecom.sqlitedemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactsDatabaseActivity extends AppCompatActivity {
    RecyclerView mContactsRecyclerView;
    EditText mNameEditText;
    EditText mPhoneNumsEditText;
    Button mAddButton;
    PhoneDirDbHelper mPhoneDirDbHelper;
    ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_database);
        mPhoneDirDbHelper = new PhoneDirDbHelper(this);
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
                mPhoneDirDbHelper.addContact(new Contact(mNameEditText.getText().toString(), mPhoneNumsEditText.getText().toString()));
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
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.access_contacts_provider:
                Intent intent = new Intent(this, ContactsProviderActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    mPhoneDirDbHelper.delContact(mContact.getId());
                    mContactAdapter.refresh();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder contactDialog = new AlertDialog.Builder(ContactsDatabaseActivity.this);
                    final View contactDialogView = LayoutInflater.from(ContactsDatabaseActivity.this).inflate(R.layout.contact_dialog, null);
                    final EditText nameEditText = contactDialogView.findViewById(R.id.name_edit_text);
                    nameEditText.setText(mContact.getName());
                    final EditText phoneNumsEditText = contactDialogView.findViewById(R.id.phone_nums_edit_text);
                    phoneNumsEditText.setText(mContact.getPhoneNums());
                    contactDialog.setTitle("修改电话号码");
                    contactDialog.setView(contactDialogView);
                    contactDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mContact.setName(nameEditText.getText().toString());
                            mContact.setPhoneNums(phoneNumsEditText.getText().toString());
                            mPhoneDirDbHelper.chgContact(mContact);
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

        List<Contact> mContacts;

        public ContactAdapter() {
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(ContactsDatabaseActivity.this);
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
            mContacts = mPhoneDirDbHelper.allContacts();
            mContactAdapter.notifyDataSetChanged();
        }
    }
}
