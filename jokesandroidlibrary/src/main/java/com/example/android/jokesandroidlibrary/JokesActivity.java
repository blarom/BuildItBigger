package com.example.android.jokesandroidlibrary;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class JokesActivity extends AppCompatActivity {

    public static final String JOKE_KEY = "jokes_android_library_joke_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);

        String joke = "";
        if (getIntent().hasExtra(JOKE_KEY)) {
            joke = getIntent().getStringExtra(JOKE_KEY);
            TextView jokeTextView = findViewById(R.id.joke_textview);
            if (!TextUtils.isEmpty(joke)) jokeTextView.setText(joke);
        }

        returnJoke(joke);
    }

    private TestCallback mCallback;

    public void setCallback(TestCallback callback){
        mCallback = callback;
    }

    public interface TestCallback{
        void onReturnJokeCalled(String joke);
    }

    @VisibleForTesting
    public void returnJoke(String joke) {
        if (mCallback!=null) mCallback.onReturnJokeCalled(joke);
    }
}
