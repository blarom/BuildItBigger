package com.udacity.gradle.builditbigger.backend;

import com.example.android.jokesjavalibrary.JavaJokes;

/** The object model for the data we are sending through endpoints */
public class JokesBean {

    private String myData;

    public String getData() {
        JavaJokes javaJokes = new JavaJokes();
        return javaJokes.getJoke();
    }

    public void setData(String data) {
        myData = data;
    }
}