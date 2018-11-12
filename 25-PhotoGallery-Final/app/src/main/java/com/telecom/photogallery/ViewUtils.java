package com.telecom.photogallery;

import android.content.Context;
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

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
