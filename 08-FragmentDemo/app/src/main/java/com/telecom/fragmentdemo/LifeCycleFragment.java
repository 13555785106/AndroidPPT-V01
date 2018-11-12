package com.telecom.fragmentdemo;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class LifeCycleFragment extends Fragment {

    private static String TAG = "碎片生命周期 ";
    /* --- 主要生命周期函数 --- */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "<=== onAttach ===>");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.e(TAG, "<=== onCreate ===>");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "<=== onCreateView ===>");
        Log.e(TAG,"container是否为空：" + (container==null));
        /**
        * 在container不为空的前提下，如果第三参数true，则把第一个参数指定的布局添加到container中；
         * 如果第三个参数为false，则只生成布局，不添加。如果container为空，第三个参数无意义，仅仅创建了布局。
        * */
        return inflater.inflate(R.layout.fragment_life_cycle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "<=== onViewCreated ===>");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "<=== onActivityCreated ===>");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(TAG, "<=== onViewStateRestored ===>");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "<=== onStart ===>");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "<=== onResume ===>");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "<=== onPause ===>");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "<=== onSaveInstanceState ===>");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "<=== onStop ===>");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "<=== onDestroyView ===>");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "<=== onDestroy ===>");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "<=== onDetach ===>");
    }
    /* --- 主要生命周期函数 --- */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_life_cycle, menu);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.e(TAG, "<=== onAttachFragment ===>");
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.e(TAG, "<=== onViewCreated ===>");
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        Log.e(TAG, "<=== onViewCreated ===>");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "<=== onOptionsItemSelected ===>");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.e(TAG, "<=== onOptionsMenuClosed ===>");
        super.onOptionsMenuClosed(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.e(TAG, "<=== onCreateContextMenu ===>");
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG, "<=== onConfigurationChanged ===>");
    }


}
