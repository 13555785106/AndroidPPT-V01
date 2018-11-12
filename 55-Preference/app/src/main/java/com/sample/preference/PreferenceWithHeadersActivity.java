package com.sample.preference;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.List;

public class PreferenceWithHeadersActivity extends AppCompatPreferenceActivity {
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(PreferenceWithHeadersActivity.this);
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("生日");
            setListFooter(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(calendar.YEAR);
                    int month = calendar.get(calendar.MONTH);
                    int dayOfMonth = calendar.get(calendar.DAY_OF_MONTH);

                    year = mSharedPreferences.getInt("birthday_year", year);
                    month = mSharedPreferences.getInt("birthday_month", month);
                    dayOfMonth = mSharedPreferences.getInt("birthday_day", dayOfMonth);

                    DatePickerDialog dialog = new DatePickerDialog(PreferenceWithHeadersActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            mSharedPreferences.edit()
                                    .putInt("birthday_year", year)
                                    .putInt("birthday_month", month)
                                    .putInt("birthday_day", dayOfMonth)
                                    .commit();

                        }
                    }, year, month, dayOfMonth);
                    dialog.show();
                }
            });
        }

    }


    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || Prefs1Fragment.class.getName().equals(fragmentName)
                || Prefs1FragmentInner.class.getName().equals(fragmentName)
                || Prefs2Fragment.class.getName().equals(fragmentName)
                || AboutFragment.class.getName().equals(fragmentName);
    }

    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setTitle("登录设置");
            //将文件中定义的默认值写入到偏好文件中
            PreferenceManager.setDefaultValues(getActivity(), R.xml.fragmented_preferences, false);
            addPreferencesFromResource(R.xml.fragmented_preferences);
            Preference preferenceUserAccount = findPreference("user_account");
            preferenceUserAccount.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                // true  代表点击事件已成功捕捉，无须执行默认动作或者返回上层调用链。 例如，不跳转至默认Intent；false 代表执行默认动作并且返回上层调用链。例如，跳转至默认Intent。
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return true;
                }
            });
            preferenceUserAccount.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                //返回true，则使用新值更新，否则不更新
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    return true;
                }
            });
        }
    }

    public static class Prefs1FragmentInner extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setTitle("联系地址");
            Log.i("args", "Arguments: " + getArguments());
            addPreferencesFromResource(R.xml.fragmented_preferences_inner);
        }
    }


    public static class Prefs2Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setTitle("其它设置");
            Log.i("args", "Arguments: " + getArguments());
            addPreferencesFromResource(R.xml.preference_dependencies);
        }
    }

    public static class AboutFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setTitle("关于");
            Log.i("args", "Arguments: " + getArguments());
            addPreferencesFromResource(R.xml.preference_about);
        }
    }
}
