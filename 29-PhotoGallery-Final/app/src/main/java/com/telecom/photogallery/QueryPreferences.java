package com.telecom.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by xiaojf on 18/1/12.
 */

public class QueryPreferences {

    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_FIRST_RESULT_ID = "firstResultId";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final String PREF_RESULT_COUNT = "resultCount";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SEARCH_QUERY, null);
    }

    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_QUERY, query)
                .apply();
    }

    public static String getResultCount(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_RESULT_COUNT, null);
    }

    public static void setResultCount(Context context, String resultCount) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_RESULT_COUNT, resultCount)
                .apply();
    }

    public static String getFirstResultId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_FIRST_RESULT_ID, null);
    }

    public static void setFirstResultId(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_FIRST_RESULT_ID, lastResultId)
                .apply();
    }

    public static String getLastResultId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);
    }

    public static void setLastResultId(Context context, String lastResultId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply();
    }

    public static boolean isAlarmOn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_IS_ALARM_ON, false);
    }

    public static void setAlarmOn(Context context, boolean isOn) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON, isOn)
                .apply();
    }
}
