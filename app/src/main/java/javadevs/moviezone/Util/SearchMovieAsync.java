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
import javadevs.moviezone.Interface.SearchCallBack;
import javadevs.moviezone.MainActivity;
import javadevs.moviezone.model.Movie;

/**
 * Created by CHUKWU JOHNPAUL on 19/05/17.
 */


public class SearchMovieAsync extends AsyncTask<String,Void,Movie[]>{
    private static final String USERS_QUERY ="query" ;
    ProgressBar mProgressBar;
    SearchCallBack mSearchCallBack;
    TextView mTextView;
    GridView mGridview;
    Button mButton;
    public SearchMovieAsync(SearchCallBack searchTaskCallback) {
        this.mSearchCallBack = searchTaskCallback;
    }
    private Movie[] getJsonResult(String response ) throws JSONException {
        Movie[] movieList;
        if (response == null || "".equals(response)) {
            return null;
        }
        JSONObject mJsonObject = new JSONObject(response);
        JSONArray mJsonArray = mJsonObject.getJSONArray("results");
        if(mJsonArray.length()>0){
           movieList = new Movie[mJsonArray.length()];
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

        }
        else{
            return null;
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


    public void hideGridView(GridView grid){
        this.mGridview = grid;
        mGridview.setVisibility(View.GONE);
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
        hideGridView(MainActivity.mygridView);
        hideTextView(MainActivity.empty_msg_txt);

    }

    @Override
    protected Movie[] doInBackground(String... params) {
        if(params.length==0){
            return null;
        }
        final String BASE_URL = "https://api.themoviedb.org/3/search/movie";
        final String API_KEY = "api_key";

        HttpURLConnection urlConnection = null;
        String MovieInJson = null;
        BufferedReader reader = null;

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIEDB_API_KEY)
                .appendQueryParameter(USERS_QUERY,params[0])
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
        mSearchCallBack.searchData(mymovies);
        if(mymovies != null){
            showGridView(MainActivity.mygridView);
            hideTextView(MainActivity.empty_msg_txt);
        }else{
            hideGridView(MainActivity.mygridView);
            showTextView(MainActivity.empty_msg_txt);
            showBtn(MainActivity.reloadbtn);
        }


    }
}
