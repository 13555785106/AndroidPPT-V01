package com.telecom.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    private RecyclerView mPhotoRecyclerView;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private ThumbnailPreloader mThumbnailPreloader;
    private int mPreloadCount = 15;
    private int childCount = -1;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i(TAG, "主线程id = " + Thread.currentThread().getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        final PhotoAdapter photoAdapter = new PhotoAdapter();
        mPhotoRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //列宽以120dp为准
                int spWidth = ViewUtils.px2dip(getActivity(), mPhotoRecyclerView.getWidth());
                int spanCount = spWidth / 120;
                GridLayoutManager gm = (GridLayoutManager) mPhotoRecyclerView.getLayoutManager();
                if (spanCount != gm.getSpanCount())
                    gm.setSpanCount(spanCount);
                if (childCount == -1 && gm.getChildCount() > 0)
                    childCount = gm.getChildCount();

                if (photoAdapter.getCurPageNo() == 0 && childCount > 0) {
                    for (int i = childCount; i < childCount + mPreloadCount && i < photoAdapter.getItemCount(); i++)
                        mThumbnailPreloader.queueThumbnail(photoAdapter.getPhotoItem(i).getUrl());
                    childCount = -2;
                }
            }
        });


        mPhotoRecyclerView.setAdapter(photoAdapter);
        mPhotoRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                GridLayoutManager gm = (GridLayoutManager) recyclerView.getLayoutManager();

                Log.i(TAG, "-------------------> scollState=" + newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int firstVisibleItemPosition = gm.findFirstVisibleItemPosition();
                    int lastVisibleItemPosition = gm.findLastVisibleItemPosition();
                    Log.i(TAG, "FirstVisibleItemPosition=" + firstVisibleItemPosition);
                    Log.i(TAG, "LastVisibleItemPosition=" + lastVisibleItemPosition);
                    int start = firstVisibleItemPosition - mPreloadCount;
                    if (start < 0)
                        start = 0;
                    for (int i = start; i < firstVisibleItemPosition; i++)
                        mThumbnailPreloader.queueThumbnail(photoAdapter.getPhotoItem(i).getUrl());
                    for (int i = lastVisibleItemPosition + 1; i < lastVisibleItemPosition + 1 + mPreloadCount && i < photoAdapter.getItemCount(); i++)
                        mThumbnailPreloader.queueThumbnail(photoAdapter.getPhotoItem(i).getUrl());
                }
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


        new FetchPhotoItemsTask(photoAdapter).execute();

        mPhotoRecyclerView.onScrollStateChanged(RecyclerView.SCROLL_STATE_IDLE);
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //设置LruCache的缓存大小
        int cacheSize = maxMemory / 8;
        LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };

        Handler responseHandler = new Handler();
        Log.i(TAG, "responseHandler 的 Looper = " + responseHandler.getLooper().toString());
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler, lruCache);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                    public void onThumbnailDownloaded(PhotoHolder photoHolder, Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        photoHolder.bindDrawable(drawable);
                    }
                }
        );

        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
        Log.i(TAG, "工作线程 id = " + mThumbnailDownloader.getId());

        mThumbnailPreloader = new ThumbnailPreloader(lruCache);
        mThumbnailPreloader.start();
        mThumbnailPreloader.getLooper();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailPreloader.quit();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super(itemView);
            mItemImageView = itemView.findViewById(R.id.item_image_view);

        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<PhotoItem> mPhotoItems = new ArrayList<>();
        private int curPageNo = 0;
        private int totalCount = -1;//图片的实际总数

        public PhotoAdapter() {
        }

        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_gallery, viewGroup, false);
            return new PhotoHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            PhotoItem photoItem = mPhotoItems.get(position);
            Drawable placeholder = getResources().getDrawable(android.R.drawable.btn_star_big_on);
            photoHolder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(photoHolder, photoItem.getUrl());
        }

        @Override
        public int getItemCount() {
            if (mPhotoItems == null)
                return 0;
            else
                return mPhotoItems.size();
        }

        public PhotoItem getPhotoItem(int position) {
            return mPhotoItems.get(position);
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

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
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

            if (mPhotoAdapter.getTotalCount() == -1 || mPhotoAdapter.getPhotoItems().size() < mPhotoAdapter.getTotalCount()) {
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
                if (mPhotoAdapter.getTotalCount() == -1)
                    mPhotoAdapter.setTotalCount(photoResult.getItemCount());
                mPhotoAdapter.getPhotoItems().addAll(photoResult.getDatas());
                mPhotoAdapter.notifyDataSetChanged();

            }

        }
    }
}
