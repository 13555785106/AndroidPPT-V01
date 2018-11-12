package com.telecom.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.UUID;

/**
 * Created by xiaojf on 17/12/26.
 */

public class PhotoViewFragment extends DialogFragment {
    private static final String ARG_CRIME_ID = "crime_id";
    private ImageView mPhotoView;
    private File mPhotoFile = null;

    public static PhotoViewFragment newInstance(UUID id) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, id);
        PhotoViewFragment fragment = new PhotoViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        UUID id = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        Crime crime = crimeLab.getCrime(id);
        mPhotoFile = crimeLab.getPhotoFile(crime);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_photo, null);
        mPhotoView = v.findViewById(R.id.dialog_photo_view);
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("图片预览")
                .setPositiveButton("确定",null)
                .create();
    }

}
