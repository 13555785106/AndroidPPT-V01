package com.telecom.beatbox;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by xiaojf on 18/1/4.
 */
public class SoundImageViewModelTest {
    private BeatBox mBeatBox;
    private SoundImage mSoundImage;
    private SoundImageViewModel mSubject;
    @Before
    public void setUp() throws Exception {
        mBeatBox = mock(BeatBox.class);
        mSoundImage = new SoundImage("soundPath","imagePath","name");
        mSubject = new SoundImageViewModel(mBeatBox);
        mSubject.setSoundImage(mSoundImage);
    }
    @Test
    public void exposesSoundNameAsTitle() {
        assertThat(mSubject.getTitle(), is(mSoundImage.getName()));
    }
    @Test
    public void callsBeatBoxPlayOnButtonClicked() {
        mSubject.onButtonClicked();
        verify(mBeatBox).play(mSoundImage);
    }
}