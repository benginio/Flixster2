package com.benginio.flixster2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Headers;

import android.os.Bundle;
import android.util.Log;

import com.benginio.flixster2.adapters.MovieAdapter;
import com.benginio.flixster2.models.Movie;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=56ac9f1b5bc39965643295e82c9fb468";
    public static final String TAG="MainActivity";

    List<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // getSupportActionBar().hide();//for hide the actionbar
        RecyclerView rvMovies=findViewById(R.id.rvMovies);
        movies=new ArrayList<>();
        //Create the adapter
         MovieAdapter movieAdapter=new MovieAdapter(this, movies);
        //set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
        //set a layout Manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient client=new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results=jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: "+results.toString());
                     movies.addAll(Movie.fromJsonArray(results));
                     movieAdapter.notifyDataSetChanged();
                     Log.i(TAG, "Movies: "+movies.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "HIT JSON EXCEPTION");
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }
}