package javadevs.moviezone.Util;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javadevs.moviezone.Interface.MovieCallBack;
import javadevs.moviezone.Movie;

/**
 * Created by CHUKWU JOHNPAUL on 16/04/17.
 */

public class FetchMovieAsync extends AsyncTask<String,Void,Movie[]> {
    private final MovieCallBack movieCallBack;
    public final String myApiKey="YOUR_KEY";
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
                    singleMovieItem.getString("original_title"),
                    singleMovieItem.getString("poster_path"),
                    singleMovieItem.getString("overview"),
                    singleMovieItem.getString("vote_average"),
                    singleMovieItem.getString("release_date")
            );

        }
        return movieList;
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
                .appendQueryParameter(API_KEY,myApiKey)
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
        movieCallBack.updateData(mymovies);
    }


}
