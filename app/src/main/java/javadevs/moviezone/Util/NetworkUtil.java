package javadevs.moviezone.Util;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by CHUKWU JOHNPAUL on 15/04/17.
 */

public class NetworkUtil {

        private static final String TAG = NetworkUtil.class.getSimpleName();
          public static URL buildUrl(String topratedUrl) {
            String movieUrl = "";
            //If url is toprated we use the top_rated query else popular
            if (topratedUrl.equals("true")){
                movieUrl = "http://api.themoviedb.org/3/movie/top_rated?api_key=ef64a84df789083d3c9996e5c1e3c055";
            }else {
                movieUrl = "http://api.themoviedb.org/3/movie/popular?api_key=ef64a84df789083d3c9996e5c1e3c055";
            }
            Uri builtUri = Uri.parse(movieUrl).buildUpon().build();
            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Log.v(TAG, "Built URI " + url);
            return url;
        }

}
