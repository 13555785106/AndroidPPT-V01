package com.telecom.beatbox;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.telecom.beatbox.databinding.FragmentBeatBoxBinding;
import com.telecom.beatbox.databinding.ListItemSoundBinding;

import java.util.List;

/**
 * Created by xiaojf on 18/1/3.
 */

public class BeatBoxFragment extends Fragment {
    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beat_box, container, false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundImageAdapter(mBeatBox.getSoundImages()));
        binding.soundRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.soundRateTextView.setText("速率:"+(5+progress)/10.0f);
                mBeatBox.setSoundRate((5+progress)/10.0f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.soundRate.setProgress(0);
        binding.soundRate.setMax(15);


        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    private class SoundImageHolder extends RecyclerView.ViewHolder {
        private ListItemSoundBinding mBinding;

        private SoundImageHolder(ListItemSoundBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new SoundImageViewModel(mBeatBox));

        }

        public void bind(SoundImage soundImage) {
            mBinding.getViewModel().setSoundImage(soundImage);
            mBinding.executePendingBindings();
        }
    }

    private class SoundImageAdapter extends RecyclerView.Adapter<SoundImageHolder> {
        private List<SoundImage> mSoundImages;

        public SoundImageAdapter(List<SoundImage> soundImages) {
            mSoundImages = soundImages;
        }

        @Override
        public SoundImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item_sound, parent, false);
            return new SoundImageHolder(binding);
        }

        @Override
        public void onBindViewHolder(SoundImageHolder holder, int position) {
            SoundImage soundImage = mSoundImages.get(position);
            holder.bind(soundImage);
        }

        @Override
        public int getItemCount() {
            return mSoundImages.size();
        }
    }

}
