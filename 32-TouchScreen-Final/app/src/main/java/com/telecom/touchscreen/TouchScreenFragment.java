package com.telecom.touchscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

public class TouchScreenFragment extends Fragment implements View.OnClickListener {
    public static TouchScreenFragment newInstance() {
        return new TouchScreenFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_touch_screen, container, false);
        GridLayout mGridLayout = v.findViewById(R.id.gridlayout);
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            mGridLayout.getChildAt(i).setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button_1:
                intent = new Intent(getActivity(), MotionEventActivity.class);
                break;
            case R.id.button_2:
                intent = new Intent(getActivity(), GestureDetectorActivity.class);
                break;
            case R.id.button_3:
                intent = new Intent(getActivity(), ScaleGestureDetectorActivity.class);
                break;
            case R.id.button_4:
                intent = new Intent(getActivity(), PathLengthActivity.class);
                break;
            case R.id.button_5:
                intent = new Intent(getActivity(), RotateAngleActivity.class);
                break;
            case R.id.button_6:
                intent = new Intent(getActivity(), GlideImageActivity.class);
                break;
            default:
                break;
        }
        if (intent != null)
            startActivity(intent);
    }
}
