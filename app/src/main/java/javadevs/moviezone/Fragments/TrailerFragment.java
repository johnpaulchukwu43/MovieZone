package javadevs.moviezone.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collections;

import javadevs.moviezone.DetailActivity;
import javadevs.moviezone.R;
import javadevs.moviezone.Util.FetchMovieTrailerAsync;
import javadevs.moviezone.adapters.MovieTrailerAdapter;
import javadevs.moviezone.model.Trailer;

/**
 * Created by CHUKWU JOHNPAUL on 13/05/17.
 */

public class TrailerFragment extends Fragment {
    private static final String YOUTUBE_APP_PACKAGE = "com.google.android.youtube";
    private static final String YOUTUBE_URL_APP = "vnd.youtube://";
    private static final String YOUTUBE_URL_BROWSER = "https://www.youtube.com/watch";
    private static final String VIDEO_PARAMETER = "v";
    private static final String MOVIE_KEY = "movie_key";
    private static final String KEY_TRAILER_LIST = "key_trailer";
    private MovieTrailerAdapter movieTrailerAdapter;
    private ArrayList<Trailer> movieTrailersList;
    private int movieId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            movieId = DetailActivity.id;
            movieTrailersList = new ArrayList<>();
        } else {
            movieId = savedInstanceState.getInt(MOVIE_KEY);
            movieTrailersList = savedInstanceState.getParcelableArrayList(KEY_TRAILER_LIST);
        }
        updateTrailerAdapters(movieId);
    }

    public static TrailerFragment newInstance() {
        return new TrailerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_trailer, container, false);
//        TextView trailersTitle = (TextView) mView.findViewById(R.id.trailer_name);
//        ProgressBar trailerProgressBar = (ProgressBar) mView.findViewById(R.id.pb_trailer_loader);
//        TextView no_trailer_message = (TextView) mView.findViewById(R.id.no_trailer);
//        TextView errorMessage = (TextView) mView.findViewById(R.id.error_message);
        movieTrailersList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.rv_trailer);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        movieTrailerAdapter = new MovieTrailerAdapter(movieTrailersList,
                trailerKey -> {
                    if (trailerKey != null) {
                        playMovieTrailer(trailerKey);
                    }
                });
        recyclerView.setAdapter(movieTrailerAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        return mView;
    }

    public void updateTrailerAdapters(int movieId) {
        FetchMovieTrailerAsync fetchMovieTrailerAsync = new FetchMovieTrailerAsync(trailers -> {
            if (trailers != null) {
                movieTrailersList.clear();
                Collections.addAll(movieTrailersList, trailers);
                movieTrailerAdapter.notifyDataSetChanged();

            }
        }, getActivity());
        fetchMovieTrailerAsync.execute(movieId);
    }

    private void playMovieTrailer(String trailerKey) {
        Intent intent;
        if (checkForYouTubeApp()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL_APP + trailerKey));
        } else {
            Uri uri = Uri.parse(YOUTUBE_URL_BROWSER)
                    .buildUpon()
                    .appendQueryParameter(VIDEO_PARAMETER, trailerKey)
                    .build();
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent);
    }

    private boolean checkForYouTubeApp() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.d(getTag(), "checkForYouTubeApp: Activity null !");
            return false;
        }
        return activity.getPackageManager()
                .getLaunchIntentForPackage(YOUTUBE_APP_PACKAGE) != null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_TRAILER_LIST, movieTrailersList);
        outState.putInt(MOVIE_KEY, movieId);

    }


}
