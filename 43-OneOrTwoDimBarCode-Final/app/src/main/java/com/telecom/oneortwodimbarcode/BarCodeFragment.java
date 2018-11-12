package com.telecom.oneortwodimbarcode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileInputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class BarCodeFragment extends Fragment {
    GridLayout mGridLayout;
    Button mButtonScanOneDim;
    Button mButtonScanTwoDim;
    ImageView mImageViewBarCode;
    TextView mTextViewBarCode;
    public static BarCodeFragment newInstance() {
        return new BarCodeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bar_code, container, false);
        mGridLayout = v.findViewById(R.id.grid_layout);
        mTextViewBarCode = v.findViewById(R.id.text_view_bar_code);
        mImageViewBarCode = v.findViewById(R.id.image_view_bar_code);
        mButtonScanOneDim = v.findViewById(R.id.button_scan_one_dim);
        mButtonScanOneDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(BarCodeFragment.this)
                        .setCaptureActivity(CustomCaptureActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                        .setPrompt("请对准条形码")
                        .setBarcodeImageEnabled(true)
                        .initiateScan();
            }
        });

        mButtonScanTwoDim = v.findViewById(R.id.button_scan_two_dim);
        mButtonScanTwoDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(BarCodeFragment.this)
                        .setCaptureActivity(CustomCaptureActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                        .setPrompt("请对准二维码")
                        .setBarcodeImageEnabled(true)
                        .initiateScan();
            }
        });



        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode="+requestCode);
        System.out.println("resultCode="+resultCode);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        IntentUtils.printIntentInfo(data);
        if (result != null) {
            if (result.getContents() == null) {
                Snackbar.make(mGridLayout, "被取消", Snackbar.LENGTH_LONG).show();
            } else {
                String scanResult = result.getContents();
                String scanResultFormat = data.getExtras().getString("SCAN_RESULT_FORMAT");
                mTextViewBarCode.setText(scanResult);
                DisplayMetrics dm = getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                width = (int)(width*0.9);
                Bitmap bitmap = null;
                Bitmap logo = null;
                try {
                    logo = BitmapFactory.decodeResource(getResources(),R.drawable.me);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    if(scanResultFormat.equals("EAN_13") || scanResultFormat.equals("EAN_8"))
                        bitmap = barcodeEncoder.encodeBitmap(result.getContents(), BarcodeFormat.CODE_128, width, width/2);
                    if(scanResultFormat.equals("QR_CODE")) {
                        bitmap = barcodeEncoder.encodeBitmap(result.getContents(), BarcodeFormat.QR_CODE, width, width);
                        bitmap = ImageUtils.addLogoToQRCode(bitmap,logo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bitmap != null)
                    mImageViewBarCode.setImageBitmap(bitmap);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
