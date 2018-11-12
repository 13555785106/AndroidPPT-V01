package com.telecom.gaodemap;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
import static android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS;
import static android.content.Intent.FLAG_ACTIVITY_FORWARD_RESULT;
import static android.content.Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY;
import static android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;
import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;
import static android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION;
import static android.content.Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static android.content.Intent.FLAG_ACTIVITY_TASK_ON_HOME;
import static android.content.Intent.FLAG_DEBUG_LOG_RESOLUTION;
import static android.content.Intent.FLAG_FROM_BACKGROUND;
import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_PREFIX_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.content.Intent.FLAG_RECEIVER_REGISTERED_ONLY;

/**
 * Created by xiaojf on 17/12/29.
 */

public final class IntentUtils {
    private IntentUtils() {
    }

    public static void printIntentInfo(Intent intent) {
        System.out.println("ACTION : " + intent.getAction());
        System.out.println("DATA : " + intent.getDataString());
        System.out.println("TYPE : " + intent.getType());
        System.out.println("PACKAGE : " + intent.getPackage());
        System.out.println("FLAGS : " + getIntentFlagsString(intent.getFlags()));
        System.out.println("CATEGORIES : " + intent.getCategories());
        Bundle extras = intent.getExtras();
        if (extras != null) {
            System.out.print("EXTRAS : ");
            for (String key : extras.keySet()) {
                System.out.println(key + "=" + extras.get(key) + "   ");
            }
        }
    }

    public static List<String> getIntentFlagsString(int flags) {

        List<String> flagsList = new ArrayList<>();
        if ((flags & FLAG_GRANT_READ_URI_PERMISSION) > 0) {
            flagsList.add("FLAG_GRANT_READ_URI_PERMISSION");
        }
        if ((flags & FLAG_GRANT_WRITE_URI_PERMISSION) > 0) {
            flagsList.add("FLAG_GRANT_WRITE_URI_PERMISSION");
        }
        if ((flags & FLAG_GRANT_PERSISTABLE_URI_PERMISSION) > 0) {
            flagsList.add("FLAG_GRANT_PERSISTABLE_URI_PERMISSION");
        }

        if ((flags & FLAG_GRANT_PREFIX_URI_PERMISSION) > 0) {
            flagsList.add("FLAG_GRANT_PREFIX_URI_PERMISSION");
        }
        if ((flags & FLAG_DEBUG_LOG_RESOLUTION) > 0) {
            flagsList.add("FLAG_DEBUG_LOG_RESOLUTION");
        }
        if ((flags & FLAG_FROM_BACKGROUND) > 0) {
            flagsList.add("FLAG_FROM_BACKGROUND");
        }
        if ((flags & FLAG_ACTIVITY_BROUGHT_TO_FRONT) > 0) {
            flagsList.add("FLAG_ACTIVITY_BROUGHT_TO_FRONT");
        }
        if ((flags & FLAG_ACTIVITY_CLEAR_TASK) > 0) {
            flagsList.add("FLAG_ACTIVITY_CLEAR_TASK");
        }
        if ((flags & FLAG_ACTIVITY_CLEAR_TOP) > 0) {
            flagsList.add("FLAG_ACTIVITY_CLEAR_TOP");
        }
        if ((flags & FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET) > 0) {
            flagsList.add("FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET");
        }
        if ((flags & FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) > 0) {
            flagsList.add("FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS");
        }
        if ((flags & FLAG_ACTIVITY_FORWARD_RESULT) > 0) {
            flagsList.add("FLAG_ACTIVITY_FORWARD_RESULT");
        }
        if ((flags & FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) > 0) {
            flagsList.add("FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY");
        }
        if ((flags & FLAG_ACTIVITY_MULTIPLE_TASK) > 0) {
            flagsList.add("FLAG_ACTIVITY_MULTIPLE_TASK");
        }
        if ((flags & FLAG_ACTIVITY_NEW_DOCUMENT) > 0) {
            flagsList.add("FLAG_ACTIVITY_NEW_DOCUMENT");
        }
        if ((flags & FLAG_ACTIVITY_NEW_TASK) > 0) {
            flagsList.add("FLAG_ACTIVITY_NEW_TASK");
        }
        if ((flags & FLAG_ACTIVITY_NO_ANIMATION) > 0) {
            flagsList.add("FLAG_ACTIVITY_NO_ANIMATION");
        }
        if ((flags & FLAG_ACTIVITY_NO_HISTORY) > 0) {
            flagsList.add("FLAG_ACTIVITY_NO_HISTORY");
        }
        if ((flags & FLAG_ACTIVITY_NO_USER_ACTION) > 0) {
            flagsList.add("FLAG_ACTIVITY_NO_USER_ACTION");
        }
        if ((flags & FLAG_ACTIVITY_PREVIOUS_IS_TOP) > 0) {
            flagsList.add("FLAG_ACTIVITY_PREVIOUS_IS_TOP");
        }
        if ((flags & FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) > 0) {
            flagsList.add("FLAG_ACTIVITY_RESET_TASK_IF_NEEDED");
        }
        if ((flags & FLAG_ACTIVITY_REORDER_TO_FRONT) > 0) {
            flagsList.add("FLAG_ACTIVITY_REORDER_TO_FRONT");
        }
        if ((flags & FLAG_ACTIVITY_SINGLE_TOP) > 0) {
            flagsList.add("FLAG_ACTIVITY_SINGLE_TOP");
        }
        if ((flags & FLAG_ACTIVITY_TASK_ON_HOME) > 0) {
            flagsList.add("FLAG_ACTIVITY_TASK_ON_HOME");
        }
        if ((flags & FLAG_RECEIVER_REGISTERED_ONLY) > 0) {
            flagsList.add("FLAG_RECEIVER_REGISTERED_ONLY");
        }
        return flagsList;
    }
}
