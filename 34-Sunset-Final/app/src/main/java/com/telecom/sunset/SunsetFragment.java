package com.telecom.sunset;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class SunsetFragment extends Fragment {
    private View mSceneView;
    private ImageView mSunView;
    private View mSkyView;
    private View mSeaView;
    private ImageView mSunInvertedView;

    private int mBrightSunColor;
    private int mInvertedSunColor;
    private int mDarkSunColor;


    private int mBlueSkyColor;
    private int mBlueSeaColor;
    private int mSunsetSkyColor;
    private int mSunsetSeaColor;
    private int mNightSkyColor;
    private int mNightSeaColor;

    private boolean isAnimating = false;
    private boolean isSunSetting = false;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);
        Resources resources = getResources();

        mBrightSunColor = resources.getColor(R.color.bright_sun);
        mInvertedSunColor = resources.getColor(R.color.inverted_sun);
        mDarkSunColor = resources.getColor(R.color.dark_sun);

        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mBlueSeaColor = resources.getColor(R.color.blue_sea);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mSunsetSeaColor = resources.getColor(R.color.sunset_sea);
        mNightSkyColor = resources.getColor(R.color.night_sky);
        mNightSeaColor = resources.getColor(R.color.night_sea);

        mSceneView = view;
        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimating) {
                    isSunSetting = !isSunSetting;
                    if (isSunSetting)
                        startSunSettingAnimation();
                    else
                        startSunRisingAnimation();

                } else
                    Toast.makeText(getActivity(), "当前动画还未结束，请等待！", Toast.LENGTH_SHORT).show();
            }
        });

        mSunView = view.findViewById(R.id.sun);
        mSunInvertedView = view.findViewById(R.id.sun_inverted);
        mSkyView = view.findViewById(R.id.sky);
        mSeaView = view.findViewById(R.id.sea);

        return view;
    }

    private void startSunSettingAnimation() {
        ObjectAnimator sunColorAnimator = ObjectAnimator
                .ofInt(mSunView, "colorFilter", mBrightSunColor, mDarkSunColor)
                .setDuration(3200);
        sunColorAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunScaleXAnimator = ObjectAnimator
                .ofFloat(mSunView, "scaleX", 1f, 1.5f)
                .setDuration(3200);

        ObjectAnimator sunScaleYAnimator = ObjectAnimator
                .ofFloat(mSunView, "scaleY", 1f, 1.5f)
                .setDuration(3200);

        ObjectAnimator sunInvertedColorAnimator = ObjectAnimator
                .ofInt(mSunInvertedView, "colorFilter", mInvertedSunColor, mDarkSunColor)
                .setDuration(3200);
        sunInvertedColorAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunInvertedScaleXAnimator = ObjectAnimator
                .ofFloat(mSunInvertedView, "scaleX", 1f, 1.5f)
                .setDuration(3200);

        ObjectAnimator sunInvertedScaleYAnimator = ObjectAnimator
                .ofFloat(mSunInvertedView, "scaleY", 1f, 1.5f)
                .setDuration(3200);

        ObjectAnimator sunHeightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", mSunView.getTop(), mSkyView.getHeight()*1.5f)
                .setDuration(3200);
        sunHeightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunInvertedHeightAnimator = ObjectAnimator
                .ofFloat(mSunInvertedView, "y", mSunInvertedView.getTop(), 0 - mSunInvertedView.getHeight()*1.5f)
                .setDuration(3200);
        sunInvertedHeightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());


        ObjectAnimator sunsetSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mBlueSeaColor, mSunsetSeaColor)
                .setDuration(3000);
        sunsetSeaAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mNightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mSunsetSeaColor, mNightSeaColor)
                .setDuration(1500);
        nightSeaAnimator.setEvaluator(new ArgbEvaluator());


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(sunColorAnimator)
                .with(sunScaleXAnimator)
                .with(sunScaleYAnimator)
                .with(sunInvertedColorAnimator)
                .with(sunInvertedScaleXAnimator)
                .with(sunInvertedScaleYAnimator)
                .with(sunHeightAnimator)
                .with(sunInvertedHeightAnimator)
                .with(sunsetSkyAnimator)
                .with(sunsetSeaAnimator)
                .before(nightSkyAnimator)
                .before(nightSeaAnimator);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    private void startSunRisingAnimation() {
        ObjectAnimator sunColorAnimator = ObjectAnimator
                .ofInt(mSunView, "colorFilter", mDarkSunColor, mBrightSunColor)
                .setDuration(3200);
        sunColorAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunScaleXAnimator = ObjectAnimator
                .ofFloat(mSunView, "scaleX", 1.5f, 1f)
                .setDuration(3200);

        ObjectAnimator sunScaleYAnimator = ObjectAnimator
                .ofFloat(mSunView, "scaleY", 1.5f, 1f)
                .setDuration(3200);

        ObjectAnimator sunInvertedColorAnimator = ObjectAnimator
                .ofInt(mSunInvertedView, "colorFilter", mDarkSunColor, mInvertedSunColor)
                .setDuration(3200);
        sunInvertedColorAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator sunInvertedScaleXAnimator = ObjectAnimator
                .ofFloat(mSunInvertedView, "scaleX", 1.5f, 1f)
                .setDuration(3200);

        ObjectAnimator sunInvertedScaleYAnimator = ObjectAnimator
                .ofFloat(mSunInvertedView, "scaleY", 1.5f, 1f)
                .setDuration(3200);

        ObjectAnimator sunHeightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", mSkyView.getHeight(), mSunView.getTop())
                .setDuration(3200);
        sunHeightAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator sunInvertedHeightAnimator = ObjectAnimator
                .ofFloat(mSunInvertedView, "y", 0 - mSunInvertedView.getHeight(), mSunInvertedView.getTop())
                .setDuration(3200);
        sunInvertedHeightAnimator.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());


        ObjectAnimator sunsetSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mSunsetSeaColor, mBlueSeaColor)
                .setDuration(3000);
        sunsetSeaAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", mNightSkyColor, mSunsetSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSeaAnimator = ObjectAnimator
                .ofInt(mSeaView, "backgroundColor", mNightSeaColor, mSunsetSeaColor)
                .setDuration(1500);
        nightSeaAnimator.setEvaluator(new ArgbEvaluator());


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(sunColorAnimator)
                .with(sunScaleXAnimator)
                .with(sunScaleYAnimator)
                .with(sunInvertedColorAnimator)
                .with(sunInvertedScaleXAnimator)
                .with(sunInvertedScaleYAnimator)
                .with(sunHeightAnimator)
                .with(sunInvertedHeightAnimator)
                .with(sunsetSkyAnimator)
                .with(sunsetSeaAnimator)
                .after(nightSkyAnimator)
                .after(nightSeaAnimator);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
}
