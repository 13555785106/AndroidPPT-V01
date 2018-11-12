package com.telecom.photogallery;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private RecyclerView mPhotoRecyclerView;
    private int itemCount = -1;

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

        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //列宽以120dp为准
                //System.out.println("PhotoRecyclerView width=" + mPhotoRecyclerView.getWidth()+",height="+ mPhotoRecyclerView.getHeight());
                int spWidth = ViewUtils.px2dip(getActivity(),mPhotoRecyclerView.getWidth());
                int spanCount = spWidth/120;
                GridLayoutManager gm = (GridLayoutManager)mPhotoRecyclerView.getLayoutManager();
                if(spanCount != gm.getSpanCount())
                    gm.setSpanCount(spanCount);

            }
        });
        final PhotoAdapter photoAdapter = new PhotoAdapter();
        mPhotoRecyclerView.setAdapter(photoAdapter);
        new FetchPhotoItemsTask(photoAdapter).execute();

        mPhotoRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    photoAdapter.setCurPageNo(photoAdapter.getCurPageNo() + 1);
                    new FetchPhotoItemsTask(photoAdapter).execute();
                }
            }
        });
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
        private List<PhotoItem> mPhotoItems = new ArrayList<>();
        private int curPageNo = 0;

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

        public int getCurPageNo() {
            return curPageNo;
        }

        public void setCurPageNo(int curPageNo) {
            this.curPageNo = curPageNo;
        }

        public List<PhotoItem> getPhotoItems() {
            return mPhotoItems;
        }
    }

    private class FetchPhotoItemsTask extends AsyncTask<String, Void, PhotoResult> {
        private final String baseUrlStr = "http://192.168.3.50:8080/PhotoGallery/ListImage";

        private PhotoAdapter mPhotoAdapter;

        public FetchPhotoItemsTask(PhotoAdapter photoAdapter) {
            mPhotoAdapter = photoAdapter;
        }

        @Override
        protected PhotoResult doInBackground(String... params) {

            if (itemCount == -1 || mPhotoAdapter.getPhotoItems().size() < itemCount) {
                String url = Uri.parse(baseUrlStr)
                        .buildUpon()
                        .appendQueryParameter("pageno", Integer.toString(mPhotoAdapter.getCurPageNo()))
                        .build().toString();
                return PhotoFetchr.fetchPhotoItems(url, "UTF-8");
            }

            return null;
        }

        @Override
        protected void onPostExecute(PhotoResult photoResult) {
            super.onPostExecute(photoResult);
            if (photoResult != null) {
                if (itemCount == -1)
                    itemCount = photoResult.getItemCount();
                mPhotoAdapter.getPhotoItems().addAll(photoResult.getDatas());
                mPhotoAdapter.notifyDataSetChanged();
            }

        }
    }
}
