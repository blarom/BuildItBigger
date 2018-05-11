package com.example.android.jokesandroidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokesActivity extends AppCompatActivity {

    public static final String JOKE_KEY = "jokes_android_library_joke_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);

        String joke = getIntent().getStringExtra(JOKE_KEY);
        TextView jokeTextView = findViewById(R.id.joke_textview);
        if (joke != null && joke.length() != 0) jokeTextView.setText(joke);
    }
}
