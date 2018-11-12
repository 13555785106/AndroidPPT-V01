package com.telecom.asynctaskloaderdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 对应用程序名按英文字母排序的比较器
     */
    public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppEntry object1, AppEntry object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new AppListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "本程序用来演示AsyncTaskLoader的使用", Snackbar.LENGTH_LONG)
                        .setAction("信息", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 填充菜单，这些item会被添加到ActionBar上。
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //针对菜单项做出反应。
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * 存储App信息的类
    * */
    public static class AppEntry {
        private final AppListLoader mLoader;
        private final ApplicationInfo mInfo;
        private final File mApkFile;
        private String mLabel;
        private Drawable mIcon;
        private boolean mMounted;

        public AppEntry(AppListLoader loader, ApplicationInfo info) {
            mLoader = loader;
            mInfo = info;
            mApkFile = new File(info.sourceDir);
        }

        public ApplicationInfo getApplicationInfo() {
            return mInfo;
        }

        public String getLabel() {
            return mLabel;
        }

        public Drawable getIcon() {
            if (mIcon == null) {
                if (mApkFile.exists()) {
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                } else {
                    mMounted = false;
                }
            } else if (!mMounted) {
                // 如果sd卡以前没挂载现在被挂载了，加载图标。
                if (mApkFile.exists()) {
                    mMounted = true;
                    mIcon = mInfo.loadIcon(mLoader.mPm);
                    return mIcon;
                }
            } else {
                return mIcon;
            }

            return mLoader.getContext().getResources().getDrawable(
                    android.R.drawable.sym_def_app_icon);
        }

        @Override
        public String toString() {
            return mLabel;
        }

        void loadLabel(Context context) {
            if (mLabel == null || !mMounted) {
                if (!mApkFile.exists()) {
                    mMounted = false;
                    mLabel = mInfo.packageName;
                } else {
                    mMounted = true;
                    CharSequence label = mInfo.loadLabel(context.getPackageManager());
                    mLabel = label != null ? label.toString() : mInfo.packageName;
                }
            }
        }


    }

    /**
     * 此类用来确定系统的配置是否发生改变。
     */
    public static class InterestingConfigChanges {
        final Configuration mLastConfiguration = new Configuration();
        int mLastDensity;

        boolean applyNewConfig(Resources res) {
            int configChanges = mLastConfiguration.updateFrom(res.getConfiguration());
            boolean densityChanged = mLastDensity != res.getDisplayMetrics().densityDpi;
            if (densityChanged || (configChanges & (ActivityInfo.CONFIG_LOCALE
                    | ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
                mLastDensity = res.getDisplayMetrics().densityDpi;
                return true;
            }
            return false;
        }
    }

    /**
     * 用来监视已安装应用程序是否变化的监听器。
     */
    public static class PackageIntentReceiver extends BroadcastReceiver {
        final AppListLoader mLoader;

        public PackageIntentReceiver(AppListLoader loader) {
            mLoader = loader;
            IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
            filter.addDataScheme("package");
            mLoader.getContext().registerReceiver(this, filter);
            // 监视sdcard卡上的应用程序。
            IntentFilter sdFilter = new IntentFilter();
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
            mLoader.getContext().registerReceiver(this, sdFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //通知loader发生了改变.
            mLoader.onContentChanged();
        }
    }

    /**
     * 一个定制的Loader用来加载所有已经安装的程序。
     */
    public static class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
        final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
        final PackageManager mPm;

        List<AppEntry> mApps;
        PackageIntentReceiver mPackageObserver;

        public AppListLoader(Context context) {
            super(context);
            // 获取包管理器。这里不能直接使用构造函数的context，
            // 而是使用 getContext()获取ApplicationContext.
            mPm = getContext().getPackageManager();
        }

        /**
         * 在这个函数中执行耗时的操作，返回最后形成的结果.
         */
        @Override
        public List<AppEntry> loadInBackground() {
            // 获取所有的应用程序信息，即使没有安装.
            List<ApplicationInfo> apps = mPm.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
            if (apps == null) {
                apps = new ArrayList<ApplicationInfo>();
            }

            final Context context = getContext();

            // 创建应用程序信息数组.
            List<AppEntry> entries = new ArrayList<AppEntry>(apps.size());
            for (int i = 0; i < apps.size(); i++) {
                AppEntry entry = new AppEntry(this, apps.get(i));
                entry.loadLabel(context);
                entries.add(entry);
            }

            // 排序.
            Collections.sort(entries, ALPHA_COMPARATOR);
            return entries;
        }

        /**
         * 当把数据传递到客户端时，这个函数被调用.此处仅仅做了简单的演示，没有进一步的实际操作。
         */
        @Override
        public void deliverResult(List<AppEntry> apps) {
            if (isReset()) {
                // 如果loader被停止，我们将释放相应的资源.
                if (apps != null) {
                    onReleaseResources(apps);
                }
            }
            List<AppEntry> oldApps = mApps;
            mApps = apps;

            if (isStarted()) {
                // 如果当前Loader已经启动，我们立即传递结果.
                super.deliverResult(apps);
            }

            // 释放旧结果的相关资源
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        /**
         * 启动Loader加载应用程序信息
         */
        @Override
        protected void onStartLoading() {
            if (mApps != null) {
                // 如果当前有结果可用，立即传递
                deliverResult(mApps);
            }

            // 创建监听器监听安装的应用程序是否发生变化
            if (mPackageObserver == null) {
                mPackageObserver = new PackageIntentReceiver(this);
            }

            // 与前次结果进行对比，配置是否发生变化
            boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

            if (takeContentChanged() || mApps == null || configChange) {
                // 如果数据不可用，或者已有数据发生改变，立刻加载数据
                forceLoad();
            }
        }


        @Override
        protected void onStopLoading() {
            // 取消加载
            cancelLoad();
        }

        @Override
        public void onCanceled(List<AppEntry> apps) {
            super.onCanceled(apps);
            onReleaseResources(apps);
        }

        @Override
        protected void onReset() {
            super.onReset();

            // 确保Loader已经停止
            onStopLoading();

            // 释放相关的资源.
            if (mApps != null) {
                onReleaseResources(mApps);
                mApps = null;
            }

            // 取消监视应用程序的改变
            if (mPackageObserver != null) {
                getContext().unregisterReceiver(mPackageObserver);
                mPackageObserver = null;
            }
        }

        protected void onReleaseResources(List<AppEntry> apps) {
            // 释放相关资源的代码，这里什么都没做
        }
    }


    public static class AppListAdapter extends ArrayAdapter<AppEntry> {
        private final LayoutInflater mInflater;

        public AppListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<AppEntry> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = mInflater.inflate(R.layout.list_item_icon_text, parent, false);
            } else {
                view = convertView;
            }

            AppEntry item = getItem(position);
            ((ImageView) view.findViewById(R.id.icon)).setImageDrawable(item.getIcon());
            ((TextView) view.findViewById(R.id.text)).setText(item.getLabel());
            System.out.println(item.getLabel());
            return view;
        }
    }

    public static class AppListFragment extends ListFragment
            implements SearchView.OnQueryTextListener, SearchView.OnCloseListener,
            LoaderManager.LoaderCallbacks<List<AppEntry>> {

        // 适配器.
        AppListAdapter mAdapter;

        // 用于查询的
        SearchView mSearchView;

        // 如果不是null则过滤用户提供的结果.
        String mCurFilter;

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setEmptyText("No applications");
            setHasOptionsMenu(true);
            mAdapter = new AppListAdapter(getActivity());
            setListAdapter(mAdapter);

            // 无数据时显示一个进度指示器
            setListShown(false);
            getLoaderManager().initLoader(0, null, this);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // 在ActionBar上添加搜索项.
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            mSearchView = new MySearchView(getActivity());
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnCloseListener(this);
            mSearchView.setIconifiedByDefault(true);
            item.setActionView(mSearchView);
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // 搜索框的内容发生改变，则过滤结果
            mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
            mAdapter.getFilter().filter(mCurFilter);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            // 如果点击某个应用程序项，例如显示详细信息，可在此处理
            Log.i("LoaderCustom", "Item clicked: " + id);
        }

        @Override
        public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
            // 此处返回创建的Loader
            return new AppListLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
            // 将新数据放到adapter中.
            mAdapter.setData(data);
            // 如果界面在前台，带动画显示
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<AppEntry>> loader) {
            // 如果Loader被重置，清空adapter的数据
            mAdapter.setData(null);
        }

        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
            }

            // SearchView本身在关闭时，不会清空搜索框的内容，此处在关闭时清除输入的信息
            @Override
            public void onActionViewCollapsed() {
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }
    }
}
