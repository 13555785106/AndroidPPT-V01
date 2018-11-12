package com.telecom.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final String KEY_INDEX = "index";
    private static final String KEY_SCORE = "score";
    private static final String KEY_PASSEDVALUES = "passedValues";
    private static final String KEY_CHEATEDVALUES = "cheatedValues";
    private static final int REQUEST_CODE_CHEAT = 0;
    private int mMaxCheatedNum =3;
    private int mCurCheatedNum =0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mResetButton;
    private Button mCheatButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private TextView mScoreTextView;
    private NumberFormat mNumberFormat = NumberFormat.getNumberInstance();

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_beijing, true),
            new Question(R.string.question_shenyang, true)
    };
    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        mNumberFormat.setMaximumFractionDigits(2);
        setContentView(R.layout.activity_quiz);
        mScoreTextView = (TextView) findViewById(R.id.score_text_view);
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mScoreTextView.setText(mNumberFormat.format(savedInstanceState.getDouble(KEY_SCORE, 0)));
            boolean[] passedValues = savedInstanceState.getBooleanArray(KEY_PASSEDVALUES);
            boolean[] cheatedValues = savedInstanceState.getBooleanArray(KEY_CHEATEDVALUES);
            for (int i = 0; i < passedValues.length; i++) {
                mQuestionBank[i].setPassed(passedValues[i]);
                mQuestionBank[i].setCheated(cheatedValues[i]);
            }
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNextButton != null)
                    mNextButton.performClick();
            }
        });


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex--;
                if (mCurrentIndex < 0) mCurrentIndex += mQuestionBank.length;
                updateQuestion();
            }
        });
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = 0;
                for (Question q : mQuestionBank) {
                    q.setPassed(false);
                    q.setCheated(false);
                }
                updateQuestion();
                updateButtons();
                mScoreTextView.setText("0");
            }
        });
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setText(getString(R.string.cheat_button)+"(剩余" + (mMaxCheatedNum-mCurCheatedNum) + "次)");
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        updateButtons();
        mQuestionTextView.setText(question);
    }

    private void updateButtons() {
        if (mQuestionBank[mCurrentIndex].isPassed()) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
        } else {
            mTrueButton.setEnabled(true);
            mFalseButton.setEnabled(true);
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        mQuestionBank[mCurrentIndex].setPassed(true);
        updateButtons();
        int messageResId = 0;
        if (mQuestionBank[mCurrentIndex].isCheated()) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                double score = Double.parseDouble(mScoreTextView.getText().toString());
                score += 100.0 / mQuestionBank.length;

                mScoreTextView.setText(mNumberFormat.format(score));
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast toast = Toast.makeText(this, messageResId, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundResource(R.drawable.toast_background);
        toast.setGravity(Gravity.TOP,0,512);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT && data !=null) {
            mQuestionBank[mCurrentIndex].setCheated(CheatActivity.wasAnswerShown(data));
            mCurCheatedNum++;
            mCheatButton.setText(getString(R.string.cheat_button)+"(剩余" + (mMaxCheatedNum-mCurCheatedNum) + "次)");
            if(mCurCheatedNum>=mMaxCheatedNum)
                mCheatButton.setEnabled(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState called");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putDouble(KEY_SCORE, Double.parseDouble(mScoreTextView.getText().toString()));
        boolean[] passedValues = new boolean[mQuestionBank.length];
        boolean[] cheatedValues = new boolean[mQuestionBank.length];
        for (int i = 0; i < mQuestionBank.length; i++) {
            passedValues[i] = mQuestionBank[i].isPassed();
            cheatedValues[i] = mQuestionBank[i].isCheated();
        }
        outState.putBooleanArray(KEY_PASSEDVALUES, passedValues);
        outState.putBooleanArray(KEY_CHEATEDVALUES,cheatedValues);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() called");
    }
}
