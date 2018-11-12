package com.telecom.photogallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class LoginActivity extends SingleFragmentActivity {
    public static Intent newIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        getSupportActionBar().setTitle("登录");
        return LoginFragment.newInstance();
    }
}
