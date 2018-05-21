package com.udacity.gradle.builditbigger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.jokesandroidlibrary.JokesActivity;
//import com.example.android.jokesjavalibrary.JavaJokes;
import com.example.android.jokesjavalibrary.JavaJokes;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.jokesApi.JokesApi;
import com.udacity.gradle.builditbigger.backend.jokesApi.model.JokesBean;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String>{

    private static final int JOKES_FROM_GCE_LOADER = 111;
    private boolean buttonWasClicked = false;

    //Lifecycle methods
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Functional Methods
    public void tellJoke(View view) {
        buttonWasClicked = true;
        loadJokesFromGce();
    }
    private void loadJokesFromGce() {

        Toast.makeText(getApplicationContext(), "loading", Toast.LENGTH_SHORT).show();

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> WebSearchLoader = loaderManager.getLoader(JOKES_FROM_GCE_LOADER);
        if (WebSearchLoader == null) loaderManager.initLoader(JOKES_FROM_GCE_LOADER, null, this);
        else loaderManager.restartLoader(JOKES_FROM_GCE_LOADER, null, this);
    }
    private void showJokeInJokesActivity(String joke) {
        Intent intent = new Intent(this, JokesActivity.class);
        intent.putExtra(JokesActivity.JOKE_KEY, joke);
        startActivity(intent);
    }
    public boolean internetIsAvailable() {
        final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr==null) return false;
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return (activeNetworkInfo != null) ? true : false;
    }
    public void tellUserInternetIsUnavailable() {
        Looper.prepare();
        Toast.makeText(getApplicationContext(), R.string.failed_to_access_internet, Toast.LENGTH_SHORT).show();
    }

    //Loader methods
    @SuppressLint("StaticFieldLeak") @NonNull @Override public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<String>(getApplicationContext()) {

            private JokesApi myApiService = null;
            private Context context;

            @Override protected void onStartLoading() {
                //if (args == null) return;
                if (!internetIsAvailable()) tellUserInternetIsUnavailable();

                if (buttonWasClicked) {
                    buttonWasClicked = false;
                    forceLoad();
                }

            }
            @Override public String loadInBackground() {


                if(myApiService == null) {  // Only do this once
                    JokesApi.Builder builder = new JokesApi.Builder(AndroidHttp.newCompatibleTransport(),
                            new AndroidJsonFactory(), null)
                            // options for running against local devappserver
                            // - 10.0.2.2 is localhost's IP address in Android emulator
                            // - turn off compression when running against local devappserver
                            .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                            .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                                @Override
                                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                    abstractGoogleClientRequest.setDisableGZipContent(true);
                                }
                            });
                    // end options for devappserver

                    myApiService = builder.build();
                }

                //context = params[0].first;
                //String name = params[0].second;
                try {
                    JokesBean jokeFromJokesBean = new JokesBean();
                    return myApiService.tellJoke(jokeFromJokesBean).execute().getData();
                } catch (IOException e) {
                    return e.getMessage();
                }

                //return null;
            }
        };
    }
    @Override public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        showJokeInJokesActivity(data);
    }
    @Override public void onLoaderReset(@NonNull Loader<String> loader) {
        getLoaderManager().destroyLoader(loader.getId());
    }


}
