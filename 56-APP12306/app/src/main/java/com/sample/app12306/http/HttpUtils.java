package com.sample.app12306.http;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 此类主要封装了一个用来发送HTTP请求的函数,支持GET或POST方法，也支持文件的上传。 发送文件时，如果提供了回调，可监控文件上传的进度。
 * 对于远程服务器的回应，本函数提供了回调可由用户决定如何处理这些数据。 肖俊峰 2018-9-15
 */
public final class HttpUtils {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    static {
        CookieHandler.setDefault(new CookieManager());
    }

    private HttpUtils() {
    }

    /**
     * 以 UTF-8编码向远程的Web服务器发送POST请求， 返回的数据是 UTF-8 编码的字符串。
     *
     * @param urlSpec    请求地址。
     * @param textFields 文本域参数。
     * @return 返回UTF-8 编码的字符串。
     */

    public static String utf8PostRequest(String urlSpec, Map<String, List<String>> textFields) {
        return sendUtf8Request(METHOD_POST, urlSpec, textFields);
    }

    /**
     * 以 UTF-8编码向远程的Web服务器发送GET请求， 返回的数据是 UTF-8 编码的字符串。
     *
     * @param urlSpec    请求地址。
     * @param textFields 文本域参数。
     * @return 返回 UTF-8 编码的字符串。
     */
    public static String utf8GetRequest(String urlSpec, Map<String, List<String>> textFields) {
        return sendUtf8Request(METHOD_GET, urlSpec, textFields);
    }

    /**
     * 这个方法用来向远程的Web服务器以 UTF-8 发送GET或者POST请求，只能发送文本域参数，不包含文件域。 返回 UTF-8 编码的字符串。
     *
     * @param method     请求方法，必须为 GET 或者 POST。
     * @param urlSpec    请求地址。
     * @param textFields 文本域参数。
     * @return 返回值是UTF-8 编码转换成的字符串。
     */
    public static String sendUtf8Request(String method, String urlSpec, Map<String, List<String>> textFields) {
        return sendRequest(method, urlSpec, textFields, "UTF-8");
    }

