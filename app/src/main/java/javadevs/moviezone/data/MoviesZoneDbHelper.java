package javadevs.moviezone.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CHUKWU JOHNPAUL on 12/05/17.
 */

public class MoviesZoneDbHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "movie_db";
    private static final String CREATE_MOVIES_TABLE = "CREATE TABLE "
            + MoviesZoneContract.MoviesEntry.TABLE_NAME + "("
            + MoviesZoneContract.MoviesEntry._ID + " INTEGER PRIMARY KEY,"
            + MoviesZoneContract.MoviesEntry.COLUMN_TITLE + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_POSTER_PATH + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_LANGUAGE + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_RATING + " INTEGER,"
            + MoviesZoneContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_GENRE_ARRAY_STRING + " TEXT,"
            + MoviesZoneContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER,"
            + MoviesZoneContract.MoviesEntry.COLUMN_FAVOURITE + " INTEGER"
            + ")";


    public MoviesZoneDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create The Table 
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MoviesZoneContract.MoviesEntry.TABLE_NAME);
        // create new tables
        onCreate(db);
    }



}
