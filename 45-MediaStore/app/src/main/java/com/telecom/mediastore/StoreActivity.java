package com.telecom.mediastore;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class StoreActivity extends Activity implements View.OnClickListener {

    private static final int REQUEST_CAPTURE = 100;
    private static final int REQUEST_DOCUMENT = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Button images = (Button) findViewById(R.id.imageButton);
        images.setOnClickListener(this);
        Button videos = (Button) findViewById(R.id.videoButton);
        videos.setOnClickListener(this);
        //仅在API19才支持创建新的文本文档
        Button text = (Button) findViewById(R.id.textButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            text.setOnClickListener(this);
        } else {
            text.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "All Done!", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == REQUEST_DOCUMENT && resultCode == Activity.RESULT_OK) {
            // 一旦用户选择了存放的位置我们就把内容写入这个文档
            Uri document = data.getData();
            writeDocument(document);
        }
    }

    private void writeDocument(Uri document) {
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(document, "w");
            FileOutputStream out = new FileOutputStream(pfd.getFileDescriptor());
            //为文件构造一些内容
            StringBuilder sb = new StringBuilder();
            sb.append("Android Recipes Log File:");
            sb.append("\n");
            sb.append("Last Written at: ");
            sb.append(DateFormat.getLongDateFormat(this).format(new Date()));
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            Log.w("AndroidRecipes", e);
        } catch (IOException e) {
            Log.w("AndroidRecipes", e);
        }
    }

    @Override
    public void onClick(View v) {
        ContentValues values;
        Intent intent;
        Uri storeLocation;
        final long nowMillis = System.currentTimeMillis();

        switch (v.getId()) {
            case R.id.imageButton:
                // 为图片创建一些元数据
                values = new ContentValues(5);
                values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, nowMillis);
                values.put(MediaStore.Images.ImageColumns.DATE_ADDED, nowMillis / 1000);
                values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, nowMillis / 1000);
                values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, "Android Recipes Image Sample");
                values.put(MediaStore.Images.ImageColumns.TITLE, "Android Recipes Image Sample");

                // 插入元数据并获取文件的Uri
                storeLocation = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                // 使用这个Uri来存储拍摄的图片
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, storeLocation);
                startActivityForResult(intent, REQUEST_CAPTURE);
                return;
            case R.id.videoButton:
                // 为视频创建一些元数据
                values = new ContentValues(7);
                values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, nowMillis);
                values.put(MediaStore.Video.VideoColumns.DATE_ADDED, nowMillis / 1000);
                values.put(MediaStore.Video.VideoColumns.DATE_MODIFIED, nowMillis / 1000);
                values.put(MediaStore.Video.VideoColumns.DISPLAY_NAME, "Android Recipes Video Sample");
                values.put(MediaStore.Video.VideoColumns.TITLE, "Android Recipes Video Sample");
                values.put(MediaStore.Video.VideoColumns.ARTIST, "Yours Truly");
                values.put(MediaStore.Video.VideoColumns.DESCRIPTION, "Sample Video Clip");

                // 插入元数据并获取文件的Uri
                storeLocation = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
                // 使用这个Uri来存储录像
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, storeLocation);
                startActivityForResult(intent, REQUEST_CAPTURE);
                return;
            case R.id.textButton:
                // 创建一个新文档
                intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                // 设置类型为文本文档
                intent.setType("text/plain");
                // 预先设置的文档标题
                intent.putExtra(Intent.EXTRA_TITLE, "Android Recipes");
                startActivityForResult(intent, REQUEST_DOCUMENT);
            default:
                return;
        }
    }
}
