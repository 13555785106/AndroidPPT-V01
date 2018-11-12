package com.telecom.contactsproviderdemo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.telecom.contactsproviderdemo.ContactsMeta.ContactsTable.Cols.PHONE_NUMS;

public class CursorLoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = CursorLoaderActivity.class.getSimpleName();
    RecyclerView mRecyclerView;
    LoaderManager mLoaderManager;
    CursorLoader mCursorLoader;
    SearchView mSearchView;
    ContactAdapter mContactAdapter = new ContactAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate(Bundle savedInstanceState)");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursor_loader);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle("例子而已");
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mLoaderManager = getLoaderManager();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mContactAdapter);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onPostCreate(Bundle savedInstanceState)");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e(TAG, "onCreateOptionsMenu(Menu menu)");
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        Log.e(TAG, searchItem.toString());
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.e(TAG, "onQueryTextChange:" + queryText);
                //当长度为零时，回车提交无效，所以我们在此处当无查询字符串时显示全部结果。
                if (queryText.length() == 0)
                    getLoaderManager().restartLoader(1, null, CursorLoaderActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                Log.e(TAG, "onQueryTextSubmit:" + queryText);
                getLoaderManager().restartLoader(1, null, CursorLoaderActivity.this);
                //SearchView存在bug,回车之后会提交两次。
                //此处的解决办法是立刻使其失去焦点。
                mSearchView.clearFocus();
                return true;
            }
        });
        mLoaderManager.initLoader(1, null, this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mCursorLoader = new CursorLoader(this, ContactsMeta.ContactsTable.CONTENT_URI, null, PHONE_NUMS + " LIKE '%'||?||'%'", new String[]{mSearchView.getQuery().toString()}, null);
        return mCursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, "count=" + data.getCount());
        mContactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactAdapter.swapCursor(null);
    }


    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

        Cursor mCursor = null;

        public ContactAdapter() {
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(CursorLoaderActivity.this);
            return new ContactHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            Log.e(TAG, "position=" + position);
            if (mCursor != null) {
                mCursor.moveToPosition(position);
                Contact contact = new Contact(mCursor.getString(1), mCursor.getString(2));
                holder.bind(contact);
            }
        }

        @Override
        public int getItemCount() {
            if (mCursor == null)
                return 0;
            else
                return mCursor.getCount();
        }

        public void swapCursor(Cursor cursor) {
            Log.e(TAG, "swapCursor");
            mCursor = cursor;
            notifyDataSetChanged();
        }
    }

    private class ContactHolder extends RecyclerView.ViewHolder {
        private Contact mContact;
        private TextView mTextViewName;
        private TextView mTextViewPhoneNums;

        public ContactHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.simple_contact_item, parent, false));
            mTextViewName = itemView.findViewById(R.id.text_view_name);
            mTextViewPhoneNums = itemView.findViewById(R.id.text_view_phone_nums);
        }

        public void bind(Contact contact) {
            mContact = contact;
            mTextViewName.setText(mContact.getName());
            mTextViewPhoneNums.setText(mContact.getPhoneNums());
        }
    }
}
