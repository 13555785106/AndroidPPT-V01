package com.telecom.fragmentdemo;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiaojf on 18/2/4.
 */

public class ViewHiberarchyPrinter {
    StringBuilder sb = new StringBuilder();

    public ViewHiberarchyPrinter(View v) {
        this(v, 4);
    }

    public ViewHiberarchyPrinter(View v, int indent) {
        walk(v, 0, 4);
    }

    private void walk(View view, int level, int indent) {
        for (int i = 0; i < indent * level; i++)
            sb.append(' ');
        sb.append(view.getClass().getSimpleName() + " " + view.getId());
        sb.append("\n");
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View v = vg.getChildAt(i);
                walk(v, level + 1, indent);
            }
        }
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
