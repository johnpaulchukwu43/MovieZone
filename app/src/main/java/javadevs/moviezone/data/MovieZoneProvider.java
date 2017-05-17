package javadevs.moviezone.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static javadevs.moviezone.data.MoviesZoneContract.MoviesEntry.TABLE_NAME;

/**
 * Created by CHUKWU JOHNPAUL on 12/05/17.
 */


public class MovieZoneProvider extends ContentProvider {
    private MoviesZoneDbHelper movieZoneDbHelper;
    public static final int USER_FAVORITE_MOVIES = 100;
    public static final int USER_FAVORITE_MOVIES_WITH_ID = 101;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        //pass the authority,path,and Id
        matcher.addURI(MoviesZoneContract. CONTENT_AUTHORITY,MoviesZoneContract.USER_FAVOURITE_MOVIES,USER_FAVORITE_MOVIES);
        matcher.addURI(MoviesZoneContract.CONTENT_AUTHORITY,MoviesZoneContract.USER_FAVOURITE_MOVIES+"/#",USER_FAVORITE_MOVIES_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Context ctx =  getContext();
        movieZoneDbHelper = new MoviesZoneDbHelper(ctx);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor query_cursor = null;
        switch (mUriMatcher.match(uri)) {

            case USER_FAVORITE_MOVIES: {

                query_cursor = movieZoneDbHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

                break;

            }

        }
        query_cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return query_cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = movieZoneDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = mUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case USER_FAVORITE_MOVIES:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(TABLE_NAME, null, contentValues);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(MoviesZoneContract.MoviesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        /* Users of the delete method will expect the number of rows deleted to be returned. */
        int numRowsDeleted;

        /*
         * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
         * deleted. However, if we do pass null and delete all of the rows in the table, we won't
         * know how many rows were deleted. According to the documentation for SQLiteDatabase,
         * passing "1" for the selection will delete all rows and return the number of rows
         * deleted, which is what the caller of this method expects.
         */
        if (null == selection) selection = "1";

        switch (mUriMatcher.match(uri)) {

            case USER_FAVORITE_MOVIES_WITH_ID:
                numRowsDeleted = movieZoneDbHelper.getWritableDatabase().delete(
                        TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

}
