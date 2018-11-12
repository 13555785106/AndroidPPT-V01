package com.telecom.nerdlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

    static Bitmap drawableToBitmap(Drawable drawable,DisplayMetrics displayMetrics)// drawable 转换成bitmap
    {
        float width = drawable.getIntrinsicWidth();
        float height = drawable.getIntrinsicHeight();
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, displayMetrics);
        height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height,displayMetrics);
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, (int) width, (int) height);
        drawable.draw(canvas);
        return bitmap;
    }

    static Drawable zoomDrawable(Drawable drawable, float w, float h,DisplayMetrics displayMetrics) {//w、h的单位为dp
        Bitmap oldbmp = drawableToBitmap(drawable,displayMetrics);
        float width = drawable.getIntrinsicWidth();
        float height = drawable.getIntrinsicHeight();
        Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象
        float scaleWidth = w / width;   // 计算缩放比例
        float scaleHeight = h / height;
        matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, displayMetrics);
        height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, displayMetrics);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, (int)width, (int)height, matrix, true); // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回
    }

}
