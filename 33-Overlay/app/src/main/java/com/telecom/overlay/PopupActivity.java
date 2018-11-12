package com.telecom.overlay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

public class PopupActivity extends AppCompatActivity implements View.OnTouchListener {
    PopupWindow mOverlay;
    Button mButtonClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        //填充弹出窗口内容布局，我们尚无权访问父视图，因此传递null作为容器视图参数
        View popupContent = getLayoutInflater().inflate(R.layout.popup, null);
        mButtonClose = popupContent.findViewById(R.id.button_close);
        mButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOverlay != null && mOverlay.isShowing()) {
                    mOverlay.dismiss();
                }
            }
        });
        mOverlay = new PopupWindow();
        //如果设置此类形式的宽高，只有点击上面的按钮才可弹出
        //mOverlay.setWindowLayoutMode(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //为弹出窗口设置固定宽高在底部才可弹出
        mOverlay.setWidth(getResources().getDimensionPixelOffset(R.dimen.popupWidth));
        mOverlay.setHeight(getResources().getDimensionPixelSize(R.dimen.popupHeight));
        mOverlay.setContentView(popupContent);
        mOverlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

        // 设置一个 弹入\弹出动画，或者0来禁用动画
        mOverlay.setAnimationStyle(R.style.PopupAnimation);

        // 默认情况弹出窗口内部的部件无法获得焦点，但是能够接收触摸事件。
        mOverlay.setTouchInterceptor(this);

        // 使弹出窗口内部的部件可获得焦点
        // 而且会使弹出窗口外部的触摸事件是弹出窗口关闭
        mOverlay.setFocusable(true);

        // 如果你想触摸弹出窗口外部释放弹出窗口而不使其内部部件获得焦点C
        mOverlay.setOutsideTouchable(true);


    }

    @Override
    protected void onPause() {
        super.onPause();
        //释放弹出窗口防止内存泄漏
        mOverlay.dismiss();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //处理在弹出窗口上的触摸事件
        return false;
    }

    public void onShowWindowClick(View v) {
        if (mOverlay.isShowing()) {
            mOverlay.dismiss();
        } else {
            // 当点击按钮时显示弹窗。如果有空间它显示在按钮下部，否则显示在上部。
            // 对于显示在锚点上部，这要求弹窗必须有固定的大小才行。
            mOverlay.showAsDropDown(v);
        }
    }

}
