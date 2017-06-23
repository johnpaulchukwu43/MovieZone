package javadevs.moviezone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import javadevs.moviezone.Interface.MovieCallBack;
import javadevs.moviezone.Interface.SearchCallBack;
import javadevs.moviezone.Util.FetchMovieAsync;
import javadevs.moviezone.Util.SearchMovieAsync;
import javadevs.moviezone.adapters.MovieAdapt;
import javadevs.moviezone.data.MoviesZoneContract;
import javadevs.moviezone.model.Movie;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String LISTS = "movie_list";
    private static final String KEY_SELECTED_POSITION = "SELECTED_POSITION";
    public static final String MOVIE = "movie" ;

    private int itemPosition = GridView.INVALID_POSITION;
    private MovieAdapt mAdapter;
    private ArrayList<Movie> myMovies;
    public AlertDialog.Builder mAlertDialog;
    public AlertDialog mAlert;
    public String sortingOrder;
    public static TextView empty_msg_txt;
    SharedPreferences prefs;
    public boolean IS_PREFERENCE_UPDATE = false;

    public static GridView mygridView;
    public static Button reloadbtn;
    
    public static final String API_KEY ="YOUR API_KEY";
    public static ProgressBar pb_loading_indicator;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchData(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();//Get the id menu clicked
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (id == R.id.action_most_popular) {
            changeSortOrder(R.string.pref_sort_popular_value);
            successLoad();
            return true;
        } else if (id == R.id.action_top_rating) {
            changeSortOrder(R.string.pref_sort_rating_value);
            successLoad();
            return true;
        } else if (id == R.id.action_favorite) {
            changeSortOrder(R.string.pref_sort_favourite);
            successLoad();
            return true;
        }
        else if (id == R.id.menu_refresh) {
            successLoad();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get Save Instances
        if(savedInstanceState == null || !savedInstanceState.containsKey(LISTS)){
            myMovies=new ArrayList<>();


        }else{

            myMovies=  savedInstanceState.getParcelableArrayList(LISTS);
            itemPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);

        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //get the default sorting Option
        sortingOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular_value));
        //reload btn
        reloadbtn = (Button)findViewById(R.id.refreshbtn);
        //txt message
        empty_msg_txt = (TextView)findViewById(R.id.no_search_found);
        //progressbar
        pb_loading_indicator = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        myMovies = new ArrayList<>();
        mAdapter = new MovieAdapt(myMovies,this);
        //GridView
        mygridView=(GridView) findViewById(R.id.my_grid_view);
        mygridView.setAdapter(mAdapter);
//grid view onclick Listener
        mygridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie mymovie= (Movie) mAdapter.getItem(i);
                itemPosition = i;
                Bundle bundle = new Bundle();
                bundle.putParcelable(MOVIE, mymovie);
                //Pass the movie info to the Detail page using an Intent
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id", mymovie.getId());
                intent.putExtra("title", mymovie.getTitle());
                intent.putExtra("overview",mymovie.getOverview());
                intent.putExtra("language", mymovie.getOriginalLanguage());
                intent.putExtra("date", mymovie.getReleaseDate());
                intent.putExtra("ratings", mymovie.getVoteAverage());
                intent.putExtra("poster",mymovie.getPosterPath());
                intent.putExtra("genre", mymovie.getGenreIdStrings());
                intent.putExtra("backdrop_path",mymovie.getBackdropPath());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
//        Create AlertDialog for Network Info
        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setMessage("No Network Connection,put on wifi or mobile data Refresh");
        mAlertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        mAlertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                successLoad();
            }
        });
        mAlertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        });
        mAlert = mAlertDialog.create();
        //Register the sharedPreference Listener on this class since it implements SharedPreferences.OnSharedPreferenceChangeListener
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
      //fire up the data
        successLoad();

    }

    @Override
    protected void onStart() {
        super.onStart();
            successLoad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void successLoad() {
            //get default shared Prefernce
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            sortingOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular_value));
            //sorting order  is not favourite
            if (!sortingOrder.equals(getString(R.string.pref_sort_favourite))) {
                if(CheckForMobileNetwork() || CheckForWifiNetwork()){//check if there is network connection
                    LoadData(sortingOrder);
                }
                else{
                    mAlert.show();

                }
            } else {
                LoadFavourite();
            }


    }
    //FETCH USERS FAVOURITE MOVIE FROM DB
    public void LoadFavourite() {
                    Cursor cursor = getContentResolver()
                    .query(MoviesZoneContract.MoviesEntry.CONTENT_URI, null, null, null, null);
           mAdapter.cleanUp();
            assert cursor != null;
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    Movie movie = new Movie(
                            cursor.getInt(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_MOVIE_ID)),
                            cursor.getString(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_POSTER_PATH)),
                            cursor.getString(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_OVERVIEW)),
                                cursor.getDouble(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_RATING)),
                            cursor.getString(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_LANGUAGE)),
                            cursor.getString(cursor.getColumnIndex(
                                    MoviesZoneContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH)));

                    myMovies.add(movie);
                } while (cursor.moveToNext());
            }
            else{
                Toast.makeText(this, "No Favourites Yet", Toast.LENGTH_SHORT).show();
            }
            mAdapter.notifyDataSetChanged();
            if (itemPosition != GridView.INVALID_POSITION) {
                mygridView.smoothScrollToPosition(itemPosition);
            }
            cursor.close();


    }
    @Override
    public void onRestart() {
        super.onRestart();
    }
//Fetch data
    public void LoadData(String order) {

        FetchMovieAsync moviesTask = new FetchMovieAsync(new MovieCallBack() {

            @Override
            public void updateData(Movie[] movies) {
                if (movies != null) {
                    mAdapter.cleanUp();
                    Collections.addAll(myMovies, movies);
                    mAdapter.notifyDataSetChanged();
                    if (itemPosition != GridView.INVALID_POSITION) {
                        mygridView.smoothScrollToPosition(itemPosition);

                    }
                }
            }
        });
        moviesTask.execute(order);
    }
    public void SearchData(String query){
        SearchMovieAsync searchTask = new SearchMovieAsync(new SearchCallBack() {
            @Override
            public void searchData(Movie[] movies) {
                if(movies!= null){
                    mAdapter.cleanUp();
                    Collections.addAll(myMovies,movies);
                    mAdapter.notifyDataSetChanged();
                }
                else{
                    mygridView.setVisibility(View.INVISIBLE);
                    empty_msg_txt.setVisibility(View.VISIBLE);
                }
            }
        });
        searchTask.execute(query);

    }
    @Override
    public void onSaveInstanceState (Bundle  outState) {
        outState.putParcelableArrayList(LISTS, myMovies);
        if (itemPosition != GridView.INVALID_POSITION) {
            outState.putInt(KEY_SELECTED_POSITION, itemPosition);
        }
        super.onSaveInstanceState(outState);

    }
    //check for wifi Connection
    public boolean CheckForWifiNetwork(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return wifi;
    }
    //check for Mobile Data Connection
    public boolean CheckForMobileNetwork(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return mobile;

    }
//SharedPreferenceChangeCallBack to watch for changes in the sharedPreferences
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
         sortingOrder = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_value));
        IS_PREFERENCE_UPDATE = true;
    }

    private void changeSortOrder(int sortOrder) {
        String orderKey = getResources().getString(sortOrder);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.pref_sort_key), orderKey);
        editor.apply();
    }

    public void refreshContent(View v){
        successLoad();
    }

    private void addScrollListener() {
        mygridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {

                }
            }


        });
    }
}
