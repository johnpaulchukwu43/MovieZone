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

import javadevs.moviezone.Interface.ReviewCallBack;
import javadevs.moviezone.MainActivity;
import javadevs.moviezone.NetworkUtil;
import javadevs.moviezone.model.Review;

/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 * Clss Extends AsyncTask Class to fetch Review by user
 */

public class FetchMovieReviewAsync extends AsyncTask<Integer, Void, Review[]> {

    private final ReviewCallBack taskCallback;
    private final Context ctx;
    private ProgressBar r_pb;
    private TextView mTextView;


    public FetchMovieReviewAsync(ReviewCallBack taskCallback,Context ctx) {
        this.taskCallback = taskCallback;
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mTextView = new TextView(ctx);
        mTextView.setVisibility(View.INVISIBLE);
        mTextView.setText("No Reviews for this Movie");
        r_pb = new ProgressBar(ctx);
        r_pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected Review[] doInBackground(Integer... integers) {
        if (integers.length == 0) {
            return null;
        }

        final String BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String TYPE = "reviews";
        final String API_KEY = "api_key";

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(String.valueOf(integers[0]))
                .appendEncodedPath(TYPE)
                .appendQueryParameter(API_KEY, MainActivity.API_KEY)
                .build();

        String jsonString = NetworkUtil.getJsonString(uri);

        try {
            return getReviewsFromJson(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Review[] getReviewsFromJson(String jsonString) throws JSONException {
        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";
        final String TOTAL_PAGES = "total_pages";
        final String RESULT_ARRAY = "results";

        if (jsonString == null || "".equals(jsonString)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(jsonString);
        int pages = jsonObject.getInt(TOTAL_PAGES);
        JSONArray jsonArray = jsonObject.getJSONArray(RESULT_ARRAY);
        Review[] reviews = new Review[jsonArray.length()];

        for (int i = 1; i <= pages; i++) {
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = jsonArray.getJSONObject(j);
                reviews[j] = new Review(
                        object.optString(REVIEW_ID),
                        object.getString(REVIEW_AUTHOR),
                        object.getString(REVIEW_CONTENT)
                );
            }
        }
        return reviews;
    }

    @Override
    protected void onPostExecute(Review[] reviews) {
        r_pb.setVisibility(View.INVISIBLE);
        if (reviews != null) {
            mTextView.setVisibility(View.INVISIBLE);
            taskCallback.updateAdapter(reviews);
        }
        else{
            mTextView.setVisibility(View.VISIBLE);
        }
    }


}
