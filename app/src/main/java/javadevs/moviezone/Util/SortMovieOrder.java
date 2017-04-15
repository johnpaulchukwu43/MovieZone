package javadevs.moviezone.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javadevs.moviezone.R;

/**
 * Created by CHUKWU JOHNPAUL on 15/04/17.
 */

public class SortMovieOrder {
    public static boolean topRated(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String SortKey = context.getString(R.string.pref_sort_key);
        String DefaultKey = context.getString(R.string.pref_sort_rating);
        String preferredSort = prefs.getString(SortKey,DefaultKey);
        String top_rated = context.getString(R.string.pref_sort_rating);
        boolean OrderByHighRated;
        if(top_rated.equals(preferredSort)){
            OrderByHighRated = true;
        }else {
            OrderByHighRated= false;
        }

        return OrderByHighRated;

    }
}
