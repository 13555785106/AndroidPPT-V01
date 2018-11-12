package com.telecom.oneortwodimbarcode;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by xiaojf on 18/6/4.
 */

public final class ImageUtils {


    private ImageUtils() {
    }

    public static Bitmap addLogoToQRCode(Bitmap src, Bitmap logo) {
        if (src == null)
            return null;

        if (logo == null)
            return src;

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0)
            return null;

        if (logoWidth == 0 || logoHeight == 0)
            return src;

        float scaleFactor = srcWidth * 1.0f / 8 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }
}
