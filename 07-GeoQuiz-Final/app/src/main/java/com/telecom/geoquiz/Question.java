package com.telecom.geoquiz;

/**
 * Created by xiaojf on 17/12/18.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mPassed;
    private boolean mCheated;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean isPassed() {
        return mPassed;
    }

    public void setPassed(boolean passed) {
        mPassed = passed;
    }

    public boolean isCheated() {
        return mCheated;
    }

    public void setCheated(boolean cheated) {
        mCheated = cheated;
    }

    @Override
    public String toString() {
        return "Question{" +
                "mTextResId=" + mTextResId +
                ", mAnswerTrue=" + mAnswerTrue +
                ", mPassed=" + mPassed +
                ", mCheated=" + mCheated +
                '}';
    }
}
