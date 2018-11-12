package com.telecom.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

public class SelectPhoneNumDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    ArrayList<String> mPhoneNums;
    private int selectIndex = 0;
    public static SelectPhoneNumDialogFragment newInstance(ArrayList<String> phoneNums) {
        SelectPhoneNumDialogFragment spndf = new SelectPhoneNumDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("phone_nums", phoneNums);
        spndf.setArguments(bundle);
        return spndf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*this.setCancelable(true);
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);*/
        mPhoneNums = getArguments().getStringArrayList("phone_nums");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setTitle("请选择电话号码：")
                .setPositiveButton("拨出",this)
                .setNegativeButton("关闭", this)
                .setIcon(R.drawable.phone_32)
                .setSingleChoiceItems(mPhoneNums.toArray(new String[]{}),0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectIndex = which;
                    }
                });
        return b.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            if (getTargetFragment() == null) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("phone_num", mPhoneNums.get(selectIndex));
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
    }
}