package com.telecom.photogallery;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojf on 18/1/7.
 */

public final class PhotoFetchr {
    private PhotoFetchr() {
    }

    public static byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public static String getUrlString(String urlSpec,String charsetName) throws IOException {
        return new String(getUrlBytes(urlSpec),charsetName);
    }

    public static PhotoResult fetchPhotoItems(String urlSpec,String charsetName){
        try {
            String result = PhotoFetchr.getUrlString(urlSpec,charsetName);
            return new Gson().fromJson(result,new TypeToken<PhotoResult>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
