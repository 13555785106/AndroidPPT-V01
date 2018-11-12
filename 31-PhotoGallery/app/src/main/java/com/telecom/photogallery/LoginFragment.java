package com.telecom.photogallery;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginFragment extends Fragment {
    private static final String baseLoginUrl = "http://192.168.3.50:8080/PhotoGallery/Login";
    private TextView mMsgText;
    private EditText mUsernameText;
    private EditText mPasswordText;
    private Button mLogin;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhotoFetchr.initCookieManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mMsgText = v.findViewById(R.id.msg_text);
        mUsernameText = v.findViewById(R.id.username_text);
        mPasswordText = v.findViewById(R.id.password_text);
        mLogin = v.findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask().execute(baseLoginUrl, mUsernameText.getText().toString(), mPasswordText.getText().toString());
            }
        });
        return v;
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        public LoginTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mMsgText.setText("");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return PhotoFetchr.login(params[0], params[1], params[2]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                mMsgText.setText("登录成功！");
                Intent intent = PhotoGalleryActivity.newIntent(getActivity());
                startActivity(intent);
                getActivity().finish();
            } else
                mMsgText.setText("登录失败！");
        }
    }
}
