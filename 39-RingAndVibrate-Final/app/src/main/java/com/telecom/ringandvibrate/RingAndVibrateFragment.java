package com.telecom.ringandvibrate;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class RingAndVibrateFragment extends Fragment {
    private static final String TAG = "RingAndVibrateFragment";

    private AudioManager mAudioManager;
    private Vibrator mVibrator;
    private Button mRingButton;
    private Button mVibrateButton;
    private VideoView mVideoView;
    public static RingAndVibrateFragment newInstance() {
        return new RingAndVibrateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mVibrator =  (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ring_and_vibrate, container, false);
        mRingButton = v.findViewById(R.id.ring_button);
        mRingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
                    try {
                        AssetFileDescriptor assetFileDescriptor = getActivity().getAssets().openFd("ring.mp3");
                        MediaPlayer mMediaPlayer = new MediaPlayer();
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                        assetFileDescriptor.close();
                        //MediaPlayer mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.ring);
                        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                System.out.println("-----");
                                mp.release();
                            }
                        });
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        mVibrateButton = v.findViewById(R.id.vibrate_button);
        mVibrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=26)
                    mVibrator.vibrate(VibrationEffect.createOneShot(1000,255));
                else
                    mVibrator.vibrate(1000);
            }
        });
        mVideoView = v.findViewById(R.id.video_view);
        MediaController mc = new MediaController(getActivity());
        mVideoView.setMediaController(mc);
        mVideoView.setVideoURI(Uri.parse("http://192.168.3.50:8080/PhotoGallery/1.avi"));
        mVideoView.start();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
