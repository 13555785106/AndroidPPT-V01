package com.sample.apptest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final static String TAG = MainActivityFragment.class.getSimpleName();
    GridLayout mGridLayout;
    PackageManager mPackageManager;
    Configuration mConfiguration = new Configuration();

    public MainActivityFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPackageManager = getContext().getPackageManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);
        //Log.e(TAG,container.toString());
        mGridLayout = v.findViewById(R.id.grid_layout);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_01:
                        Log.e(TAG, "getContext().getPackageResourcePath()=" + getContext().getPackageResourcePath());
                        List<ApplicationInfo> apps = mPackageManager.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
                        for (ApplicationInfo ai : apps) {
                            Log.e(TAG, "" + ai.processName);
                            Log.e(TAG, "" + ai.loadIcon(mPackageManager).toString());
                        }
                        break;
                    case R.id.button_02:
                        //本例查看屏幕方向是否改变
                        int configChanges = mConfiguration.updateFrom(getResources().getConfiguration());
                        Log.e(TAG, "CONFIG_ORIENTATION=" + ((configChanges & ActivityInfo.CONFIG_ORIENTATION) != 0));
                        break;
                    case R.id.button_03:
                        startActivity(new Intent(getContext(), LayoutInflaterActivity.class));
                        break;
                    case R.id.button_04:
                        startActivity(new Intent(getContext(), GsonActivity.class));
                        break;
                    case R.id.button_05:
                        startActivity(new Intent(getContext(), ToolBarActivity.class));
                        break;
                    case R.id.button_06:
                        Snackbar snackbar = Snackbar.make(v, "Hello World!", Snackbar.LENGTH_LONG)
                                .setAction("点我看效果", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.e(TAG, "Snackbar 被点击!");
                                    }
                                });
                        AppCompatTextView snackbarTextView = snackbar.getView().findViewById(R.id.snackbar_text);//snackbar_action
                        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher_round, null);//图片可以自己选择
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        snackbarTextView.setCompoundDrawables(drawable, null, null, null);

                        AppCompatButton snackbarButton = snackbar.getView().findViewById(R.id.snackbar_action);
                        snackbarButton.setCompoundDrawables(drawable, null, null, null);

                        Log.e(TAG, new ViewHiberarchyPrinter(snackbar.getView()).toString());
                        snackbar.show();
                        break;
                    case R.id.button_07:
                        startActivity(new Intent(getContext(), ViewPagerActivity.class));
                        break;
                }
            }
        };
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            View cv = mGridLayout.getChildAt(i);
            if (cv instanceof Button) {
                cv.setOnClickListener(clickListener);
            }
        }
        return v;
    }

}
