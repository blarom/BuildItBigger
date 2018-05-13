package com.udacity.gradle.builditbigger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestMainActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private TestCallback mCallback;

    public void setCallback(TestCallback callback){
        mCallback = callback;
    }

    public interface TestCallback{
        void onHandleGceResponseCalled(String data);
    }

    @Override
    public void handleGceResponse(String data) {
        mCallback.onHandleGceResponseCalled(data);
    }
}
