package com.telecom.baidumap;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiaojf on 17/12/23.
 */

public final class ViewUtils {
    private ViewUtils() {
    }

    public static void printViewHiberarchy(View view, int indent, int indentStep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++)
            sb.append(' ');
        System.out.println(sb.toString() + view.getClass().getSimpleName() + " " + view.getId());
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                printViewHiberarchy(v, indent + indentStep, indentStep);
            }
        }
    }


}
