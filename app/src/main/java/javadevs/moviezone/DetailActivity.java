package javadevs.moviezone;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import javadevs.moviezone.Fragments.OverviewFragment;
import javadevs.moviezone.Fragments.ReviewFragment;
import javadevs.moviezone.Fragments.TrailerFragment;
import javadevs.moviezone.data.MoviesZoneContract;
import javadevs.moviezone.data.MoviesZoneDbHelper;

public class DetailActivity extends AppCompatActivity {
    String backdrop_path, get_title, get_language, get_releasedate,poster, release_date;
    double get_ratings;
    public static int id;
    FloatingActionButton fab;

    private boolean isMarkFavorite;
    MoviesZoneSection mMovieZoneSection;
    TabLayout tablayout;
    ViewPager MovieViewPager;

    SQLiteDatabase movieZoneDB;
    ImageView backdropImg;
    TextView title,language_date,ratings,genre;
    MoviesZoneDbHelper myHelper;
    NestedScrollView scrollView;
    public static String movie_overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        //FAB Setup
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_favourite);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add Movie to Favourite
                toggleFavourite();


            }
        });
        //Display all Movie Views
        displayViews();
    }

    private void displayViews() {
        backdropImg = (ImageView) findViewById(R.id.poster);
        title = (TextView) findViewById(R.id.tv_movie_title);
        language_date = (TextView) findViewById(R.id.tv_movie_language_date);
        ratings = (TextView) findViewById(R.id.tv_movie_ratings);
        genre = (TextView) findViewById(R.id.tv_movie_genres);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        if (scrollView != null) {

            scrollView.setFillViewport(true);
        }
        tablayout = (TabLayout) findViewById(R.id.movie_details_tab);
        //ViewPager
        MovieViewPager = (ViewPager) findViewById(R.id.movie_details_container);
        //MoviesZoneHelper Class an extension of SQLite Helper Clas
        myHelper = new MoviesZoneDbHelper(this);
        //MovieZoneSection an extension of FragmentPageAdpter
        mMovieZoneSection = new MoviesZoneSection(getSupportFragmentManager());
        //Set MovieZoneSection as an adapter for Viewpager
        MovieViewPager.setAdapter(mMovieZoneSection);
        tablayout.setupWithViewPager(MovieViewPager);
        //get Movie data using an Intent sent from the MainActivity
        Intent getMovieData = getIntent();
        if (getMovieData != null) {
            backdrop_path = "http://image.tmdb.org/t/p/" + "w342/" + getMovieData.getStringExtra("backdrop_path");
            Picasso.with(this).load(backdrop_path).into(backdropImg);
            id = getMovieData.getIntExtra("id",0);
            get_title = getMovieData.getStringExtra("title");
            get_language = getMovieData.getStringExtra("language");
            get_releasedate = getMovieData.getStringExtra("date");
             get_ratings = getMovieData.getDoubleExtra("ratings",0);
            poster = getMovieData.getStringExtra("poster");
            movie_overview = getMovieData.getStringExtra("overview");
            release_date = get_releasedate;
            title.setText(get_title);
            language_date.setText(release_date + " | " + get_language.toUpperCase());
            ratings.setText(String.valueOf(get_ratings));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                startIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    
    private void toggleFavourite() {
        //First Check if movie is in DB is true, then delete it,else add it to the db
        if(isMovieInDB()){
            String stringId = String.valueOf(id);
            String whereClause = MoviesZoneContract.MoviesEntry.COLUMN_MOVIE_ID + "=" + stringId;
            Uri deleteUri = MoviesZoneContract.MoviesEntry.CONTENT_URI;
            deleteUri = deleteUri.buildUpon().appendPath(stringId).build();
            getContentResolver().delete(deleteUri, whereClause, null);
            isMarkFavorite = false;
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
            Toast.makeText(this, "Removed from Favourite", Toast.LENGTH_LONG).show();

        }
        else{
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_MOVIE_ID, id);
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_TITLE, get_title);
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_POSTER_PATH,poster);
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_BACKGROUND_IMAGE_PATH,backdrop_path);
           contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_OVERVIEW,movie_overview);
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_RATING, get_ratings);
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_RELEASE_DATE, release_date);
            contentValues.put(MoviesZoneContract.MoviesEntry.COLUMN_LANGUAGE,get_language);
            getContentResolver()
                    .insert(MoviesZoneContract.MoviesEntry.CONTENT_URI, contentValues);
            isMarkFavorite = true;
            fab.setBackgroundTintList(getResources().getColorStateList(R.color.red));
            Toast.makeText(this, "Added to Favourite", Toast.LENGTH_LONG).show();
        }
    }

    //Method to check if Movie exists in DB already
    private boolean isMovieInDB() {
        //use myhelper, object of MovieZoneDbHelper to return current db
        movieZoneDB = myHelper.getReadableDatabase();
        String whereClause = MoviesZoneContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] whereArgs = {String.valueOf(id)};
        Cursor cursor = movieZoneDB.query(
                MoviesZoneContract.MoviesEntry.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            cursor.close();
            return false;

        } else {
            cursor.close();
            return true;

        }


    }



    //Method to start an implicit intent to share Movie
    private void startIntent() {
        Intent shared = new Intent((Intent.ACTION_SEND));
        String userInfoSend = "Check out this awesome Movie:\n"+get_title
                +"\nOverview:\n"+movie_overview+"\n"+
                "Release Date:\t"+release_date+
                "\n With Ratings:\t"+String.valueOf(get_ratings);
        shared.setType("text/plain");
        shared.putExtra( Intent.EXTRA_TEXT,userInfoSend);
        shared.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(shared);

    }

    private class MoviesZoneSection extends FragmentPagerAdapter{

        public MoviesZoneSection(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return OverviewFragment.newInstance();
            } else if (position == 1) {
                return ReviewFragment.newInstance();
            }
            else if (position == 2) {
                return TrailerFragment.newInstance();
            }

            else {
                return OverviewFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Reviews";
                case 2:
                    return "Trailers";
            }
            return null;
        }
    }
}
