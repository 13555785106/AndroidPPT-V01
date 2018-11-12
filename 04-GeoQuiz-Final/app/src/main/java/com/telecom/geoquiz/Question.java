package com.telecom.geoquiz;

/**
 * Created by xiaojf on 17/12/18.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mPassed;

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

    @Override
    public String toString() {
        return "Question{" +
                "mTextResId=" + getTextResId() +
                ", mAnswerTrue=" + isAnswerTrue() +
                ", mPassed=" + isPassed() +
                '}';
    }
}
