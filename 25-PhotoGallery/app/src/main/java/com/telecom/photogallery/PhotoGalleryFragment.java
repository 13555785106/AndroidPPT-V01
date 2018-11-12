package com.telecom.photogallery;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        PhotoAdapter photoAdapter = new PhotoAdapter();
        mPhotoRecyclerView.setAdapter(photoAdapter);
        new FetchItemsTask(photoAdapter).execute(new String[]{"http://192.168.3.50:8080/PhotoGallery/ListImage", "UTF-8"});
        return v;
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(PhotoItem photoItem) {
            mTitleTextView.setText(photoItem.getName());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<PhotoItem> mPhotoItems;

        public PhotoAdapter() {
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            PhotoItem photoItem = mPhotoItems.get(position);
            photoHolder.bindGalleryItem(photoItem);
        }

        @Override
        public int getItemCount() {
            if (mPhotoItems == null)
                return 0;
            else
                return mPhotoItems.size();
        }

        public void setPhotoItems(List<PhotoItem> photoItems) {
            mPhotoItems = photoItems;
        }
    }

    private class FetchItemsTask extends AsyncTask<String, Void, PhotoResult> {
        PhotoAdapter mPhotoAdapter;

        public FetchItemsTask(PhotoAdapter photoAdapter) {
            mPhotoAdapter = photoAdapter;
        }

        @Override
        protected PhotoResult doInBackground(String... params) {
            return PhotoFetchr.fetchPhotoItems(params[0],params[1]);

        }

        @Override
        protected void onPostExecute(PhotoResult photoResult) {
            super.onPostExecute(photoResult);
            mPhotoAdapter.setPhotoItems(photoResult.getDatas());
            mPhotoAdapter.notifyDataSetChanged();
        }
    }
}
