package javadevs.moviezone.Util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javadevs.moviezone.BuildConfig;
import javadevs.moviezone.Interface.TrailerCallBack;
import javadevs.moviezone.MainActivity;
import javadevs.moviezone.NetworkUtil;
import javadevs.moviezone.model.Trailer;

/**
 * Created by CHUKWU JOHNPAUL on 14/05/17.
 */

public class FetchMovieTrailerAsync extends AsyncTask<Integer,Void,Trailer[]> {
    private final Context ctx;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    int count = 0;
        private final TrailerCallBack trailerTaskCallback;

    @Override
    protected void onPreExecute() {
        mProgressBar = new ProgressBar(ctx);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(6);
        mProgressBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    public  FetchMovieTrailerAsync(TrailerCallBack trailerTaskCallback,Context ctx) {
        this.trailerTaskCallback = trailerTaskCallback;
        this.ctx = ctx;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        mProgressBar.setProgress(count);
        count++;
    }

    @Override
    protected Trailer[] doInBackground(Integer... integers) {
        if (integers.length == 0) {
            return null;
        }

        final String BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String TYPE = "videos";
        final String API_KEY = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(String.valueOf(integers[0]))
                .appendEncodedPath(TYPE)
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIEDB_API_KEY)
                .build();

        String jsonString = NetworkUtil.getJsonString(uri);

        try {
            return getTrailersFromJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Trailer[] getTrailersFromJson(String jsonString) throws JSONException {
        final String TRAILER_ID = "id";
        final String TRAILER_NAME = "name";
        final String TRAILER_KEY = "key";
        final String RESULT_ARRAY = "results";

        if (jsonString == null || "".equals(jsonString)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULT_ARRAY);

        Trailer[] trailers = new Trailer[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            trailers[i] = new Trailer(
                    object.getString(TRAILER_ID),
                    object.getString(TRAILER_KEY),
                    object.getString(TRAILER_NAME)
            );

        }

        return trailers;
    }

    @Override
    protected void onPostExecute(Trailer[] trailers) {

        if (trailers != null) {
            mProgressBar.setVisibility(View.GONE);
            trailerTaskCallback.updateAdapter(trailers);
        }
        else{
            //showNoTrailerMessage(TrailerFragment.no_trailer_message);
        }

    }
}
