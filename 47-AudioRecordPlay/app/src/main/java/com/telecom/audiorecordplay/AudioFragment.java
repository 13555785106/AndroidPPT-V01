package com.telecom.audiorecordplay;


import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;

import java.io.File;


public class AudioFragment extends Fragment implements
        MediaController.MediaPlayerControl, MediaPlayer.OnBufferingUpdateListener {
    private final static String TAG = AudioFragment.class.getSimpleName();
    File mFile = new File(Environment.getExternalStorageDirectory(), TAG + ".mp4");
    ImageButton mImageButtonRecord;
    boolean mRecording = true;
    MediaController mController;
    MediaPlayer mPlayer;
    ImageView coverImage;
    int bufferPercent = 0;
    private MediaRecorder mMediaRecorder;
    private Thread mWorkerThread;
    private ProgressBar mProgressBarVolume;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int amplitude = msg.getData().getInt("amplitude", 0);
                Log.e(TAG, "" + amplitude);
                int percent = (int) (amplitude * 1.0f / Short.MAX_VALUE * 100);
                mProgressBarVolume.setProgress(percent);
            }
        }
    };

    public static AudioFragment newInstance() {
        AudioFragment fragment = new AudioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaRecorder = new MediaRecorder();
        mPlayer = new MediaPlayer();
        mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        int amplitude = mMediaRecorder.getMaxAmplitude();
                        Message msg = Message.obtain();
                        msg.what = 0;
                        msg.getData().putInt("amplitude", amplitude);

                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        mWorkerThread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_audio, container, false);
        mProgressBarVolume = v.findViewById(R.id.progress_bar_volume);
        mImageButtonRecord = v.findViewById(R.id.image_button_record);
        mImageButtonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRecording) {

                    mImageButtonRecord.setImageResource(R.drawable.stop);
                    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    mMediaRecorder.setOutputFile(mFile.getAbsolutePath());
                    try {
                        mMediaRecorder.prepare();
                        mMediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                 mController.hide();
                } else {
                    mImageButtonRecord.setImageResource(R.drawable.record);
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    mPlayer.reset();
                    //Set the audio data source
                    try {
                        mPlayer.setDataSource(mFile.getAbsolutePath());
                        mPlayer.prepare();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Set an image for the album cover
                    coverImage.setImageResource(R.mipmap.ic_launcher_round);
                    mController.setMediaPlayer(AudioFragment.this);
                    mController.setEnabled(true);
                    mController.show(0);
                }
                mRecording = !mRecording;
            }
        });

        coverImage = (ImageView) v.findViewById(R.id.coverImage);
        Log.e(TAG, "" + getActivity().toString());
        mController = new MediaController(getActivity());
        mController.setAnchorView(v.findViewById(R.id.media_controls));
        return v;
    }
    public void showMediaController(){
        mController.show(0);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

    }


    //MediaPlayerControl Methods
    @Override
    public int getBufferPercentage() {
        return bufferPercent;
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mPlayer.pause();
    }

    @Override
    public void seekTo(int pos) {
        mPlayer.seekTo(pos);
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    //BufferUpdateListener Methods
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        bufferPercent = percent;
    }

    //Android 2.0+ Target Callbacks
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    //Android 4.3+ Target Callbacks
    @Override
    public int getAudioSessionId() {
        return mPlayer.getAudioSessionId();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.release();
        mPlayer = null;
        mMediaRecorder.release();
        mMediaRecorder = null;
    }
}
/*

MediaRecorder recorder = new MediaRecorder();
 recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
 recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
 recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
 recorder.setOutputFile(PATH_NAME);
 recorder.prepare();
 recorder.start();   // Recording is now started
 ...
 recorder.stop();
 recorder.reset();   // You can reuse the object by going back to setAudioSource() step
 recorder.release(); // Now the object cannot be reused

 */