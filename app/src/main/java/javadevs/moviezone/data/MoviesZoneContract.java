package javadevs.moviezone.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by CHUKWU JOHNPAUL on 12/05/17.
 */

public class MoviesZoneContract {

    private MoviesZoneContract() {
    }

    public static final String CONTENT_AUTHORITY = "javadevs.moviezone";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String USER_FAVOURITE_MOVIES = "favorite_movies";

    public static final class MoviesEntry implements BaseColumns {
        private MoviesEntry() {
        }

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(USER_FAVOURITE_MOVIES)
                .build();
        public static final String TABLE_NAME = "movies";
        public static final String _ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKGROUND_IMAGE_PATH = "background_image";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_FAVOURITE = "favorite";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_GENRE_ARRAY_STRING = "genre_array_string";

    }

}
