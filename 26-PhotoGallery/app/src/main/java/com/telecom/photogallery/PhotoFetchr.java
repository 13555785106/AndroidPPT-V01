package com.telecom.photogallery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static String getUrlString(String urlSpec, String charsetName) throws IOException {
        return new String(getUrlBytes(urlSpec), charsetName);
    }

    public static PhotoResult fetchPhotoItems(String urlSpec, String charsetName) {
        PhotoResult photoResult = new PhotoResult();
        try {
            String result = PhotoFetchr.getUrlString(urlSpec, charsetName);
            JSONObject jsonObject = new JSONObject(result);
            photoResult.setItemCount(jsonObject.getInt("itemCount"));
            photoResult.setPageCount(jsonObject.getInt("pageCount"));
            photoResult.setPageNo(jsonObject.getInt("pageNo"));
            photoResult.setPageSize(jsonObject.getInt("pageSize"));

            JSONArray jsonDatas = jsonObject.getJSONArray("datas");
            for (int i = 0; i < jsonDatas.length(); i++) {
                jsonObject = jsonDatas.getJSONObject(i);
                PhotoItem photoItem = new PhotoItem();
                photoItem.setId(jsonObject.getLong("id"));
                photoItem.setName(jsonObject.getString("name"));
                photoItem.setUrl(jsonObject.getString("url"));
                photoResult.getDatas().add(photoItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoResult;
    }
}
