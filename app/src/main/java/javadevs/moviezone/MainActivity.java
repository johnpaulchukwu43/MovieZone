package javadevs.moviezone;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import javadevs.moviezone.Util.NetworkUtil;
import javadevs.moviezone.Util.SortMovieOrder;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Movie> myMovies;
    public AlertDialog.Builder mAlertDialog;
    public AlertDialog mAlert;
    public GridLayoutManager gridLayoutManager;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;


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
        //GridLayout
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        //Set recyclerViewUp
        mRecyclerView = (RecyclerView) findViewById(R.id.mrecycle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //SetRecyclerAdapter
        myMovies = new ArrayList<>();
        mAdapter = new MovieAdapter(myMovies,this);
        mRecyclerView.setAdapter(mAdapter);
        successLoad();
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
    }

    @Override
    protected void onStart() {
        /*
         * If the preferences for sort order have changed since the user was last in
         * MainActivity, perform another query and set the flag to false.
         */
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED){
            Log.d(TAG, "onStart: preferences were updated");
            successLoad();
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }



    private void successLoad() {

            LoadData();
    }

    private void LoadData() {
        String istop_rated = "";
        boolean isTopOrder = SortMovieOrder.topRated(MainActivity.this);
        if (isTopOrder){
            istop_rated = "true";
        }else {
            istop_rated = "false";
        }
        String _URL =  NetworkUtil.buildUrl(istop_rated).toString();
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Feeds..Please Wait");
        mProgressDialog.show();
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, _URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                       try{
                           mProgressDialog.dismiss();

                           JSONObject mJsonObject = new JSONObject(response);
                           JSONArray mJsonArray = mJsonObject.getJSONArray("results");
                           for(int i=0;i<mJsonArray.length();i++){
                               JSONObject singleMovieItem = mJsonArray.getJSONObject(i);
                               Movie movieList = new Movie(
                                       singleMovieItem.getString("original_title"),
                                       singleMovieItem.getString("poster_path"),
                                       singleMovieItem.getString("overview"),
                                       singleMovieItem.getString("vote_average"),
                                       singleMovieItem.getString("release_date")
                               );
                               Log.w(TAG, "onResponse: what i get"+movieList.getPosterPath());
                               myMovies.add(movieList);
                           }
                           mAdapter = new MovieAdapter(myMovies,getApplicationContext());
                           mRecyclerView.setAdapter(mAdapter);

                       }
                       catch (Exception ex){
                           ex.printStackTrace();
                       }
                    }
                },
                new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Connection Failed..Refresh to try Again",Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                }
            });
        RequestQueue mRequestQueue =  Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(mStringRequest);
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
        PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}
