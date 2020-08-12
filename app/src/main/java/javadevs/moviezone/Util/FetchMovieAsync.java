package javadevs.moviezone.Util;

import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javadevs.moviezone.BuildConfig;
import javadevs.moviezone.Interface.MovieCallBack;
import javadevs.moviezone.MainActivity;
import javadevs.moviezone.model.Movie;

/**
 * Created by CHUKWU JOHNPAUL on 16/04/17.
 * Class extends AsyncTask CLass and is used to fetch Movie data from the specified url
 */

public class FetchMovieAsync extends AsyncTask<String,Void,Movie[]> {
    private final MovieCallBack movieCallBack;
    ProgressBar mProgressBar;
    TextView mTextView;
    GridView mGridview;
    Button mButton;

    public FetchMovieAsync(MovieCallBack movieTaskCallback) {
        this.movieCallBack = movieTaskCallback;
    }

    private Movie[] getJsonResult(String response ) throws JSONException{
        if (response == null || "".equals(response)) {
            return null;
        }
        JSONObject mJsonObject = new JSONObject(response);
        JSONArray mJsonArray = mJsonObject.getJSONArray("results");
        Movie[] movieList = new Movie[mJsonArray.length()];
        for(int i=0;i<mJsonArray.length();i++){
            JSONObject singleMovieItem = mJsonArray.getJSONObject(i);
            movieList[i] = new Movie(
                    singleMovieItem.getInt("id"),
                    singleMovieItem.getString("title"),
                    singleMovieItem.getString("poster_path"),
                    singleMovieItem.getString("overview"),
                    singleMovieItem.getDouble("vote_average"),
                    singleMovieItem.getString("release_date"),
                    singleMovieItem.getString("original_language"),
                    singleMovieItem.getString("backdrop_path")

            );


        }
        return movieList;
    }

    public void showProgressBar(ProgressBar mProgressBar){
        this.mProgressBar = mProgressBar;
        mProgressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgressBar(ProgressBar mProgressBar){
        this.mProgressBar = mProgressBar;
        mProgressBar.setVisibility(View.GONE);
    }
    public void showTextView(TextView msg){
        this.mTextView = msg;
        mTextView.setVisibility(View.VISIBLE);
    }


    public void hideTextView(TextView msg){
        this.mTextView = msg;
        mTextView.setVisibility(View.GONE);
    }
    public void showGridView(GridView grid){
        this.mGridview = grid;
        mGridview.setVisibility(View.VISIBLE);
    }
    public void showBtn(Button btn){
        this.mButton = btn;
        mButton.setVisibility(View.VISIBLE);
    }


    public void hideBtn(Button btn){
        this.mButton = btn;
        mButton.setVisibility(View.GONE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showProgressBar(MainActivity.pb_loading_indicator);
        hideTextView(MainActivity.empty_msg_txt);
        showGridView(MainActivity.mygridView);
        hideBtn(MainActivity.reloadbtn);
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        if(params.length==0){
            return null;
        }
        final String BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String API_KEY = "api_key";

        HttpURLConnection urlConnection = null;
        String MovieInJson = null;
        BufferedReader reader = null;

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(params[0])
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIEDB_API_KEY)
                .build();
        try {
            URL url = new URL(uri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            // set the connection timeout to 5 seconds and the read timeout to 10 seconds
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            MovieInJson = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            return getJsonResult(MovieInJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(Movie[] mymovies) {
        hideProgressBar(MainActivity.pb_loading_indicator);
        movieCallBack.updateData(mymovies);
    }


}
