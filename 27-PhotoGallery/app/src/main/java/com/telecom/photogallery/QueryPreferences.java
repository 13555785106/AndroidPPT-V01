package com.telecom.photogallery;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by xiaojf on 18/1/12.
 */

public class QueryPreferences{

        private static final String PREF_SEARCH_QUERY = "searchQuery";

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

}
