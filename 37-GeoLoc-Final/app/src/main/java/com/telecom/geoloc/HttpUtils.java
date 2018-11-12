package com.telecom.geoloc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaojf on 17/12/14.
 */

public final class HttpUtils {
    private HttpUtils(){}

    public static String getUrlString(String urlSpec){
        return getUrlString(urlSpec,null);
    }
    public static String getUrlString(String urlSpec, Map<String, String> params){
        return getUrlString(urlSpec,params,"UTF-8");
    }
    public static String getUrlString(String urlSpec, Map<String, String> params,String charsetName){
        return getUrlString(urlSpec,params,null,null,charsetName);
    }
    public static String getUrlString(String urlSpec, Map<String, String> params,Map<String, String> requestHeaders,Map<String, List<String>> responseHeaders, String charsetName){
        URL url = null;
        HttpURLConnection conn = null;
        try {
            if (params != null) {
                int i = 0;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (i == 0)
                        urlSpec += "?";
                    else
                        urlSpec += "&";
                    urlSpec += entry.getKey();
                    urlSpec += "=";
                    urlSpec += entry.getValue();
                    i++;
                }

            }
            url = new URL(urlSpec);
            conn = (HttpURLConnection)url.openConnection();
            if (requestHeaders != null) {
                int i = 0;
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    conn.setRequestProperty(entry.getKey(),entry.getValue());
                }

            }
            conn.connect();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                if(responseHeaders != null) {
                    for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet())
                        responseHeaders.put(entry.getKey(),entry.getValue());
                }

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream is = conn.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while( (bytesRead = is.read(buffer)) >0){
                    out.write(buffer,0,bytesRead);
                }
                out.close();
                return new String(out.toByteArray(),charsetName);
            }else
                System.out.println(conn.getResponseMessage());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(conn != null) conn.disconnect();
        }
        return null;
    }
}
