package javadevs.moviezone;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Collections;
import javadevs.moviezone.Interface.MovieCallBack;
import javadevs.moviezone.Util.FetchMovieAsync;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String LISTS ="LISTOFMOVIES" ;
    private MovieAdapt mAdapter;
    private ArrayList<Movie> myMovies;
    public AlertDialog.Builder mAlertDialog;
    public AlertDialog mAlert;
    public String sortingOrder;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent settingsActivity = new Intent(this,SettingsActivity.class);
                startActivity(settingsActivity);
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
        }

        //GridLayout
        myMovies = new ArrayList<>();
        mAdapter = new MovieAdapt(myMovies,this);
        GridView mygridView=(GridView) findViewById(R.id.my_grid_view);
        mygridView.setAdapter(mAdapter);
//onclick Listener
        mygridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie mymovie= (Movie) mAdapter.getItem(i);
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("title",mymovie.getTitle());
                intent.putExtra("posterpath",mymovie.getPosterPath());
                intent.putExtra("overview",mymovie.getOverview());
                intent.putExtra("releasedate",mymovie.getReleaseDate());
                intent.putExtra("voteAverage",mymovie.getVoteAverage());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        //Progress

//        Create AlertDialog for Network Info
        mAlertDialog = new AlertDialog.Builder(this);
        mAlertDialog.setMessage("No Network Connection,put on wifi or mobile data and try again");
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

        //SharedPref
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
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

        if(CheckForMobileNetwork() || CheckForWifiNetwork()){
            LoadData();
        }
        else{
            mAlert.show();

        }

    }
    @Override
    public void onRestart() {
        super.onRestart();
    }

    private void LoadData() {

        FetchMovieAsync moviesTask = new FetchMovieAsync(new MovieCallBack() {
            @Override
            public void updateData(Movie[] mymovie) {
                if (mymovie != null) {
                    mAdapter.cleanUp();
                    Collections.addAll(myMovies, mymovie);
                    mAdapter.notifyDataSetChanged();
                }
            }

        });
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
         sortingOrder = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_value));
        moviesTask.execute(sortingOrder);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LISTS, myMovies);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
         sortingOrder = preferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_value));
    }
}