    /**
     * 这个方法用来向远程的Web服务器以指定编码charsetName发送GET或者POST请求，只能发送文本域参数，不包含文件域。 返回
     * charsetName 编码的字符串。
     *
     * @param method     请求方法，必须为 GET 或者 POST。
     * @param urlSpec    请求地址。
     * @param textFields 文本域参数。
     * @return 返回值是UTF-8 编码转换成的字符串。
     */
    public static String sendRequest(String method, String urlSpec, Map<String, List<String>> textFields,
                                     String charsetName) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        sendRequest(method, urlSpec, textFields, charsetName, new HttpDownloadProgress() {
            @Override
            public void onStart(String fileName, int length) {
            }

            @Override
            public void onProgress(byte[] bytes, int count) {
                baos.write(bytes, 0, count);
            }

            @Override
            public void onFinish() {
            }
        });
        String str = null;
        try {
            str = new String(baos.toByteArray(), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 这个方法用来向远程的Web服务器发送GET或者POST请求。
     *
     * @param method               请求方法，必须为 GET 或者 POST。
     * @param urlSpec              请求地址。
     * @param textFields           文本域参数。
     * @param charsetName          字符集名称,例如 UTF-8、ISO-8859-1等。
     * @param httpDownloadProgress 处理返回数据时的回调。
     * @throws IllegalArgumentException 设置了无效的方法时，或者其它参数不匹配.
     * @throws IOException              读写数据时的各种IO错误。
     */
    public static void sendRequest(String method, String urlSpec, Map<String, List<String>> textFields,
                                   String charsetName, HttpDownloadProgress httpDownloadProgress) {
        sendRequest(method, urlSpec, textFields, null, 8192, false, charsetName, null, httpDownloadProgress);
    }

    /**
     * 这个方法用来向远程的Web服务器发送GET或者POST请求。
     *
     * @param method             请求方法，必须为 GET 或者 POST。
     * @param urlSpec            请求地址。
     * @param textFields         文本域参数。
     * @param fileFields         文件域参数。
     * @param bufferLength       上传文件时使用缓存大小，默认8192字节，必须大于0此设置才会有效。
     * @param isMultiPart        是否是 multipart/form-data
     *                           类型，默认为false。只有method为POST时此设置有效。
     * @param charsetName        字符集名称,例如 UTF-8、ISO-8859-1等。
     * @param httpUploadProgress 上传文件时使用的回调。
     * @param httpUploadProgress 处理返回数据时的回调。
     * @throws IllegalArgumentException 设置了无效的方法时，或者其它参数不匹配.
     * @throws IOException              读写数据时的各种IO错误。
     */
    public static void sendRequest(String method, String urlSpec, Map<String, List<String>> textFields,
                                   Map<String, List<File>> fileFields, int bufferLength, boolean isMultiPart, String charsetName,
                                   HttpUploadProgress httpUploadProgress, HttpDownloadProgress httpDownloadProgress) {

        if (method == null)
            method = "";
        method = method.toUpperCase();
        if (!method.equals(METHOD_GET) && !method.equals(METHOD_POST))
            throw new IllegalArgumentException("无效的请求方法，必须是 GET 或 POST");
        if (isMultiPart && method.equals(METHOD_GET))
            throw new IllegalArgumentException("上传文件时，方法必须是 POST");
        if (bufferLength <= 0)
            bufferLength = 8192;
        if (charsetName == null)
            charsetName = "UTF-8";
        String boundary = null;
        if (isMultiPart)
            boundary = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        URL url = null;
        HttpURLConnection httpURLConnection = null;

        try {
            StringBuilder sbQueryString = new StringBuilder();
            if (textFields != null) {
                for (Map.Entry<String, List<String>> entry : textFields.entrySet()) {
                    String key = entry.getKey();
                    for (String val : entry.getValue()) {
                        sbQueryString.append(key);
                        sbQueryString.append("=");
                        sbQueryString.append(URLEncoder.encode(val, charsetName));
                        sbQueryString.append("&");
                    }
                }
            }
            if (method.equals(METHOD_GET) && sbQueryString.length() > 0)
                urlSpec += "?" + sbQueryString.toString();
            url = new URL(urlSpec);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            if (method.equals(METHOD_GET)) {
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else if (method.equals(METHOD_POST)) {
                httpURLConnection.setDoOutput(true);
                if (isMultiPart) {
                    httpURLConnection.setChunkedStreamingMode(0);
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                } else
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            }
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod(method.toUpperCase());
            httpURLConnection.setRequestProperty("Charset", charsetName);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接

            if (method.equals(METHOD_POST)) {
                DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
                if (isMultiPart) {
                    if (textFields != null) {
                        for (Map.Entry<String, List<String>> entry : textFields.entrySet()) {
                            String key = entry.getKey();
                            for (String val : entry.getValue()) {
                                dos.writeBytes("--" + boundary + "\r\n");
                                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n");
                                dos.write(val.getBytes(charsetName));
                                dos.writeBytes("\r\n");
                            }
                        }
                    }
                    if (fileFields != null) {
                        byte[] buffer = new byte[bufferLength];
                        for (Map.Entry<String, List<File>> entry : fileFields.entrySet()) {
                            String key = entry.getKey();
                            for (File file : entry.getValue()) {
                                dos.writeBytes("--" + boundary + "\r\n");
                                dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\""
                                        + new String(file.getName().getBytes(charsetName), "ISO-8859-1") + "\"\r\n");
                                dos.writeBytes("Content-Type: application/octet-stream" + "\r\n\r\n");
                                int count = 0;
                                FileInputStream fis = null;
                                try {
                                    if (httpUploadProgress != null)
                                        httpUploadProgress.onStart(file);
                                    fis = new FileInputStream(file);
                                    while ((count = fis.read(buffer)) > -1) {
                                        dos.write(buffer, 0, count);
                                        if (httpUploadProgress != null)
                                            httpUploadProgress.onProgress(file, count);
                                    }
                                    fis.close();
                                    if (httpUploadProgress != null)
                                        httpUploadProgress.onFinish(file);
                                    dos.writeBytes("\r\n");
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                } finally {
                                    if (fis != null)
                                        fis.close();
                                }
                            }
                        }
                    }
                    if ((textFields != null && textFields.size() > 0) || (fileFields != null && fileFields.size() > 0))
                        dos.writeBytes("--" + boundary + "--\r\n");

                } else {
                    if (sbQueryString.length() > 0) {
                        dos.write(sbQueryString.toString().getBytes(charsetName));
                    }
                }

                dos.flush();
                dos.close();
            }

            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(httpURLConnection.getResponseMessage() + ": with " + urlSpec);
            } else {
                /*
                 * for (Map.Entry<String, List<String>> entry :
				 * httpURLConnection.getHeaderFields().entrySet()) {
				 * System.out.println(entry.getKey() + "=" + entry.getValue().get(0)); }
				 */
                if (httpDownloadProgress != null) {
                    String charset = httpURLConnection.getHeaderField("Content-Type");
                    if (charset != null) {
                        int pos = charset.indexOf('=');
                        if (pos != -1)
                            charset = charset.substring(pos + 1);
                    }
                    String fileName = httpURLConnection.getHeaderField("Content-Disposition");
                    if (fileName != null) {
                        int pos = fileName.indexOf('=');
                        if (pos != -1) {
                            fileName = fileName.substring(pos + 1);
                            if (charset != null) {
                                fileName = new String(fileName.getBytes("ISO-8859-1"), charset);
                            }
                        }
                    }
                    httpDownloadProgress.onStart(fileName, httpURLConnection.getContentLength());
                    InputStream in = httpURLConnection.getInputStream();
                    int bytesRead = 0;
                    byte[] buffer = new byte[1024];
                    while ((bytesRead = in.read(buffer)) > 0) {
                        httpDownloadProgress.onProgress(buffer, bytesRead);
                    }
                    in.close();
                    httpDownloadProgress.onFinish();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // printCharSets();
        // testUtf8Request();
        testSendRequest();
    }

    public static void printCharSets() {
        for (Map.Entry<String, Charset> entry : Charset.availableCharsets().entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    public static void testUtf8Request() {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        params.put("account", Arrays.asList(new String[]{"a001"}));
        params.put("passwd", Arrays.asList(new String[]{"1234"}));
        params.put("confirmPasswd", Arrays.asList(new String[]{"1234"}));
        params.put("birthday", Arrays.asList(new String[]{"2018-9-15"}));
        params.put("sex", Arrays.asList(new String[]{"男"}));
        params.put("salary", Arrays.asList(new String[]{"4000"}));
        params.put("hobbies", Arrays.asList(new String[]{"足球", "篮球", "排球"}));
        // 通过GET方式发送请求
        System.out.println(sendUtf8Request("GET", "http://localhost:9090/WebSample/sample/ex01/AddUser", params));
        // 通过POST方式发送请求
        System.out.println(sendUtf8Request("POST", "http://localhost:9090/WebSample/sample/ex01/AddUser", params));
    }

    public static void testSendRequest() {
        Map<String, List<File>> files = new HashMap<String, List<File>>();
        files.put("file", Arrays.asList(new File[]{new File("/Users/xiaojf/Desktop/img/3D-Eagle-icon.png")}));
        // 上传一个文件
        sendRequest("POST", "http://localhost:9090/WebSample/sample/updownload/UploadFile", null, files, 8192, true,
                null, new HttpUploadProgress() {
                    long fileSize = 0;

                    @Override
                    public void onStart(File file) {
                        fileSize = 0;
                    }

                    @Override
                    public void onProgress(File file, int increment) {
                        fileSize += increment;
                        System.out.println(file + " 已上传 " + fileSize);

                    }

                    @Override
                    public void onFinish(File file) {

                    }
                }, new HttpDownloadProgress() {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    @Override
                    public void onStart(String fileName, int length) {
                        System.out.println(fileName);
                        System.out.println(length);
                    }

                    @Override
                    public void onProgress(byte[] bytes, int count) {
                        baos.write(bytes, 0, count);
                    }

                    @Override
                    public void onFinish() {
                        try {
                            System.out.println(new String(baos.toByteArray(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // 下载一个文件
        sendRequest("GET", "http://localhost:9090/WebSample/sample/updownload/DownloadFile?id=101", null, null, 0,
                false, null, null, new HttpDownloadProgress() {
                    FileOutputStream fos = null;

                    @Override
                    public void onStart(String fileName, int length) {
                        if (fileName != null) {
                            try {
                                File file = new File(fileName);
                                System.out.println("文件保存在 " + file.getCanonicalPath());
                                fos = new FileOutputStream(file);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onProgress(byte[] bytes, int count) {
                        if (fos != null)
                            try {
                                fos.write(bytes, 0, count);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }

                    @Override
                    public void onFinish() {
                        if (fos != null)
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                });
    }
}
