package com.sample.app12306;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sample.app12306.http.RequestThread;
import com.sample.app12306.util.SharedPreferencesHelper;
import com.sample.app12306.util.SimpleTextWatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sample.app12306.Constants.MY_PREFERENCE_ACCOUNT;
import static com.sample.app12306.Constants.MY_PREFERENCE_AUTOLOGIN;
import static com.sample.app12306.Constants.MY_PREFERENCE_PASSWD;

public class LoginFragment extends Fragment {
    private SharedPreferencesHelper mSharedPreferencesHelper;
    private EditText mEditTextAccount;
    private EditText mEditTextPasswd;
    private Button mButtonLogin;
    private CheckBox mCheckBoxAutoLogin;
    private TextView mTextViewLostPasswd;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Integer.MAX_VALUE:
                    String response = msg.getData().getString("response", "");
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            showLoginError();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showLoginError();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void showLoginError(){
        final Snackbar snackbar = Snackbar.make(getView(), "登录失败，请检查账号和密码及网络！", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity(), null);
        mEditTextAccount = v.findViewById(R.id.edit_text_account);
        mEditTextAccount.setText(mSharedPreferencesHelper.getSharedPreference(MY_PREFERENCE_ACCOUNT, "").toString());
        mEditTextAccount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mSharedPreferencesHelper.put(MY_PREFERENCE_ACCOUNT, mEditTextAccount.getText().toString());
            }
        });
        mEditTextPasswd = v.findViewById(R.id.edit_text_passwd);
        mEditTextPasswd.setText(mSharedPreferencesHelper.getSharedPreference(MY_PREFERENCE_PASSWD, "").toString());
        mEditTextPasswd.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mSharedPreferencesHelper.put(MY_PREFERENCE_PASSWD, mEditTextPasswd.getText().toString());
            }
        });

        mCheckBoxAutoLogin = v.findViewById(R.id.check_box_auto_login);
        mCheckBoxAutoLogin.setChecked((boolean) mSharedPreferencesHelper.getSharedPreference(MY_PREFERENCE_AUTOLOGIN, false));
        mCheckBoxAutoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(mCheckBoxAutoLogin.isChecked());
                mSharedPreferencesHelper.put(MY_PREFERENCE_AUTOLOGIN, mCheckBoxAutoLogin.isChecked());
            }
        });
        mTextViewLostPasswd = v.findViewById(R.id.text_view_lost_passwd);
        mTextViewLostPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "这个功能暂不支持", Snackbar.LENGTH_LONG)
                        .setAction("详细信息", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), "你好，这个功能以后支持！", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
        mButtonLogin = v.findViewById(R.id.button_login);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, List<String>> reqParams = new HashMap<>();
                reqParams.put("account", Arrays.asList(new String[]{mEditTextAccount.getText().toString()}));
                reqParams.put("passwd", Arrays.asList(new String[]{mEditTextPasswd.getText().toString()}));
                new Thread(new RequestThread(mHandler, "/Login", reqParams)).start();
            }
        });

        if ((boolean) mSharedPreferencesHelper.getSharedPreference(MY_PREFERENCE_AUTOLOGIN, false)) {
            mButtonLogin.callOnClick();
        }
        return v;
    }
}
